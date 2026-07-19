package ru.isma.next.external

import io.grpc.ManagedChannel
import ru.nstu.isma.contracts.v1.compiler_service.HighlightRequest
import ru.nstu.isma.contracts.v1.compiler_service.HighlightResponse
import ru.nstu.isma.contracts.v1.compiler_service.LismaCompilerServiceGrpc

class GrpcLismaCompilerClient(socketPath: String) {
    private val isLinux = System.getProperty("os.name")?.contains("linux", ignoreCase = true) == true

    private val handle = if (isLinux) {
        LinuxGrpcClient.createHandle(socketPath)
    } else {
        WindowsGrpcClient.createHandle(socketPath)
    }

    private val channel: ManagedChannel get() = handle.channel
    val blockingStub = LismaCompilerServiceGrpc.newBlockingStub(channel)!!

    fun highlight(sourceCode: String): HighlightResponse {
        val request = HighlightRequest.newBuilder()
            .setSourceCode(sourceCode)
            .build()
        return blockingStub.highlight(request)
    }

    fun shutdown() {
        channel.shutdown()
        handle.eventLoop.shutdownGracefully()
    }
}
