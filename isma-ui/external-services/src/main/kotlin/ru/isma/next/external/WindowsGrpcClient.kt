package ru.isma.next.external

object WindowsGrpcClient {

    fun createHandle(socketPath: String): GrpcChannelHandle {
        val eventLoop = io.netty.channel.MultiThreadIoEventLoopGroup(
            io.netty.channel.nio.NioIoHandler.newFactory()
        )
        val channel = io.grpc.netty.NettyChannelBuilder
            .forAddress(io.netty.channel.unix.DomainSocketAddress(socketPath))
            .channelType(io.netty.channel.socket.nio.NioSocketChannel::class.java)
            .eventLoopGroup(eventLoop)
            .negotiationType(io.grpc.netty.NegotiationType.PLAINTEXT)
            .keepAliveTime(365 * 24 * 3600, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        return GrpcChannelHandle(channel, eventLoop)
    }
}
