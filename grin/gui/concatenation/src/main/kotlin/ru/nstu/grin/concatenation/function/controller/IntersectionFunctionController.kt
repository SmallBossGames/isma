package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.get
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class IntersectionFunctionController : Controller() {
    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val concatenationCanvasModel: ConcatenationCanvasModel = mainGrinScope.get()
    private val functionCanvasService: FunctionCanvasService = mainGrinScope.get()

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
        functionCanvasService.showInterSections(list[0], list[1])
    }
}