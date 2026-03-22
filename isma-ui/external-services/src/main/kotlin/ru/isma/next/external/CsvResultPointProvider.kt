package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import java.io.ByteArrayInputStream
import java.io.InputStream

private const val COMMA = ","

class CsvIntegrationResultPointProvider(data: ByteArray) : IntegrationResultPointProvider {
    private val points: List<IntgResultPoint>

    init {
        val lines = ByteArrayInputStream(data).bufferedReader().readLines()
        require(lines.size >= 3) { "CSV data must have at least 3 lines (header + names + data)" }

        val headerValues = lines[0].split(COMMA).map { it.trim().toInt() }
        val yForDeCount = headerValues[1]
        val rhsForDeCount = headerValues[2]
        val rhsForAeCount = headerValues[3]

        points = lines.drop(2).map { line ->
            val values = line.split(COMMA).map { it.trim().toDouble() }
            val x = values[0]
            val yForDe = values.subList(1, 1 + yForDeCount).toDoubleArray()
            val rhsForDe = values.subList(1 + yForDeCount, 1 + yForDeCount + rhsForDeCount).toDoubleArray()
            val rhsForAe = values.subList(1 + yForDeCount + rhsForDeCount, values.size).toDoubleArray()
            val rhs = arrayOf(rhsForDe, rhsForAe)
            IntgResultPoint(x, yForDe, rhs)
        }
    }

    override val results: Flow<IntgResultPoint> = flow {
        points.forEach { emit(it) }
    }
}

class CsvMetadata(data: ByteArray) {
    val xCount: Int
    val yForDeCount: Int
    val rhsForDeCount: Int
    val rhsForAeCount: Int
    val variableNames: List<String>

    init {
        val lines = ByteArrayInputStream(data).bufferedReader().readLines()
        require(lines.size >= 2) { "CSV data must have at least 2 lines (header + names)" }

        val headerValues = lines[0].split(COMMA).map { it.trim().toInt() }
        require(headerValues.size == 4) { "Expected 4 values in CSV header, got ${headerValues.size}" }
        xCount = headerValues[0]
        yForDeCount = headerValues[1]
        rhsForDeCount = headerValues[2]
        rhsForAeCount = headerValues[3]

        variableNames = lines[1].split(COMMA).map { it.trim() }
    }

    val totalColumnCount: Int get() = xCount + yForDeCount + rhsForDeCount + rhsForAeCount
}
