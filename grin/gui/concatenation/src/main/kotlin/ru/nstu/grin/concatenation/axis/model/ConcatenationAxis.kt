package ru.nstu.grin.concatenation.axis.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import java.util.*

data class ConcatenationAxis(
    val id: UUID,
    val name: String,
    val order: Int,
    val direction: Direction,

    var backGroundColor: Color,

    var fontColor: Color,

    var distanceBetweenMarks: Double,
    var textSize: Double,
    var font: String,
    var isHide: Boolean = false,
    var axisMarkType: AxisMarkType = AxisMarkType.LINEAR,
    val settings: AxisSettings = AxisSettings()
) : Cloneable {

    fun isLogarithmic(): Boolean {
        return axisMarkType == AxisMarkType.LOGARITHMIC
    }

    override fun clone(): Any {
        return ConcatenationAxis(
            id = id,
            name = name,
            order = order,
            direction = direction,
            backGroundColor = backGroundColor,
            fontColor = fontColor,
            distanceBetweenMarks = distanceBetweenMarks,
            textSize = textSize,
            font = font,
            settings = settings.copy()
        )
    }

    fun isLocated(x: Double, y: Double, canvasWidth: Double, canvasHeight: Double): Boolean {
        when (direction) {
            Direction.LEFT -> {
                val maxX = (order + 1) * SettingsProvider.getAxisWidth()
                val minX = order * SettingsProvider.getAxisWidth()
                return x < maxX && x > minX
            }
            Direction.RIGHT -> {
                val maxX = canvasWidth - order * SettingsProvider.getAxisWidth()
                val minX = canvasWidth - (order + 1) * SettingsProvider.getAxisWidth()

                return x > minX && x < maxX
            }
            Direction.TOP -> {
                val maxY = (order + 1) * SettingsProvider.getAxisWidth()
                val minY = order * SettingsProvider.getAxisWidth()

                return y > minY && y < maxY
            }
            Direction.BOTTOM -> {
                val maxY = canvasHeight - order * SettingsProvider.getAxisWidth()
                val minY = canvasHeight - (order + 1) * SettingsProvider.getAxisWidth()

                return y > minY && y < maxY
            }
        }
    }

    val isXAxis get() = direction == Direction.TOP || direction == Direction.BOTTOM

    val isYAxis get() = direction == Direction.RIGHT || direction == Direction.LEFT
}