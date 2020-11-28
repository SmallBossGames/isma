package ru.nstu.grin.concatenation.function.events

import tornadofx.FXEvent
import java.util.*

data class CalculateIntegralEvent(
    val functionId: UUID,
    val leftBorder: Double,
    val rightBorder: Double
) : FXEvent()