package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.FormType
import ru.nstu.grin.model.Point
import ru.nstu.grin.model.Scalable
import ru.nstu.grin.settings.SettingProvider
import java.nio.ByteBuffer

/**
 * @author kostya05983
 */
data class Function(
    val pointArray: List<Point>,
    val xAxis: Axis,
    val yAxis: Axis,
    val functionColor: Color
) : FormType, Drawable {
    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
        return when (direction) {
            CoordinateDirection.X -> {
                Function(
                    pointArray.map { Point(it.x * scale, it.y) },
                    xAxis.scale(scale, direction) as Axis,
                    yAxis,
                    functionColor
                )
            }
            CoordinateDirection.Y -> {
                Function(
                    pointArray.map { Point(it.x * scale, it.y) },
                    xAxis,
                    yAxis.scale(scale, direction) as Axis,
                    functionColor
                )
            }
        }
    }

    override fun draw(context: GraphicsContext) {
        context.strokePolyline(
            pointArray.map { it.x + Axis.WIDTH_AXIS }.toDoubleArray(),
            pointArray.map {
                SettingProvider.getCanvasHeight() - it.y - Axis.WIDTH_AXIS
            }.toDoubleArray(),
            pointArray.size
        )
        xAxis.draw(context)
        yAxis.draw(context)
    }

    private fun List<Double>.toDoubleArray(): DoubleArray {
        val array = DoubleArray(this.size)
        for (i in this.indices) {
            array[i] = this[i]
        }
        return array
    }

    fun getShape(): Shape {
        return Line(0.0, 10.0, 0.0, 20.0)
    }

    /**
     * Format, next numbers of bytes
     * | 0     - xDirection
     * | 1     - yDirection
     * | 2-6   - minDelta
     * | 7-10  - delta
     * | 11-22 - function Color
     * | 23-34 - xAxisColor
     * | 35-46 - yAxisColor
     * | 47 -n - points array
     */
    override fun toByteArray(): ByteArray {
        TODO("Not implemented")
    }

    private fun Color.toBytes(): ByteArray {
        val byteBuffer = ByteBuffer.allocate(12)
        byteBuffer.putDouble(blue)
        byteBuffer.putDouble(green)
        byteBuffer.putDouble(red)
        return byteBuffer.array()
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return true
    }
}
