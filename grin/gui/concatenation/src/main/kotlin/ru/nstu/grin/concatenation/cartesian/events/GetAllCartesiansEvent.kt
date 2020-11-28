package ru.nstu.grin.concatenation.cartesian.events

import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import tornadofx.FXEvent

data class GetAllCartesiansEvent(
    val cartesianSpaces: List<CartesianSpace>
) : FXEvent()