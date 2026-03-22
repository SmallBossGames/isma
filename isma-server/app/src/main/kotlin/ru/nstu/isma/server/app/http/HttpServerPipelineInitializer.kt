package ru.nstu.isma.server.app.http

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.handler.codec.http.HttpServerCodec

class HttpServerPipelineInitializer(
    private val sessionStore: ru.nstu.isma.domain.simulation.ISimulationSessionStore,
) : ChannelInitializer<Channel>() {

    override fun initChannel(ch: Channel) {
        ch.pipeline().apply {
            addLast(HttpServerCodec())
            addLast(SimulationResultHttpHandler(sessionStore))
        }
    }
}
