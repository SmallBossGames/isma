package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.function.events.LocalizeFunctionEvent
import ru.nstu.grin.concatenation.function.model.LocalizeFunctionModel
import tornadofx.Controller
import java.util.*

class LocalizeFunctionController : Controller() {
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val model: LocalizeFunctionModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.functionsListUpdatedEvent.collect{
                model.functions.setAll(it)
            }
        }

        model.functions.setAll(concatenationCanvasModel.getAllFunctions())
    }

    fun localize(id: UUID) {
        val event = LocalizeFunctionEvent(
            id = id
        )
        fire(event)
    }

    fun getAllFunctions() {
        fire(GetAllFunctionsQuery())
    }
}