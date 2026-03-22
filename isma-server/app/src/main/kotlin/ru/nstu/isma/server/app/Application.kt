package ru.nstu.isma.server.app

import io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.services.ProtoReflectionServiceV1
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollServerDomainSocketChannel
import io.netty.channel.unix.DomainSocketAddress
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import ru.nstu.isma.domain.domainModule
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.server.app.http.HttpServerPipelineInitializer
import ru.nstu.isma.server.infrastructure.infrastructureModule
import ru.nstu.isma.server.app.grpc.SimulationServiceGrpcImpl
import java.io.File
import java.util.UUID

var globalHttpSocketPath = ""

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

    globalHttpSocketPath = httpSocketPath

    startKoin {
        modules(domainModule, infrastructureModule, appModule)
    }

    val koin = object : KoinComponent {
        val grpcService: SimulationServiceGrpcImpl by inject()
        val sessionStore: ISimulationSessionStore by inject()
    }

    File(socketPath).delete()
    File(httpSocketPath).delete()

    val bossGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())
    val workerGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

    val grpcServer = NettyServerBuilder
        .forAddress(DomainSocketAddress(socketPath))
        .channelType(EpollServerDomainSocketChannel::class.java)
        .bossEventLoopGroup(bossGroup)
        .workerEventLoopGroup(workerGroup)
        .addService(koin.grpcService)
        .addService(ProtoReflectionServiceV1.newInstance())
        .build()

    val httpChannel: Channel = ServerBootstrap()
        .group(bossGroup, workerGroup)
        .channel(EpollServerDomainSocketChannel::class.java)
        .childHandler(HttpServerPipelineInitializer(koin.sessionStore))
        .bind(DomainSocketAddress(httpSocketPath))
        .await()
        .channel()

    println("Starting gRPC server on Unix socket: $socketPath")
    println("Starting HTTP server on Unix socket: $httpSocketPath")
    grpcServer.start()
    println("GRPC_SOCKET=$socketPath")
    println("HTTP_SOCKET=$httpSocketPath")
    println("Servers started. Shutting down with Ctrl+C...")

    Runtime.getRuntime().addShutdownHook(
        Thread {
            grpcServer.shutdown()
            httpChannel.close().sync()
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
            File(socketPath).delete()
            File(httpSocketPath).delete()
        }
    )

    grpcServer.awaitTermination()
}