package ru.nstu.isma.daemon.app

import io.netty.channel.MultiThreadIoEventLoopGroup
import io.ktor.server.engine.EmbeddedServer

data class ServerHandles(
    val grpcServer: io.grpc.Server,
    val httpServer: EmbeddedServer<*, *>,
    val bossGroup: MultiThreadIoEventLoopGroup,
    val workerGroup: MultiThreadIoEventLoopGroup,
)
