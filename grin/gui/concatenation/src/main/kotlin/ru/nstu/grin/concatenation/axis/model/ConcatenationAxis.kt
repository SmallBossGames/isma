package ru.nstu.grin.concatenation.axis.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.file.json.ColorDeserializer
import ru.nstu.grin.concatenation.file.json.ColorSerializer
import java.util.*

/**
 * @param zeroPoint - Точка нуля, где он располжен
 * @param marksProvider - поставитель меток оси
 * @param order -
 */
data class ConcatenationAxis(
    val id: UUID,
    val name: String,
    val order: Int,
    val direction: Direction,

    @field:JsonDeserialize(using = ColorDeserializer::class)
    @field:JsonSerialize(using = ColorSerializer::class)
    var backGroundColor: Color,

    @field:JsonDeserialize(using = ColorDeserializer::class)
    @field:JsonSerialize(using = ColorSerializer::class)
    var fontColor: Color,

    var distanceBetweenMarks: Double,
    var textSize: Double,
    var font: String,
    var isHide: Boolean = false,
    var axisMarkType: AxisMarkType = AxisMarkType.LINEAR,
    val settings: AxisSettings = AxisSettings()
) : Cloneable {

    @JsonIgnore
    fun isLogarithmic(): Boolean {
        return axisMarkType == AxisMarkType.LOGARITHMIC
    }

    @JsonIgnore
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

    @JsonIgnore
    fun isLocated(x: Double, y: Double): Boolean {
        when (direction) {
            Direction.LEFT -> {
                val maxX = (order + 1) * SettingsProvider.getAxisWidth()
                val minX = order * SettingsProvider.getAxisWidth()
                return x < maxX && x > minX
            }
            Direction.RIGHT -> {
                val maxX = SettingsProvider.getCanvasWidth() - order * SettingsProvider.getAxisWidth()
                val minX = SettingsProvider.getCanvasWidth() - (order + 1) * SettingsProvider.getAxisWidth()

                return x > minX && x < maxX
            }
            Direction.TOP -> {
                val maxY = (order + 1) * SettingsProvider.getAxisWidth()
                val minY = order * SettingsProvider.getAxisWidth()

                return y > minY && y < maxY
            }
            Direction.BOTTOM -> {
                val maxY = SettingsProvider.getCanvasHeight() - order * SettingsProvider.getAxisWidth()
                val minY = SettingsProvider.getCanvasHeight() - (order + 1) * SettingsProvider.getAxisWidth()

                return y > minY && y < maxY
            }
        }
    }

    @JsonIgnore
    fun isXAxis(): Boolean {
        return direction == Direction.TOP || direction == Direction.BOTTOM
    }

    @JsonIgnore
    fun isYAxis(): Boolean {
        return !isXAxis()
    }
}