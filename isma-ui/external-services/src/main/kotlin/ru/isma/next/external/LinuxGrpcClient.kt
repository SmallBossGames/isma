package ru.isma.next.external

import io.grpc.ManagedChannel
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollDomainSocketChannel
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.unix.DomainSocketAddress

data class GrpcChannelHandle(
    val channel: ManagedChannel,
    val eventLoop: MultiThreadIoEventLoopGroup
)

object LinuxGrpcClient {
    fun createHandle(socketPath: String): GrpcChannelHandle {
        val eventLoopGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

        val channel = io.grpc.netty.NettyChannelBuilder
            .forAddress(DomainSocketAddress(socketPath))
            .channelType(EpollDomainSocketChannel::class.java)
            .eventLoopGroup(eventLoopGroup)
            .negotiationType(io.grpc.netty.NegotiationType.PLAINTEXT)
            .keepAliveTime(365 * 24 * 3600, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        return GrpcChannelHandle(channel, eventLoopGroup)
    }
}
