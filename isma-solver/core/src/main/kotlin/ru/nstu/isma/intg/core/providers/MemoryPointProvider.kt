package ru.nstu.isma.intg.core.providers

import kotlinx.coroutines.flow.asFlow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import java.util.*


class MemoryPointProvider : IntegrationResultPointProvider {
    private val resultsInternal = LinkedList<IntgResultPoint>()

    override val results get() = resultsInternal.asFlow()

    fun accept(intgResultPoint: IntgResultPoint) {
        resultsInternal.add(intgResultPoint)
    }
}