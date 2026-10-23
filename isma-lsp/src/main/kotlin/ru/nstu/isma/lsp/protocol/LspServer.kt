package ru.nstu.isma.lsp.protocol

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import ru.nstu.isma.lsp.lsp.LismaLanguageService
import ru.nstu.isma.lsp.lsp.LSP_TOKEN_LEGEND
import ru.nstu.isma.lsp.lsp.SemanticTokensEncoder
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.exitProcess

/**
 * Minimal LSP server: lifecycle, full document sync, pull-model semantic
 * tokens.
 *
 * Extension model: new request features are added as [handle] branches backed
 * by [LismaLanguageService] capabilities; server-to-client push features
 * (e.g. publishDiagnostics) go through [sendNotification].
 */
class LspServer(
    private val input: InputStream,
    private val output: OutputStream,
    private val languageService: LismaLanguageService,
) {
    private val documents = ConcurrentHashMap<String, String>()
    @Volatile
    private var shutdownRequested = false

    /** Blocking dispatch loop; returns when the input stream closes. */
    fun run() {
        while (true) {
            val message = LspMessages.read(input) ?: break
            handle(message.jsonObject)
        }
    }

    private fun handle(message: JsonObject) {
        val method = message["method"]?.jsonPrimitive?.content ?: return
        val id = message["id"]
        val params = message["params"]?.jsonObject ?: JsonObject(emptyMap())

        when (method) {
            "initialize" -> respond(id, buildInitializeResult())
            "initialized" -> Unit
            "shutdown" -> {
                shutdownRequested = true
                respond(id, JsonNull)
            }
            "exit" -> exitProcess(if (shutdownRequested) EXIT_NORMAL else EXIT_AFTER_FAILURE)

            "textDocument/didOpen" -> {
                val document = params["textDocument"]?.jsonObject ?: return
                val uri = document.uri ?: return
                documents[uri] = document["text"]?.jsonPrimitive?.content.orEmpty()
            }
            "textDocument/didChange" -> {
                val document = params["textDocument"]?.jsonObject ?: return
                val uri = document.uri ?: return
                val change = params["contentChanges"]?.jsonArray?.lastOrNull()?.jsonObject ?: return
                documents[uri] = change["text"]?.jsonPrimitive?.content.orEmpty()
            }
            "textDocument/didClose" -> {
                val document = params["textDocument"]?.jsonObject ?: return
                documents.remove(document.uri ?: return)
            }

            "textDocument/semanticTokens/full" -> {
                val uri = params["textDocument"]?.jsonObject?.uri ?: return
                val data = SemanticTokensEncoder.encode(languageService.semanticTokens(documents[uri].orEmpty()))
                respond(
                    id,
                    buildJsonObject {
                        putJsonArray("data") {
                            data.forEach { add(it) }
                        }
                    },
                )
            }

            else -> {
                if (id != null) {
                    respondError(id, METHOD_NOT_FOUND, "Method not found: $method")
                }
            }
        }
    }

    private fun buildInitializeResult(): JsonObject = buildJsonObject {
        putJsonObject("capabilities") {
            put("textDocumentSync", TEXT_DOCUMENT_SYNC_FULL)
            putJsonObject("semanticTokensProvider") {
                putJsonObject("legend") {
                    putJsonArray("tokenTypes") {
                        LSP_TOKEN_LEGEND.forEach { add(it) }
                    }
                    put("tokenModifiers", JsonArray(emptyList()))
                }
                put("full", JsonObject(emptyMap()))
            }
        }
        putJsonObject("serverInfo") {
            put("name", SERVER_NAME)
            put("version", SERVER_VERSION)
        }
    }

    private fun respond(id: JsonElement?, result: JsonElement) {
        if (id == null) return
        LspMessages.write(
            output,
            buildJsonObject {
                put("jsonrpc", "2.0")
                put("id", id)
                put("result", result)
            },
        )
    }

    private fun respondError(id: JsonElement?, code: Int, message: String) {
        if (id == null) return
        LspMessages.write(
            output,
            buildJsonObject {
                put("jsonrpc", "2.0")
                put("id", id)
                putJsonObject("error") {
                    put("code", code)
                    put("message", message)
                }
            },
        )
    }

    /** Server-to-client notification hook (future push features). */
    fun sendNotification(method: String, params: JsonObject) {
        LspMessages.write(
            output,
            buildJsonObject {
                put("jsonrpc", "2.0")
                put("method", method)
                put("params", params)
            },
        )
    }

    private val JsonObject.uri: String?
        get() = this["uri"]?.jsonPrimitive?.content

    private companion object {
        const val EXIT_NORMAL = 0
        const val EXIT_AFTER_FAILURE = 1
        const val METHOD_NOT_FOUND = -32601
        const val TEXT_DOCUMENT_SYNC_FULL = 1
        const val SERVER_NAME = "lisma-lsp"
        const val SERVER_VERSION = "1.0.0"
    }
}
