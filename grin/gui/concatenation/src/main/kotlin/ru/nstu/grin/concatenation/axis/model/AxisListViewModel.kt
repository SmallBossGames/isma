package ru.nstu.grin.concatenation.axis.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Scope
import tornadofx.ViewModel

class AxisListViewModel(
    override val scope: Scope
): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()

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