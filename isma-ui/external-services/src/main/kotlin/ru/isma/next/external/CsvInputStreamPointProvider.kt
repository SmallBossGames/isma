package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.intg.api.utilities.CsvMetadata
import ru.nstu.isma.intg.api.utilities.CsvParser
import java.io.InputStream

class CsvInputStreamPointProvider(
    private val inputStream: InputStream,
    private val metadata: CsvMetadata,
) : IntegrationResultPointProvider {

    override val results: Flow<IntgResultPoint>
        get() = CsvParser.parsePoints(inputStream, metadata)
}
