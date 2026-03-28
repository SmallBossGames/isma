package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.intg.api.utilities.BinaryParser
import java.io.InputStream

class BinaryInputStreamPointProvider(
    private val inputStream: InputStream,
    private val columnNames: List<String>,
) : IntegrationResultPointProvider {

    override val results: Flow<IntgResultPoint>
        get() = BinaryParser.parsePoints(inputStream, columnNames)
}
