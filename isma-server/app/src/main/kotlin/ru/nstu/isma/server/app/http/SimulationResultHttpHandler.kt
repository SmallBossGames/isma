package ru.nstu.isma.server.app.http

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.*
import io.netty.util.ReferenceCountUtil
import org.slf4j.LoggerFactory
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus
import java.io.File
import java.io.FileInputStream
import java.util.regex.Pattern

class SimulationResultHttpHandler(
    private val sessionStore: ISimulationSessionStore,
) : ChannelInboundHandlerAdapter() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SimulationResultHttpHandler::class.java)
        private val DOWNLOAD_PATTERN = Pattern.compile("^/simulation/(\\d+)/download$")
        private const val CHUNK_SIZE = 8192
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is HttpRequest) {
            ctx.fireChannelRead(msg)
            return
        }
        val request = msg
        try {
            val path = request.uri()
            val method = request.method()

            if (method != HttpMethod.GET) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "Method Not Allowed")
                return
            }

            val matcher = DOWNLOAD_PATTERN.matcher(path)
            if (!matcher.matches()) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND, "Not Found")
                return
            }

            val simulationId = matcher.group(1)?.toLongOrNull()
            if (simulationId == null) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Invalid simulation ID")
                return
            }

            val session = sessionStore.get(simulationId)
            if (session == null || session.status != SimulationStatus.COMPLETED) {
                val status = if (session == null) "not found" else "not completed (${session.status})"
                sendError(ctx, HttpResponseStatus.NOT_FOUND, "Simulation $simulationId: $status")
                return
            }

            val resultFilePath = session.resultFilePath
            if (resultFilePath == null || !File(resultFilePath).exists()) {
                LOGGER.error("Result file missing for simulation $simulationId: $resultFilePath")
                sendError(ctx, HttpResponseStatus.NOT_FOUND, "Result file not found")
                return
            }

            sendFile(ctx, File(resultFilePath), simulationId)
        } finally {
            ReferenceCountUtil.release(request)
        }
    }

    private fun sendFile(ctx: ChannelHandlerContext, file: File, simulationId: Long) {
        val response = DefaultHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK
        )

        response.headers().apply {
            set(HttpHeaderNames.CONTENT_TYPE, "text/csv; charset=utf-8")
            set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED)
            set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=\"simulation_$simulationId.csv\"")
        }
        ctx.writeAndFlush(response)

        FileInputStream(file).use { input ->
            val buffer = ByteArray(CHUNK_SIZE)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                val chunk = DefaultHttpContent(Unpooled.copiedBuffer(buffer, 0, bytesRead))
                ctx.writeAndFlush(chunk)
            }
        }

        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
    }

    private fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus, message: String) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer(message.toByteArray())
        )
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8")
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
        ctx.writeAndFlush(response)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        LOGGER.error("Exception in handler", cause)
        ctx.close()
    }
}
