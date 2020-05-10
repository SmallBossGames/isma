package ru.nstu.grin.concatenation.canvas.events

import tornadofx.FXEvent
import java.util.*

data class UpdateCartesianEvent(
    val id: UUID,
    val name: String,
    val isShowGrid: Boolean
) : FXEvent()