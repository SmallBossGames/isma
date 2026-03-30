package ru.isma.next.external

import io.grpc.ManagedChannel
import io.grpc.netty.NettyChannelBuilder
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollDomainSocketChannel
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.unix.DomainSocketAddress
import ru.nstu.isma.contracts.simulation.HighlightRequest
import ru.nstu.isma.contracts.simulation.HighlightResponse
import ru.nstu.isma.contracts.simulation.LismaCompilerServiceGrpc

class GrpcLismaCompilerClient(socketPath: String) {
    private val eventLoopGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

    private val channel: ManagedChannel = NettyChannelBuilder
        .forAddress(DomainSocketAddress(socketPath))
        .channelType(EpollDomainSocketChannel::class.java)
        .eventLoopGroup(eventLoopGroup)
        .negotiationType(io.grpc.netty.NegotiationType.PLAINTEXT)
        .keepAliveTime(365 * 24 * 3600, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val blockingStub = LismaCompilerServiceGrpc.newBlockingStub(channel)!!

    fun highlight(sourceCode: String): HighlightResponse {
        val request = HighlightRequest.newBuilder()
            .setSourceCode(sourceCode)
            .build()
        return blockingStub.highlight(request)
    }

    fun shutdown() {
        channel.shutdown()
        eventLoopGroup.shutdownGracefully()
    }
}
