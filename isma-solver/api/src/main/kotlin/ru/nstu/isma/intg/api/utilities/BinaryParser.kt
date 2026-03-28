package ru.nstu.isma.intg.api.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.nstu.isma.intg.api.models.IntgResultPoint
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.InputStream

data class BinaryMetadata(
    val columnNames: List<String>,
)

data class BinaryParseResult(
    val metadata: BinaryMetadata,
    val dataInputStream: InputStream,
)

object BinaryParser {
    fun parseAll(inputStream: InputStream): BinaryParseResult {
        val bufferedStream = BufferedInputStream(inputStream)
        val dis = DataInputStream(bufferedStream)
        val columnCount = dis.readShort().toInt() and 0xFFFF

        val columnNames = (0 until columnCount).map {
            val len = dis.readShort().toInt() and 0xFFFF
            val bytes = ByteArray(len)
            dis.readFully(bytes)
            String(bytes, Charsets.UTF_8)
        }

        val buffer = ByteArrayOutputStream()
        bufferedStream.use { it.copyTo(buffer) }

        return BinaryParseResult(BinaryMetadata(columnNames), ByteArrayInputStream(buffer.toByteArray()))
    }

    fun parsePoints(inputStream: InputStream, columnNames: List<String>): Flow<IntgResultPoint> = flow {
        val dis = DataInputStream(BufferedInputStream(inputStream))

        val deNames = columnNames.filter { it.startsWith("DE_") }.sortedBy { it }
        val aeNames = columnNames.filter { it.startsWith("AE_") }.sortedBy { it }

        val deCount = deNames.size
        val aeCount = aeNames.size

        try {
            while (true) {
                val x = dis.readDouble()
                val yForDe = DoubleArray(deCount + aeCount)
                for (i in yForDe.indices) {
                    yForDe[i] = dis.readDouble()
                }

                val rhsDe = DoubleArray(deCount)
                for (i in rhsDe.indices) {
                    rhsDe[i] = dis.readDouble()
                }

                val rhsAe = DoubleArray(aeCount)
                for (i in rhsAe.indices) {
                    rhsAe[i] = dis.readDouble()
                }

                emit(IntgResultPoint(x, yForDe, arrayOf(rhsDe, rhsAe)))
            }
        } catch (_: EOFException) {
        }
    }.flowOn(Dispatchers.IO)
}
