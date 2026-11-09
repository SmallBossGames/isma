package ru.nstu.isma.lsp.protocol

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.nstu.isma.lsp.lsp.DecodedSemanticToken
import ru.nstu.isma.lsp.lsp.LismaLanguageService
import ru.nstu.isma.lsp.lsp.SemanticTokensDecoder
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/** One-way in-memory pipe: writes framed LSP messages, reads as a blocking stream. */
private class InMemoryPipe {
    private val buffer = ByteArrayOutputStream()
    private val lock = Object()
    @Volatile
    private var closed = false

    fun write(element: JsonElement) {
        val body = element.toString().toByteArray(Charsets.UTF_8)
        synchronized(lock) {
            buffer.write("Content-Length: ${body.size}\r\n\r\n".toByteArray(Charsets.UTF_8))
            buffer.write(body)
            lock.notifyAll()
        }
    }

    fun close() {
        synchronized(lock) {
            closed = true
            lock.notifyAll()
        }
    }

    fun inputStream(): InputStream = object : InputStream() {
        override fun read(): Int {
            while (true) {
                synchronized(lock) {
                    val bytes = buffer.toByteArray()
                    if (bytes.isNotEmpty()) {
                        val b = bytes[0].toInt() and 0xFF
                        buffer.reset()
                        buffer.write(bytes, 1, bytes.size - 1)
                        return b
                    }
                    if (closed) return -1
                    lock.wait(50)
                }
            }
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            val n = read()
            if (n < 0) return -1
            b[off] = n.toByte()
            return 1
        }
    }
}

/** Collects raw server output; frames are split lazily from the byte stream. */
private class ByteSink : OutputStream() {
    private val buffer = ByteArrayOutputStream()
    val lock = Object()

    override fun write(b: Int) {
        synchronized(lock) {
            buffer.write(b)
            lock.notifyAll()
        }
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        synchronized(lock) {
            buffer.write(b, off, len)
            lock.notifyAll()
        }
    }

    fun awaitFrame(timeoutMs: Long = 10_000): JsonElement {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (true) {
            synchronized(lock) {
                val bytes = buffer.toByteArray()
                val headerEnd = String(bytes, Charsets.UTF_8).indexOf("\r\n\r\n")
                if (headerEnd >= 0) {
                    val header = String(bytes, 0, headerEnd, Charsets.UTF_8)
                    val length = header
                        .lineSequence()
                        .first { it.startsWith("Content-Length:", ignoreCase = true) }
                        .substringAfter(':')
                        .trim()
                        .toInt()
                    val bodyStart = headerEnd + 4
                    if (bytes.size >= bodyStart + length) {
                        val body = bytes.copyOfRange(bodyStart, bodyStart + length)
                        buffer.reset()
                        buffer.write(bytes, bodyStart + length, bytes.size - bodyStart - length)
                        return LspMessages.parse(String(body, Charsets.UTF_8))
                    }
                }
            }
            val remaining = deadline - System.currentTimeMillis()
            if (remaining <= 0) throw AssertionError("No LSP response within ${timeoutMs}ms")
            Thread.sleep(minOf(10, remaining))
        }
    }
}

private class Fixture {
    private val clientToServer = InMemoryPipe()
    val serverOutput = ByteSink()

    val serverThread = Thread {
        LspServer(clientToServer.inputStream(), serverOutput, LismaLanguageService()).run()
    }.apply { isDaemon = true; start() }

    fun send(method: String, id: Int? = null, params: JsonObject = JsonObject(emptyMap())) {
        clientToServer.write(
            buildJsonObject {
                put("jsonrpc", "2.0")
                if (id != null) put("id", id)
                put("method", method)
                put("params", params)
            },
        )
    }

    fun openDocument(uri: String, text: String, version: Int = 1) {
        send(
            "textDocument/didOpen",
            params = buildJsonObject {
                putJsonObject("textDocument") {
                    put("uri", uri)
                    put("languageId", "lisma")
                    put("version", version)
                    put("text", text)
                }
            },
        )
    }

    fun changeDocument(uri: String, text: String, version: Int) {
        send(
            "textDocument/didChange",
            params = buildJsonObject {
                putJsonObject("textDocument") {
                    put("uri", uri)
                    put("version", version)
                }
                putJsonArray("contentChanges") {
                    add(buildJsonObject { put("text", text) })
                }
            },
        )
    }

