package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import tornadofx.Controller

class CartesianCanvasController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    init {
        subscribe<UpdateCartesianEvent> {
            updateCartesian(it)
            getAllCartesianSpaces()
        }
        subscribe<CartesianQuery> { event ->
            val cartesianSpace = model.cartesianSpaces.first { it.id == event.id }
            fire(GetCartesianEvent(cartesianSpace))
        }
        subscribe<GetAllCartesiansQuery> {
            getAllCartesianSpaces()
        }
        subscribe<DeleteCartesianSpaceQuery> {
            deleteCartesianSpace(it)
            refreshDependencies()
        }
    }

    private fun getAllCartesianSpaces() {
        val event = GetAllCartesiansEvent(model.cartesianSpaces)
        fire(event)
    }

    private fun refreshDependencies() {
        getAllCartesianSpaces()
        val functions = model.cartesianSpaces.map { it.functions }.flatten()
        val functionEvent = GetAllFunctionsEvent(functions)
        fire(functionEvent)

        val axises = model.cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten()
        val axisEvent = GetAllAxisesEvent(axises)
        fire(axisEvent)
    }

    private fun updateCartesian(event: UpdateCartesianEvent) {
        val cartesian = model.cartesianSpaces.first { it.id == event.id }
        cartesian.isShowGrid = event.isShowGrid
        cartesian.name = event.name
        view.redraw()
    }

    private fun deleteCartesianSpace(event: DeleteCartesianSpaceQuery) {
        model.cartesianSpaces.removeIf {
            it.id == event.id
        }
        view.redraw()
    }
}