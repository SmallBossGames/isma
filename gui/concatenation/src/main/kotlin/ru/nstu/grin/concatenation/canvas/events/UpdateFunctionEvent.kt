package ru.nstu.grin.concatenation.canvas.events

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.FXEvent
import java.util.*

data class UpdateFunctionEvent(
    val id: UUID,
    val name: String,
    val color: Color,
    val lineSize: Double,
    val lineType: LineType,
    val isHide: Boolean
) : FXEvent()