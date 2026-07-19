package ru.nstu.isma.server.app

import io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.services.ProtoReflectionServiceV1
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.unix.DomainSocketAddress

object WindowsServerSetup {
    fun createGrpcHandles(
        socketPath: String,
        koin: KoinHolder
    ): GrpcServerHandles {
        val bossGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())
        val workerGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())

        val grpcServer = NettyServerBuilder
            .forAddress(DomainSocketAddress(socketPath))
            .channelType(NioServerSocketChannel::class.java)
            .bossEventLoopGroup(bossGroup)
            .workerEventLoopGroup(workerGroup)
            .addService(koin.grpcService)
            .addService(koin.compilerService)
            .addService(ProtoReflectionServiceV1.newInstance())
            .build()

        return GrpcServerHandles(grpcServer, bossGroup, workerGroup)
    }
}
