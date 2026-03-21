package ru.nstu.isma.server.app

import io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.protobuf.services.ProtoReflectionServiceV1
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollServerDomainSocketChannel
import io.netty.channel.unix.DomainSocketAddress
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import ru.nstu.isma.domain.domainModule
import ru.nstu.isma.server.infrastructure.infrastructureModule
import ru.nstu.isma.server.app.grpc.SimulationServiceGrpcImpl
import java.io.File
import java.util.UUID

fun main() {
    startKoin {
        modules(domainModule, infrastructureModule, appModule)
    }

    val koin = object : KoinComponent {
        val grpcService: SimulationServiceGrpcImpl by inject()
    }

    val socketPath = "${System.getProperty("java.io.tmpdir")}/isma-${UUID.randomUUID()}.sock"

    File(socketPath).delete()

    val bossGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())
    val workerGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

    val server = NettyServerBuilder
        .forAddress(DomainSocketAddress(socketPath))
        .channelType(EpollServerDomainSocketChannel::class.java)
        .bossEventLoopGroup(bossGroup)
        .workerEventLoopGroup(workerGroup)
        .addService(koin.grpcService)
        .addService(ProtoReflectionServiceV1.newInstance())
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