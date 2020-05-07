package ru.nstu.grin.concatenation.canvas.events

import javafx.scene.paint.Color
import tornadofx.FXEvent
import java.util.*

data class UpdateAxisEvent(
    val id: UUID,
    val distance: Double,
    val textSize: Double,
    val font: String,
    val fontColor: Color,
    val axisColor: Color
) : FXEvent()