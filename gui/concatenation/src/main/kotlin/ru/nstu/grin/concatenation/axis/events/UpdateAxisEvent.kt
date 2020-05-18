package ru.nstu.grin.concatenation.axis.events

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import tornadofx.FXEvent
import java.util.*

data class UpdateAxisEvent(
    val id: UUID,
    val distance: Double,
    val textSize: Double,
    val font: String,
    val fontColor: Color,
    val axisColor: Color,
    val isHide: Boolean,
    val axisMarkType: AxisMarkType,
    val logarithmBase: Double,
    val isOnlyIntegerPow: Boolean
) : FXEvent()