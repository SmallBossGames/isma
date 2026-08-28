package ru.nstu.isma.server.app

import io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.services.ProtoReflectionServiceV1
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollServerDomainSocketChannel
import io.netty.channel.unix.DomainSocketAddress

data class GrpcServerHandles(
    val grpcServer: io.grpc.Server,
    val bossGroup: MultiThreadIoEventLoopGroup,
    val workerGroup: MultiThreadIoEventLoopGroup,
)

object LinuxServerSetup {
    fun createGrpcHandles(
        socketPath: String,
        koin: KoinHolder
    ): GrpcServerHandles {
        val bossGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())
        val workerGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

        val grpcServer = NettyServerBuilder
            .forAddress(DomainSocketAddress(socketPath))
            .channelType(EpollServerDomainSocketChannel::class.java)
            .bossEventLoopGroup(bossGroup)
            .workerEventLoopGroup(workerGroup)
            .addService(koin.grpcService)
            .addService(koin.compilerService)
            .addService(ProtoReflectionServiceV1.newInstance())
            .build()

        return GrpcServerHandles(grpcServer, bossGroup, workerGroup)
    }
}
