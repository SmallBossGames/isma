package ru.nstu.grin.concatenation.cartesian.events

import tornadofx.FXEvent
import java.util.*

data class DeleteCartesianSpaceQuery(
    val id: UUID
) : FXEvent()