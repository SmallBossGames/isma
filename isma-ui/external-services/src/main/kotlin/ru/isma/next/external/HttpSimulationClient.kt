package ru.isma.next.external

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.*
import java.io.File

class HttpSimulationClient(
    private val socketPath: String,
    private val timeoutSeconds: Long = 30,
) {
    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = timeoutSeconds * 1000
        }
    }

    suspend fun downloadToFile(urlPath: String, targetFile: File) {
        val input = client.get(urlPath) {
            unixSocket(socketPath)
        }.bodyAsChannel().toInputStream()

        input.use { inputStream ->
            targetFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    fun close() {
        client.close()
    }
}
