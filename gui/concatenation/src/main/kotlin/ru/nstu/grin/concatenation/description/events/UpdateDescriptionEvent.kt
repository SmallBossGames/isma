package ru.nstu.grin.concatenation.description.events

import javafx.scene.paint.Color
import tornadofx.FXEvent
import java.util.*

data class UpdateDescriptionEvent(
    val id: UUID,
    val text: String,
    val textSize: Double,
    val color: Color,
    val font: String
) : FXEvent()