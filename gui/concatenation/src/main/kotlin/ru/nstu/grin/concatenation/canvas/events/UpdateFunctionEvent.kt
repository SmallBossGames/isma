package ru.nstu.grin.concatenation.canvas.events

import javafx.scene.paint.Color
import tornadofx.FXEvent
import java.util.*

data class UpdateFunctionEvent(
    val id: UUID,
    val color: Color
) : FXEvent()