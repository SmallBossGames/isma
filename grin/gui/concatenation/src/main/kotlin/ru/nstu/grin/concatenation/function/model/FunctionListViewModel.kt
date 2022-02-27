package ru.nstu.grin.concatenation.function.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class FunctionListViewModel(
    private val concatenationCanvasModel: ConcatenationCanvasModel
) {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)

    val functions = FXCollections.observableArrayList<ConcatenationFunction>()!!

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.getAllFunctions()),
                concatenationCanvasModel.functionsListUpdatedEvent
            ).collect {
                functions.setAll(it)
            }
        }
    }
}