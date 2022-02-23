package ru.nstu.grin.concatenation.cartesian.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.cartesian.events.DeleteCartesianSpaceQuery
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import tornadofx.Controller
import java.util.*

class CartesianListViewController : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val model: CartesianListViewModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.cartesianSpacesListUpdatedEvent.collect{
                model.cartesianSpaces.setAll(it)
            }
        }

        model.cartesianSpaces.setAll(concatenationCanvasModel.getAllCartesianSpaces())
    }

    fun deleteCartesian(cartesianId: UUID) {
        val event =
            DeleteCartesianSpaceQuery(cartesianId)
        fire(event)
    }
}