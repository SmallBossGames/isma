package ru.isma.next.external

object WindowsGrpcClient {

    fun createHandle(socketPath: String): GrpcChannelHandle {
        val eventLoop = io.netty.channel.MultiThreadIoEventLoopGroup(
            io.netty.channel.nio.NioIoHandler.newFactory()
        )
        val channel = io.grpc.netty.NettyChannelBuilder
            .forAddress(io.netty.channel.unix.DomainSocketAddress(socketPath))
            .channelType(io.netty.channel.socket.nio.NioDomainSocketChannel::class.java)
            .eventLoopGroup(eventLoop)
            .negotiationType(io.grpc.netty.NegotiationType.PLAINTEXT)
            .keepAliveTime(45, java.util.concurrent.TimeUnit.SECONDS)
            .keepAliveWithoutCalls(true)
            .maxInboundMessageSize(1024 * 1024 * 512)
            .build()
        return GrpcChannelHandle(channel, eventLoop)
    }
}
