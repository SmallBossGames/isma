package ru.nstu.isma.intg.api

import kotlinx.coroutines.flow.asFlow
import java.util.*
import java.util.function.Consumer

class IntgResultMemoryStore : IntgResultPointProvider {
    private val resultsInternal = LinkedList<IntgResultPoint>()

    override val results get() = resultsInternal.asFlow()

    fun accept(intgResultPoint: IntgResultPoint) {
        resultsInternal.add(intgResultPoint)
    }

    fun read(handler: Consumer<List<IntgResultPoint>>) {
        handler.accept(resultsInternal)
    }
}