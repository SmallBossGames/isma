package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.events.GetCartesianEvent
import ru.nstu.grin.concatenation.cartesian.events.UpdateCartesianEvent
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel
import tornadofx.Controller
import java.util.*

class ChangeCartesianController : Controller() {
    val cartesianId: UUID by param()
    private val model: ChangeCartesianSpaceModel by inject()

    init {
        subscribe<GetCartesianEvent> {
            if (it.cartesianSpace.id == cartesianId) {
                model.name = it.cartesianSpace.name
                model.isShowGrid = it.cartesianSpace.isShowGrid
            }
        }
    }

    fun updateCartesianSpace() {
        val event = UpdateCartesianEvent(
            id = cartesianId,
            name = model.name,
            isShowGrid = model.isShowGrid
        )
        fire(event)
    }
}