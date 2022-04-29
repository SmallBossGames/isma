package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.get
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class IntersectionFunctionController : Controller() {
    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val concatenationCanvasModel: ConcatenationCanvasModel = mainGrinScope.get()
    private val functionCanvasService: FunctionOperationsService = mainGrinScope.get()

    private val fxCoroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: IntersectionFunctionModel by inject()

    init {
        fxCoroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.functions),
                concatenationCanvasModel.functionsListUpdatedEvent
            ).collect{
                model.functions.setAll(it)
            }
        }
    }

    fun findIntersection(list: List<ConcatenationFunction>) {
        coroutineScope.launch {
            functionCanvasService.showInterSections(list[0], list[1])
        }
    }
}