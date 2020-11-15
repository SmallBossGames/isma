package ru.nstu.grin.simple.events

import ru.nstu.grin.simple.model.LogarithmAxis
import tornadofx.FXEvent

data class TurnLogarithmScaleEvent(
    val axis: LogarithmAxis
) : FXEvent()