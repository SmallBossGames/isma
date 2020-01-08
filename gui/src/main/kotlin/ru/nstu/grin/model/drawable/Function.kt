package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.extensions.toByteArray
import ru.nstu.grin.file.Writer
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.Point
import ru.nstu.grin.model.drawable.axis.AbstractAxis
import ru.nstu.grin.settings.SettingProvider
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

/**
 * @author kostya05983
 */
data class Function(
    val name: String,
    val pointArray: List<Point>,
    val xAxis: AbstractAxis,
    val yAxis: AbstractAxis,
    val functionColor: Color
) : Drawable, Writer {
    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
        return when (direction) {
            CoordinateDirection.X -> {
                Function(
                    name,
                    pointArray.map { Point(it.x * scale, it.y) },
                    xAxis.scale(scale, direction) as AbstractAxis,
                    yAxis,
                    functionColor
                )
            }
            CoordinateDirection.Y -> {
                Function(
                    name,
                    pointArray.map { Point(it.x * scale, it.y) },
                    xAxis,
                    yAxis.scale(scale, direction) as AbstractAxis,
                    functionColor
                )
            }
        }
    }

    override fun draw(context: GraphicsContext) {
        context.strokePolyline(
            pointArray.map { it.x + AbstractAxis.WIDTH_AXIS }.toDoubleArray(),
            pointArray.map {
                SettingProvider.getCanvasHeight() - it.y - AbstractAxis.WIDTH_AXIS
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

    override fun serialize(oos: ObjectOutputStream) {
        oos.writeObject(pointArray)
        xAxis.serialize(oos)
        yAxis.serialize(oos)
        oos.write(functionColor.toByteArray())
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return true
    }
}
