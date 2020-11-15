package ru.nstu.grin.concatenation.cartesian.service

import ru.nstu.grin.concatenation.axis.events.GetAllAxisesEvent
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.cartesian.events.*
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsEvent
import tornadofx.Controller
import java.util.*

class CartesianCanvasService : Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun copyCartesian(event: CartesianCopyQuery) {
        val oldCartesian = model.cartesianSpaces.first { it.id == event.id }
        val newCartesian = oldCartesian.clone().copy(
            id = UUID.randomUUID(), name = event.name,
            xAxis = oldCartesian.xAxis.copy(
                id = UUID.randomUUID(),
                name = event.xAxisName,
                order = oldCartesian.xAxis.order + 1,
                settings = oldCartesian.xAxis.settings.copy()
            ),
            yAxis = oldCartesian.yAxis.copy(
                id = UUID.randomUUID(),
                name = event.yAxisName,
                order = oldCartesian.yAxis.order + 1,
                settings = oldCartesian.yAxis.settings.copy()
            )
        )
        model.cartesianSpaces.add(newCartesian)
        view.redraw()
        getAllCartesianSpaces()
    }

    fun getCartesian(event: CartesianQuery) {
        val cartesianSpace = model.cartesianSpaces.first { it.id == event.id }
        fire(GetCartesianEvent(cartesianSpace))
    }

    fun getAllCartesianSpaces() {
        val event =
            GetAllCartesiansEvent(model.cartesianSpaces)
        fire(event)
    }

    fun updateCartesian(event: UpdateCartesianEvent) {
        val cartesian = model.cartesianSpaces.first { it.id == event.id }
        cartesian.isShowGrid = event.isShowGrid
        cartesian.name = event.name
        view.redraw()
        getAllCartesianSpaces()
    }

    fun deleteCartesianSpace(event: DeleteCartesianSpaceQuery) {
        model.cartesianSpaces.removeIf {
            it.id == event.id
        }
        view.redraw()
        refreshDependencies()
    }

    private fun refreshDependencies() {
        getAllCartesianSpaces()
        val functions = model.cartesianSpaces.map { it.functions }.flatten()
        val functionEvent =
            GetAllFunctionsEvent(functions)
        fire(functionEvent)

        val axises = model.cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten()
        val axisEvent = GetAllAxisesEvent(axises)
        fire(axisEvent)
    }
}