    fun requestSemanticTokens(uri: String, id: Int): JsonElement {
        send(
            "textDocument/semanticTokens/full",
            id = id,
            params = buildJsonObject {
                putJsonObject("textDocument") {
                    put("uri", uri)
                }
            },
        )
        return serverOutput.awaitFrame()
    }

    fun close() {
        clientToServer.close()
    }
}

class LspServerTest {

    private fun decode(result: JsonElement): List<DecodedSemanticToken> =
        SemanticTokensDecoder.decode(result.jsonObject["data"]!!.jsonArray.map { it.jsonPrimitive.content.toLong() })

    @Test
    fun `initialize advertises sync and semantic token capabilities`() {
        val fx = Fixture()
        fx.send("initialize", id = 1)

        val response = fx.serverOutput.awaitFrame()
        val capabilities = response.jsonObject["result"]!!.jsonObject["capabilities"]!!.jsonObject

        assertEquals("1", capabilities["textDocumentSync"]?.jsonPrimitive?.content)
        val provider = capabilities["semanticTokensProvider"]!!.jsonObject
        val legend = provider["legend"]!!.jsonObject
        assertEquals(
            listOf("keyword", "comment", "number"),
            legend["tokenTypes"]!!.jsonArray.map { it.jsonPrimitive.content },
        )
        assertTrue(provider["full"]!!.jsonObject.isEmpty())
        assertEquals("lisma-lsp", response.jsonObject["result"]!!.jsonObject["serverInfo"]!!.jsonObject["name"]?.jsonPrimitive?.content)
        fx.close()
    }

    @Test
    fun `semantic tokens full round trip over the wire`() {
        val fx = Fixture()
        fx.send("initialize", id = 1)
        fx.serverOutput.awaitFrame()
        fx.send("initialized")

        fx.openDocument("file:///test.lisma", "const a = 1.5;\n// note")
        val response = fx.requestSemanticTokens("file:///test.lisma", id = 2)

        assertEquals(
            listOf(
                DecodedSemanticToken(0, 0, 5, 0), // const -> keyword
                DecodedSemanticToken(0, 10, 3, 2), // 1.5 -> number
                DecodedSemanticToken(1, 0, 7, 1), // // note -> comment
            ),
            decode(response.jsonObject["result"]!!),
        )
        fx.close()
    }

    @Test
    fun `didChange replaces document content`() {
        val fx = Fixture()
        fx.send("initialize", id = 1)
        fx.serverOutput.awaitFrame()

        fx.openDocument("file:///test.lisma", "const a = 1;")
        fx.changeDocument("file:///test.lisma", "state Main (x) { x' = -x; }", version = 2)
        val response = fx.requestSemanticTokens("file:///test.lisma", id = 2)

        val decoded = decode(response.jsonObject["result"]!!)
        assertEquals(1, decoded.size)
        assertEquals(DecodedSemanticToken(0, 0, 5, 0), decoded[0]) // state -> keyword
        fx.close()
    }

    @Test
    fun `didClose removes document`() {
        val fx = Fixture()
        fx.send("initialize", id = 1)
        fx.serverOutput.awaitFrame()

        fx.openDocument("file:///gone.lisma", "const a = 1;")
        fx.send(
            "textDocument/didClose",
            params = buildJsonObject {
                putJsonObject("textDocument") {
                    put("uri", "file:///gone.lisma")
                }
            },
        )
        val response = fx.requestSemanticTokens("file:///gone.lisma", id = 2)

        assertTrue(decode(response.jsonObject["result"]!!).isEmpty())
        fx.close()
    }

    @Test
    fun `unknown request gets method not found error`() {
        val fx = Fixture()
        fx.send("initialize", id = 1)
        fx.serverOutput.awaitFrame()

        fx.send("workspace/unknownThing", id = 7)

        val response = fx.serverOutput.awaitFrame()
        val error = response.jsonObject["error"]!!.jsonObject
        assertEquals("-32601", error["code"]?.jsonPrimitive?.content)
        fx.close()
    }

    @Test
    fun `shutdown responds with null`() {
        val fx = Fixture()
        fx.send("initialize", id = 1)
        fx.serverOutput.awaitFrame()

        fx.send("shutdown", id = 2)

        val response = fx.serverOutput.awaitFrame()
        assertEquals("null", response.jsonObject["result"]?.toString())
        fx.close()
    }
}
