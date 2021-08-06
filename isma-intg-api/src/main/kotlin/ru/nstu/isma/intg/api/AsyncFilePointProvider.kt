package ru.nstu.isma.intg.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import java.io.File
import java.lang.RuntimeException
import java.util.function.Consumer

class AsyncFilePointProvider(filePath: String) : IntgResultPointProvider {
    override val results = flow {
        File(filePath).bufferedReader().use { reader ->
            val header = reader.readLine()
            val metadata = initFromHeader(header)

            reader.lineSequence().forEach { line ->
                val point = buildIntegrationResultPoint(metadata, line)
                emit(point)
            }
        }
    }.flowOn(Dispatchers.IO)


    private fun initFromHeader(header: String) : ResultFileMetadata {
        val values = header.split(COMMA).toTypedArray()
        if (values.size != 4) {
            throw RuntimeException("Header expected to be size of 4: x count, yForDe count, rhsForDe count, rhsForAe count")
        }
        return ResultFileMetadata(
            xCount = Integer.valueOf(values[0]),
            yForDeCount = Integer.valueOf(values[1]),
            rhsForDeCount = Integer.valueOf(values[2]),
            rhsForAeCount = Integer.valueOf(values[3]),
        )
    }

    private fun buildIntegrationResultPoint(metadata: ResultFileMetadata, pointString: String): IntgResultPoint {
        val values = pointString.split(COMMA).toTypedArray()
        val expectedValueCount =
            metadata.xCount + metadata.yForDeCount + metadata.rhsForDeCount + metadata.rhsForAeCount
        val actualValueCount = values.size
        if (actualValueCount != expectedValueCount) {
            throw RuntimeException("Expected value count per string is $expectedValueCount but was $actualValueCount")
        }
        val x = java.lang.Double.valueOf(values[0])
        val yForDe = DoubleArray(metadata.yForDeCount)
        val rhsForDe = DoubleArray(metadata.rhsForDeCount)
        val rhsForAe = DoubleArray(metadata.rhsForAeCount)
        var valueIndex = 1
        for (i in 0 until metadata.yForDeCount) {
            yForDe[i] = java.lang.Double.valueOf(values[valueIndex])
            valueIndex++
        }
        for (i in 0 until metadata.rhsForDeCount) {
            rhsForDe[i] = java.lang.Double.valueOf(values[valueIndex])
            valueIndex++
        }
        for (i in 0 until metadata.rhsForAeCount) {
            rhsForAe[i] = java.lang.Double.valueOf(values[valueIndex])
            valueIndex++
        }
        val rhs = Array(DaeSystem.RHS_PART_COUNT) { doubleArrayOf() }
        rhs[DaeSystem.RHS_DE_PART_IDX] = rhsForDe
        rhs[DaeSystem.RHS_AE_PART_IDX] = rhsForAe
        return IntgResultPoint(x, yForDe, rhs)
    }

    companion object {
        private const val COMMA = ','
    }
}

data class ResultFileMetadata(val xCount: Int, val yForDeCount: Int, val rhsForDeCount: Int, val rhsForAeCount: Int)