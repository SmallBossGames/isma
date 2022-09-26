package ru.nstu.grin.concatenation.cartesian.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class CartesianListViewModel(
    private val concatenationCanvasModel: ConcatenationCanvasModel
) {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)

    val cartesianSpaces = FXCollections.observableArrayList<CartesianSpace>()!!

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.cartesianSpaces),
                concatenationCanvasModel.cartesianSpacesListUpdatedEvent
            ).collect {
                cartesianSpaces.setAll(it)
            }
        }
    }
}