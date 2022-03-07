package ru.nstu.isma.intg.api.providers

import kotlinx.coroutines.flow.asFlow
import ru.nstu.isma.intg.api.models.IntgResultPoint
import java.util.*


class MemoryPointProvider : IntegrationResultPointProvider {
    private val resultsInternal = LinkedList<IntgResultPoint>()

    override val results get() = resultsInternal.asFlow()

    fun accept(intgResultPoint: IntgResultPoint) {
        resultsInternal.add(intgResultPoint)
    }
}