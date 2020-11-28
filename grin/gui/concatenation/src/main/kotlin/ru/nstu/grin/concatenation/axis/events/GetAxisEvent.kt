package ru.nstu.grin.concatenation.axis.events

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import tornadofx.FXEvent

data class GetAxisEvent(
    val axis: ConcatenationAxis
) : FXEvent()