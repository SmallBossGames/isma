package ru.nstu.grin.concatenation.canvas.events

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import tornadofx.FXEvent

data class GetAllAxisesEvent(
    val axises: List<ConcatenationAxis>
) : FXEvent()