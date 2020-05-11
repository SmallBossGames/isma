package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.service.CartesianCanvasService
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