package ru.nstu.isma.server.app

import io.grpc.netty.NettyServerBuilder
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerDomainSocketChannel
import io.netty.channel.unix.DomainSocketAddress
import ru.nstu.isma.domain.handlers.getSimulationResult.GetSimulationResultHandlerImpl
import ru.nstu.isma.domain.handlers.listSimulationMethods.ListSimulationMethodsHandlerImpl
import ru.nstu.isma.domain.handlers.monitorSimulation.MonitorSimulationHandlerImpl
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationHandlerImpl
import ru.nstu.isma.server.app.grpc.SimulationServiceGrpcImpl
import java.io.File
import java.util.UUID

fun main() {
    val socketPath = "${System.getProperty("java.io.tmpdir")}/isma-${UUID.randomUUID()}.sock"

    File(socketPath).delete()

    val runSimulationHandler = RunSimulationHandlerImpl()
    val getSimulationResultHandler = GetSimulationResultHandlerImpl()
    val monitorSimulationHandler = MonitorSimulationHandlerImpl()
    val listSimulationMethodsHandler = ListSimulationMethodsHandlerImpl()

    val grpcService = SimulationServiceGrpcImpl(
        runSimulationHandler = runSimulationHandler,
        getSimulationResultHandler = getSimulationResultHandler,
        monitorSimulationHandler = monitorSimulationHandler,
        listSimulationMethodsHandler = listSimulationMethodsHandler,
    )

    val bossGroup = EpollEventLoopGroup(1)
    val workerGroup = EpollEventLoopGroup()

    val server = NettyServerBuilder
        .forAddress(DomainSocketAddress(socketPath))
        .channelType(EpollServerDomainSocketChannel::class.java)
        .bossEventLoopGroup(bossGroup)
        .workerEventLoopGroup(workerGroup)
        .addService(grpcService)
        .build()

    println("Starting gRPC server on Unix socket: $socketPath")
    server.start()
    println("Server started. Shutting down with Ctrl+C...")

    Runtime.getRuntime().addShutdownHook(
        Thread {
            server.shutdown()
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
            File(socketPath).delete()
        }
    )

    server.awaitTermination()
}
