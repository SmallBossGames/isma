package ru.isma.next.external

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
class HttpSimulationClient(
    private val socketPath: String,
    private val timeoutSeconds: Long = 30,
) {
    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = timeoutSeconds * 1000
        }
    }

    suspend fun download(urlPath: String): ByteArray {
        return client.get(urlPath) {
            unixSocket(socketPath)
        }.readRawBytes()
    }

    fun close() {
        client.close()
    }
}

class HttpDownloadException(message: String) : RuntimeException(message)
