package ru.isma.next.external

import io.grpc.ManagedChannel
import ru.nstu.isma.contracts.v1.simulation_service.SimulationServiceGrpc

class GrpcSimulationClient(socketPath: String) {
    private val isLinux = System.getProperty("os.name")?.contains("linux", ignoreCase = true) == true

    private val handle = if (isLinux) {
        LinuxGrpcClient.createHandle(socketPath)
    } else {
        WindowsGrpcClient.createHandle(socketPath)
    }

    private val channel: ManagedChannel get() = handle.channel
    val blockingStub = SimulationServiceGrpc.newBlockingStub(channel)

    fun shutdown() {
        channel.shutdown()
        handle.eventLoop.shutdownGracefully()
    }
}
