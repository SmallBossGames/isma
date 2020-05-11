package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.events.DeleteCartesianSpaceQuery
import ru.nstu.grin.concatenation.cartesian.events.GetAllCartesiansEvent
import ru.nstu.grin.concatenation.canvas.model.CartesianListViewModel
import tornadofx.Controller
import tornadofx.toObservable
import java.util.*

class CartesianListViewController : Controller() {
    private val model: CartesianListViewModel by inject()

    init {
        subscribe<GetAllCartesiansEvent> {
            println("Get all cartesians")
            model.cartesianSpaces = it.cartesianSpaces.toObservable()
        }
    }

    fun deleteCartesian(cartesianId: UUID) {
        val event =
            DeleteCartesianSpaceQuery(cartesianId)
        fire(event)
    }
}