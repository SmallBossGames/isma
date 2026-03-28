package ru.nstu.isma.intg.api.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.nstu.isma.intg.api.models.IntgResultPoint
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader

private const val COMMA = ","

data class CsvMetadata(
    val xCount: Int,
    val yForDeCount: Int,
    val rhsForDeCount: Int,
    val rhsForAeCount: Int,
    val variableNames: List<String>,
) {
    val totalColumnCount: Int get() = xCount + yForDeCount + rhsForDeCount + rhsForAeCount
}

data class CsvParseResult(
    val metadata: CsvMetadata,
    val dataInputStream: InputStream,
)

object CsvParser {
    fun parseAll(inputStream: InputStream): CsvParseResult {
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
        
        val headerLine = reader.readLine() 
            ?: throw IllegalStateException("CSV header is missing")
        val namesLine = reader.readLine() 
            ?: throw IllegalStateException("CSV variable names are missing")

        val headerValues = headerLine.split(COMMA).map { it.trim() }
        require(headerValues.size == 4) { "Expected 4 values in CSV header, got ${headerValues.size}" }

        val xCount = headerValues[0].toInt()
        val yForDeCount = headerValues[1].toInt()
        val rhsForDeCount = headerValues[2].toInt()
        val rhsForAeCount = headerValues[3].toInt()

        val variableNames = namesLine.split(COMMA).map { it.trim() }

        val metadata = CsvMetadata(
            xCount = xCount,
            yForDeCount = yForDeCount,
            rhsForDeCount = rhsForDeCount,
            rhsForAeCount = rhsForAeCount,
            variableNames = variableNames,
        )

        val buffer = ByteArrayOutputStream()
        var line: String? = reader.readLine()
        while (line != null) {
            buffer.write(line.toByteArray(Charsets.UTF_8))
            buffer.write('\n'.code)
            line = reader.readLine()
        }

        return CsvParseResult(metadata, ByteArrayInputStream(buffer.toByteArray()))
    }

    fun parsePoints(inputStream: InputStream, metadata: CsvMetadata): Flow<IntgResultPoint> = flow {
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

        var line: String? = reader.readLine()
        while (line != null) {
            val point = buildIntegrationResultPoint(metadata, line)
            emit(point)
            line = reader.readLine()
        }
    }.flowOn(Dispatchers.IO)

    private fun buildIntegrationResultPoint(metadata: CsvMetadata, line: String): IntgResultPoint {
        val values = line.split(COMMA).map { it.trim().toDouble() }
        val expectedCount = metadata.totalColumnCount
        require(values.size == expectedCount) { 
            "Expected $expectedCount values, got ${values.size}" 
        }

        val x = values[0]
        val yForDe = values.subList(1, 1 + metadata.yForDeCount).toDoubleArray()
        val rhsForDe = values.subList(1 + metadata.yForDeCount, 1 + metadata.yForDeCount + metadata.rhsForDeCount).toDoubleArray()
        val rhsForAe = values.subList(1 + metadata.yForDeCount + metadata.rhsForDeCount, values.size).toDoubleArray()
        val rhs = arrayOf(rhsForDe, rhsForAe)

        return IntgResultPoint(x, yForDe, rhs)
    }
}
