package ru.nstu.isma.intg.api.providers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.nstu.isma.intg.api.utilities.IntegrationResultPointFileHelpers.buildIntegrationResultPoint
import ru.nstu.isma.intg.api.utilities.IntegrationResultPointFileHelpers.buildMetdataFromHeader
import java.io.File

class AsyncFilePointProvider(filePath: String) : IntegrationResultPointProvider {
    override val results = flow {
        File(filePath).bufferedReader().use { reader ->
            val header = reader.readLine()
            val metadata = buildMetdataFromHeader(header)

            reader.lineSequence().forEach { line ->
                val point = buildIntegrationResultPoint(metadata, line)
                emit(point)
            }
        }
    }.flowOn(Dispatchers.IO)
}

