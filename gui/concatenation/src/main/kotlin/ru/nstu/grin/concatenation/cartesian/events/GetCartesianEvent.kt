package ru.nstu.grin.concatenation.cartesian.events

import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import tornadofx.FXEvent

data class GetCartesianEvent(
    val cartesianSpace: CartesianSpace
) : FXEvent()