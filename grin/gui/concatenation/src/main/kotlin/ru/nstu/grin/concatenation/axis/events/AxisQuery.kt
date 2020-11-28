package ru.nstu.grin.concatenation.axis.events

import tornadofx.FXEvent
import java.util.*

data class AxisQuery(
    val id: UUID
) : FXEvent()