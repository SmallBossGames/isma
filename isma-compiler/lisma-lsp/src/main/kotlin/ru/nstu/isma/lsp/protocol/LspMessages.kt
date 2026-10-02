package ru.nstu.isma.lsp.protocol

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.io.InputStream
import java.io.OutputStream

/**
 * LSP base protocol framing: a UTF-8 JSON body preceded by header lines
 * terminated by an empty line, with the body length given by
 * `Content-Length` (bytes).
 */
object LspMessages {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(text: String): JsonElement = json.parseToJsonElement(text)

    /** Reads one framed message; returns null on clean EOF. */
    fun read(input: InputStream): JsonElement? {
        val headers = readHeaders(input) ?: return null
        val contentLength = headers
            .lineSequence()
            .firstOrNull { it.startsWith("Content-Length:", ignoreCase = true) }
            ?.substringAfter(':')
            ?.trim()
            ?.toIntOrNull()
            ?: return null

        val body = ByteArray(contentLength)
        var offset = 0
        while (offset < contentLength) {
            val n = input.read(body, offset, contentLength - offset)
            if (n < 0) return null
            offset += n
        }
        return parse(String(body, Charsets.UTF_8))
    }

    fun write(output: OutputStream, element: JsonElement) {
        val body = element.toString().toByteArray(Charsets.UTF_8)
        output.write("Content-Length: ${body.size}\r\n\r\n".toByteArray(Charsets.UTF_8))
        output.write(body)
        output.flush()
    }

    private fun readHeaders(input: InputStream): String? {
        val header = StringBuilder()
        while (true) {
            val b = input.read()
            if (b < 0) return null
            header.append(b.toChar())
            if (header.endsWith("\r\n\r\n")) return header.toString()
        }
    }
}
