package ru.nstu.isma.server.app

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import ru.nstu.isma.domain.domainModule
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.server.app.grpc.LismaCompilerServiceGrpcImpl
import ru.nstu.isma.server.app.grpc.SimulationServiceGrpcImpl
import ru.nstu.isma.server.app.http.simulationResultRoutes
import ru.nstu.isma.server.infrastructure.infrastructureModule
import java.io.File
import java.util.*

class KoinHolder(
    val grpcService: SimulationServiceGrpcImpl,
    val compilerService: LismaCompilerServiceGrpcImpl,
    val sessionStore: ISimulationSessionStore
)

fun main(args: Array<String>) {
    val cliArgs = args.toList()
    val socketPath = cliArgs.indexOf("--socket-path").let { idx ->
        if (idx >= 0 && idx + 1 < args.size) args[idx + 1]
        else "${System.getProperty("java.io.tmpdir")}/isma-${UUID.randomUUID()}.sock"
    }
    val httpSocketPath = cliArgs.indexOf("--http-socket-path").let { idx ->
        if (idx >= 0 && idx + 1 < args.size) args[idx + 1]
        else "${System.getProperty("java.io.tmpdir")}/isma-http-${UUID.randomUUID()}.sock"
    }

    startKoin {
        modules(domainModule, infrastructureModule, appModule)
    }

    File(socketPath).delete()
    File(httpSocketPath).delete()

    val koinObj = object : KoinComponent {
        val grpcService: SimulationServiceGrpcImpl by inject()
        val compilerService: LismaCompilerServiceGrpcImpl by inject()
        val sessionStore: ISimulationSessionStore by inject()
    }
    val koin = KoinHolder(koinObj.grpcService, koinObj.compilerService, koinObj.sessionStore)

    val isLinux = System.getProperty("os.name")?.contains("linux", ignoreCase = true) == true
    val grpcHandles = if (isLinux) {
        LinuxServerSetup.createGrpcHandles(socketPath, koin)
    } else {
        WindowsServerSetup.createGrpcHandles(socketPath, koin)
    }

    val httpServer = embeddedServer(CIO, configure = {
        unixConnector(httpSocketPath) { }
    }) {
        routing {
            simulationResultRoutes(koin.sessionStore)
        }
    }

    httpServer.start(wait = false)

    val handles = ServerHandles(
        grpcHandles.grpcServer,
        httpServer,
        grpcHandles.bossGroup,
        grpcHandles.workerGroup
    )

    println("Starting gRPC server on Unix socket: $socketPath")
    println("Starting HTTP server on Unix socket: $httpSocketPath")
    handles.grpcServer.start()
    println("GRPC_SOCKET=$socketPath")
    println("HTTP_SOCKET=$httpSocketPath")
    println("Servers started. Shutting down with Ctrl+C...")

    Runtime.getRuntime().addShutdownHook(
        Thread {
            handles.grpcServer.shutdown()
            httpServer.stop(1000, 2000)
            handles.bossGroup.shutdownGracefully().sync()
            handles.workerGroup.shutdownGracefully().sync()
            File(socketPath).delete()
            File(httpSocketPath).delete()
        }
    )

    handles.grpcServer.awaitTermination()
}
