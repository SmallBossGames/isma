package ru.nstu.isma.intg.api.utilities

import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.models.ResultPointsFileMetadata

private const val COMMA = ","

object IntegrationResultPointFileHelpers {
    fun buildCsvString(resultPoint: IntgResultPoint): String {
        val builder = StringBuilder()
        builder.append(resultPoint.x)
        for (y in resultPoint.yForDe) {
            builder.append(COMMA).append(y)
        }
        val yForAeArray = resultPoint.rhs[DaeSystem.RHS_AE_PART_IDX]
        for (yForAe in yForAeArray) {
            builder.append(COMMA).append(yForAe)
        }
        val fArray = resultPoint.rhs[DaeSystem.RHS_DE_PART_IDX]
        for (f in fArray) {
            builder.append(COMMA).append(f)
        }
        builder.append(System.lineSeparator())
        return builder.toString()
    }

    fun buildCsvHeader(firstResultPoint: IntgResultPoint): String {
        val builder = StringBuilder()
        builder.append(1) // x count
        builder.append(COMMA).append(firstResultPoint.yForDe.size) // y count;
        builder.append(COMMA).append(firstResultPoint.rhs[DaeSystem.RHS_DE_PART_IDX].size) // rhsForDeCount;
        builder.append(COMMA).append(firstResultPoint.rhs[DaeSystem.RHS_AE_PART_IDX].size) // rhsForAeCount;
        builder.append(System.lineSeparator())
        return builder.toString()
    }

    fun buildIntegrationResultPoint(metadata: ResultPointsFileMetadata, pointString: String): IntgResultPoint {
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

    fun buildMetdataFromHeader(header: String) : ResultPointsFileMetadata {
        val values = header.split(COMMA).toTypedArray()
        if (values.size != 4) {
            throw RuntimeException("Header expected to be size of 4: x count, yForDe count, rhsForDe count, rhsForAe count")
        }
        return ResultPointsFileMetadata(
            xCount = Integer.valueOf(values[0]),
            yForDeCount = Integer.valueOf(values[1]),
            rhsForDeCount = Integer.valueOf(values[2]),
            rhsForAeCount = Integer.valueOf(values[3]),
        )
    }
}