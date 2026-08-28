package ru.isma.next.external

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.isma.next.domain.models.SimulationPoint
import ru.isma.next.domain.models.SimulationResultReader
import ru.isma.next.exchange.format.readAllPointsSequence
import java.io.File

class BinaryFilePointProvider(
    private val file: File,
    private val columnNames: List<String>,
) : SimulationResultReader {

    override val results: Flow<SimulationPoint>
        get() = flow {
            val pointsSequence = readAllPointsSequence(file)

            val deNames = columnNames.filter { it.startsWith("DE_") }.sortedBy { it }
            val aeNames = columnNames.filter { it.startsWith("AE_") }.sortedBy { it }

            val deCount = deNames.size
            val aeCount = aeNames.size

            for (row in pointsSequence) {
                val x = row[0]
                val yForDe = DoubleArray(deCount + aeCount)
                for (i in yForDe.indices) {
                    yForDe[i] = row[1 + i]
                }

                val rhsDe = DoubleArray(deCount)
                for (i in rhsDe.indices) {
                    rhsDe[i] = row[1 + deCount + aeCount + i]
                }

                val rhsAe = DoubleArray(aeCount)
                for (i in rhsAe.indices) {
                    rhsAe[i] = row[1 + deCount + i]
                }

                emit(SimulationPoint(x, yForDe, arrayOf(rhsDe, rhsAe)))
            }
        }.flowOn(Dispatchers.IO)

    companion object {
        fun readMetadata(file: File): BinaryMetadataCache {
            val metadata = ru.isma.next.exchange.format.readMetadata(file)
            return BinaryMetadataCache(metadata.columnNames)
        }
    }
}

typealias BinaryMetadataCache = ru.isma.next.exchange.format.BinaryMetadata
