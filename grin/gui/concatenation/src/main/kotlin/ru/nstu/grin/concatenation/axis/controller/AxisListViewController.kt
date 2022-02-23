package ru.nstu.grin.concatenation.axis.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.model.AxisListViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Controller

class AxisListViewController : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val model: AxisListViewModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.axesListUpdatedEvent.collect {
                model.axises.setAll(it)
            }
        }

        model.axises.setAll(concatenationCanvasModel.getAllAxes())
    }
}