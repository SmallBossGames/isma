package ru.nstu.grin.concatenation.axis.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class AxisListViewModel(
    private val concatenationCanvasModel: ConcatenationCanvasModel
) {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)

    val axes = FXCollections.observableArrayList<ConcatenationAxis>()!!

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.getAllAxes()),
                concatenationCanvasModel.axesListUpdatedEvent
            ).collect {
                axes.setAll(it)
            }
        }
    }
}