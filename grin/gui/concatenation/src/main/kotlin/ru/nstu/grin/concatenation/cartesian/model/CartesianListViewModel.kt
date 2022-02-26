package ru.nstu.grin.concatenation.cartesian.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.ViewModel

class CartesianListViewModel : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()

    val cartesianSpaces = FXCollections.observableArrayList<CartesianSpace>()!!

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.getAllCartesianSpaces()),
                concatenationCanvasModel.cartesianSpacesListUpdatedEvent
            ).collect {
                cartesianSpaces.setAll(it)
            }
        }
    }
}