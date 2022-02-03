package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.events.DeleteCartesianSpaceQuery
import ru.nstu.grin.concatenation.cartesian.events.GetAllCartesiansEvent
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import tornadofx.Controller
import java.util.*

class CartesianListViewController : Controller() {
    private val model: CartesianListViewModel by inject()

    init {
        subscribe<GetAllCartesiansEvent> {
            model.replaceCartesianSpaces(it.cartesianSpaces)
        }
    }

    fun deleteCartesian(cartesianId: UUID) {
        val event =
            DeleteCartesianSpaceQuery(cartesianId)
        fire(event)
    }
}