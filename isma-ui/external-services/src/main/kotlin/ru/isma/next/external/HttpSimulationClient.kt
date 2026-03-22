package ru.isma.next.external

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.epoll.EpollDomainSocketChannel
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.unix.DomainSocketAddress
import io.netty.handler.codec.http.DefaultFullHttpRequest
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.LastHttpContent
import io.netty.util.ReferenceCountUtil
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HttpSimulationClient(
    private val socketPath: String,
    private val timeoutSeconds: Long = 30,
) {
    fun download(urlPath: String): ByteArray {
        val latch = CountDownLatch(1)
        var result: ByteArray? = null
        var error: Throwable? = null
        val chunks = mutableListOf<ByteArray>()

        @Suppress("DEPRECATION")
        val group = EpollEventLoopGroup(1)
        try {
            val handler = object : ChannelInboundHandlerAdapter() {
                private var gotResponse = false

                override fun channelActive(ctx: ChannelHandlerContext) {
                    val request = DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, urlPath)
                    request.headers().set(HttpHeaderNames.HOST, "localhost")
                    ctx.writeAndFlush(request)
                }

                override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                    try {
                        when (msg) {
                            is HttpResponse -> {
                                if (msg.status() != HttpResponseStatus.OK) {
                                    error = HttpDownloadException("HTTP error: ${msg.status()}")
                                    latch.countDown()
                                    return
                                }
                                gotResponse = true
                            }
                            is HttpContent -> {
                                val content = msg.content()
                                if (content.readableBytes() > 0) {
                                    val bytes = ByteArray(content.readableBytes())
                                    content.readBytes(bytes)
                                    chunks.add(bytes)
                                }
                                if (msg is LastHttpContent) {
                                    result = combineChunks(chunks)
                                    latch.countDown()
                                }
                            }
                            else -> ctx.fireChannelRead(msg)
                        }
                    } finally {
                        ReferenceCountUtil.release(msg)
                    }
                }

                override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                    error = HttpDownloadException(cause.message ?: "Unknown error")
                    latch.countDown()
                }

                override fun channelInactive(ctx: ChannelHandlerContext) {
                    if (latch.count > 0 && result == null) {
                        error = HttpDownloadException("Connection closed")
                        latch.countDown()
                    }
                }
            }

            val channel = Bootstrap()
                .group(group)
                .channel(EpollDomainSocketChannel::class.java)
                .handler(object : io.netty.channel.ChannelInitializer<EpollDomainSocketChannel>() {
                    override fun initChannel(ch: EpollDomainSocketChannel) {
                        ch.pipeline().addLast(HttpClientCodec())
                        ch.pipeline().addLast(handler)
                    }
                })
                .connect(DomainSocketAddress(socketPath))
                .channel()

            val completed = latch.await(timeoutSeconds, TimeUnit.SECONDS)
            channel.close()

            if (!completed) throw HttpDownloadException("Request timed out after ${timeoutSeconds}s")
            if (error != null) throw error!!
            return result ?: throw HttpDownloadException("No response received")
        } finally {
            group.shutdownGracefully()
        }
    }

    private fun combineChunks(chunks: List<ByteArray>): ByteArray {
        val totalSize = chunks.sumOf { it.size }
        val result = ByteArray(totalSize)
        var offset = 0
        for (chunk in chunks) {
            chunk.copyInto(result, offset)
            offset += chunk.size
        }
        return result
    }
}

class HttpDownloadException(message: String) : RuntimeException(message)
