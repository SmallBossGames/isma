package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.intg.api.utilities.BinaryParser
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

class BinaryFilePointProvider(
    private val file: File,
    private val columnNames: List<String>,
) : IntegrationResultPointProvider {

    override val results: Flow<IntgResultPoint>
        get() {
            val fileStream = FileInputStream(file)
            val bufferedStream = BufferedInputStream(fileStream)
            val dis = DataInputStream(bufferedStream)

            val columnCount = dis.readShort().toInt() and 0xFFFF
            repeat(columnCount) {
                val len = dis.readShort().toInt() and 0xFFFF
                dis.skipBytes(len)
            }

            return BinaryParser.parsePoints(bufferedStream, columnNames)
        }

    companion object {
        fun readMetadata(file: File): BinaryMetadataCache {
            val dis = DataInputStream(BufferedInputStream(FileInputStream(file)))
            val columnCount = dis.readShort().toInt() and 0xFFFF

            val columnNames = (0 until columnCount).map {
                val len = dis.readShort().toInt() and 0xFFFF
                val bytes = ByteArray(len)
                dis.readFully(bytes)
                String(bytes, Charsets.UTF_8)
            }

            return BinaryMetadataCache(columnNames)
        }
    }
}

data class BinaryMetadataCache(
    val columnNames: List<String>,
)
