package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LocalizeFunctionData
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService
import ru.nstu.grin.concatenation.koin.MainGrinScope

class LocalizeFunctionController(
    private val model: LocalizeFunctionData,
) : KoinComponent {
    private val mainGrinScope: MainGrinScope = get()
    private val koinScope = mainGrinScope.scope
    private val concatenationCanvasModel: ConcatenationCanvasModel = koinScope.get()
    private val functionCanvasService: FunctionOperationsService = koinScope.get()

    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)

    init {
        coroutineScope.launch {
            concatenationCanvasModel.functionsListUpdatedEvent.collect {
                model.functions.setAll(it)
            }
        }

        model.functions.setAll(concatenationCanvasModel.functions)
    }

    fun localize(function: ConcatenationFunction) {
        functionCanvasService.localizeFunction(function)
    }
}