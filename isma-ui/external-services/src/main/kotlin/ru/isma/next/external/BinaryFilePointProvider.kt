package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.intg.api.utilities.BinaryParser
import ru.isma.next.exchange.format.readMetadata
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
            val metadata = ru.isma.next.exchange.format.readMetadata(file)
            return BinaryMetadataCache(metadata.columnNames)
        }
    }
}

typealias BinaryMetadataCache = ru.isma.next.exchange.format.BinaryMetadata
