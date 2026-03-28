package ru.isma.next.external

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.InputStream

class HttpSimulationClient(
    private val socketPath: String,
    private val timeoutSeconds: Long = 30,
) {
    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = timeoutSeconds * 1000
        }
    }

    suspend fun downloadAsInputStream(urlPath: String): InputStream {
        val response = client.get(urlPath) {
            unixSocket(socketPath)
        }
        return response.bodyAsChannel().toInputStream()
    }

    fun close() {
        client.close()
    }
}

class HttpDownloadException(message: String) : RuntimeException(message)
