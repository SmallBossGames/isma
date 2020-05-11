package ru.nstu.grin.concatenation.cartesian.events

import tornadofx.FXEvent
import java.util.*

data class CartesianCopyQuery(
    val id: UUID,
    val name: String,
    val xAxisName: String,
    val yAxisName: String
) : FXEvent()