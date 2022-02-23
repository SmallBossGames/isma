package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.events.ShowIntersectionsEvent
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionModel
import tornadofx.Controller

class IntersectionFunctionController : Controller() {
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val model: IntersectionFunctionModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.functionsListUpdatedEvent.collect{
                model.functions.setAll(it)
            }
        }

        model.functions.setAll(concatenationCanvasModel.getAllFunctions())
    }

    fun findIntersection(list: List<ConcatenationFunction>) {
        val event = ShowIntersectionsEvent(
            id = list[0].id,
            secondId = list[1].id
        )
        fire(event)
    }
}