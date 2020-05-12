package ru.nstu.grin.concatenation.axis.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.extensions.toByteArray
import ru.nstu.grin.common.file.Writer
import ru.nstu.grin.concatenation.axis.marks.MarksProvider
import java.io.ObjectOutputStream
import java.util.*

/**
 * @param zeroPoint - Точка нуля, где он располжен
 * @param marksProvider - поставитель меток оси
 * @param order -
 */
data class ConcatenationAxis(
    val id: UUID,
    val name: String,
    val zeroPoint: Double,
    val marksProvider: MarksProvider,
    val order: Int,
    val direction: Direction,
    var backGroundColor: Color,
    var fontColor: Color,
    var distanceBetweenMarks: Double,
    var textSize: Double,
    var font: String,
    var isHide: Boolean = false,
    val settings: AxisSettings = AxisSettings()
) : Writer, Cloneable {

    override fun clone(): Any {
        return ConcatenationAxis(
            id = id,
            name = name,
            zeroPoint = zeroPoint,
            marksProvider = marksProvider,
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

    fun isXAxis(): Boolean {
        return direction == Direction.TOP || direction == Direction.BOTTOM
    }

    fun isYAxis(): Boolean {
        return !isXAxis()
    }

    override fun serialize(oos: ObjectOutputStream) {
        oos.writeObject(direction)
        oos.writeDouble(zeroPoint)
        oos.write(backGroundColor.toByteArray())
        oos.write(fontColor.toByteArray())
    }
}