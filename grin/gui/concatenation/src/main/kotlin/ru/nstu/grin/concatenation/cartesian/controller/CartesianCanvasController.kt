package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.events.*
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService
import tornadofx.Controller

class CartesianCanvasController : Controller() {
    private val service: CartesianCanvasService by inject()

    init {
        subscribe<CartesianCopyQuery> {
            service.copyCartesian(it)
        }
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