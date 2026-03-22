package ru.isma.next.external

import io.grpc.ManagedChannel
import io.grpc.netty.NettyChannelBuilder
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollDomainSocketChannel
import io.netty.channel.unix.DomainSocketAddress
import ru.nstu.isma.contracts.simulation.SimulationServiceGrpc
import ru.nstu.isma.contracts.simulation.SimulationServiceGrpc.SimulationServiceBlockingStub
import ru.nstu.isma.contracts.simulation.SimulationServiceGrpc.SimulationServiceStub

class GrpcSimulationClient(socketPath: String) {
    private val eventLoopGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

    private val channel: ManagedChannel = NettyChannelBuilder
        .forAddress(DomainSocketAddress(socketPath))
        .channelType(EpollDomainSocketChannel::class.java)
        .eventLoopGroup(eventLoopGroup)
        .negotiationType(io.grpc.netty.NegotiationType.PLAINTEXT)
        .keepAliveTime(365 * 24 * 3600, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val blockingStub: SimulationServiceBlockingStub = SimulationServiceGrpc.newBlockingStub(channel)
    val asyncStub: SimulationServiceStub = SimulationServiceGrpc.newStub(channel)

    fun shutdown() {
        channel.shutdown()
        eventLoopGroup.shutdownGracefully()
    }
}
