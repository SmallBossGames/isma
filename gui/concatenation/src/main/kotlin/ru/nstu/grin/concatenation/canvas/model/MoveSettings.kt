package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import java.util.*

data class MoveSettings(
    var id: UUID,
    val type: MovedElementType,
    val pressedX: Double,
    val pressedY: Double,
    var xAxis: ConcatenationAxis? = null,
    var yAxis: ConcatenationAxis? = null
)