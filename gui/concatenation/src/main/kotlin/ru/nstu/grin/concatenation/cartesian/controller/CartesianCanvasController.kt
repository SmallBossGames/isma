package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService
import ru.nstu.grin.concatenation.cartesian.events.CartesianQuery
import ru.nstu.grin.concatenation.cartesian.events.DeleteCartesianSpaceQuery
import ru.nstu.grin.concatenation.cartesian.events.GetAllCartesiansQuery
import ru.nstu.grin.concatenation.cartesian.events.UpdateCartesianEvent
import tornadofx.Controller

class CartesianCanvasController : Controller() {
    private val service: CartesianCanvasService by inject()

    init {
        subscribe<UpdateCartesianEvent> {
            service.updateCartesian(it)
        }
        subscribe<CartesianQuery> { event ->
            service.getCartesian(event)
        }
        subscribe<GetAllCartesiansQuery> {
            service.getAllCartesianSpaces()
        }
        subscribe<DeleteCartesianSpaceQuery> {
            service.deleteCartesianSpace(it)
        }
    }
}