package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.extensions.toByteArray
import ru.nstu.grin.file.Writer
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.DraggedDirection
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.Point
import ru.nstu.grin.model.drawable.axis.AbstractAxis
import ru.nstu.grin.settings.SettingsProvider
import java.io.ObjectOutputStream

/**
 * @author kostya05983
 */
data class ConcatenationFunction(
    val name: String,
    val points: List<Point>,
    val xAxis: AbstractAxis,
    val yAxis: AbstractAxis,
    val functionColor: Color
) : Drawable, Writer {
    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
        return when (direction) {
            CoordinateDirection.X -> {
                ConcatenationFunction(
                    name,
                    points.map { Point(it.x * scale, it.y) },
                    xAxis.scale(scale, direction) as AbstractAxis,
                    yAxis,
                    functionColor
                )
            }
            CoordinateDirection.Y -> {
                ConcatenationFunction(
                    name,
                    points.map { Point(it.x * scale, it.y) },
                    xAxis,
                    yAxis.scale(scale, direction) as AbstractAxis,
                    functionColor
                )
            }
        }
    }

    fun moveFunctionOnPlot(value: Double, direction: DraggedDirection): Drawable {
        return when (direction) {
            DraggedDirection.LEFT -> {
                ConcatenationFunction(
                    name,
                    points.map { Point(it.x - value, it.y) },
                    xAxis.changeDeltas(value, direction),
                    yAxis,
                    functionColor
                )
            }
            DraggedDirection.RIGHT -> {
                ConcatenationFunction(
                    name,
                    points.map { Point(it.x + value, it.y) },
                    xAxis.changeDeltas(value, direction),
                    yAxis,
                    functionColor
                )
            }
            DraggedDirection.UP -> {
                ConcatenationFunction(
                    name,
                    points.map { Point(it.x, it.y + value) },
                    xAxis,
                    yAxis.changeDeltas(value, direction),
                    functionColor
                )
            }
            DraggedDirection.DOWN -> {
                ConcatenationFunction(
                    name,
                    points.map { Point(it.x, it.y - value) },
                    xAxis,
                    yAxis.changeDeltas(value, direction),
                    functionColor
                )
            }
            DraggedDirection.UNDEFINED -> {
                ConcatenationFunction(
                    name, points, xAxis, yAxis, functionColor
                )
            }
        }
    }

    override fun draw(context: GraphicsContext) {
        context.strokePolyline(
            points.map { it.x + AbstractAxis.WIDTH_AXIS }.toDoubleArray(),
            points.map {
                SettingsProvider.getCanvasHeight() - it.y - AbstractAxis.WIDTH_AXIS
            }.toDoubleArray(),
            points.size
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
        oos.writeObject(points)
        xAxis.serialize(oos)
        yAxis.serialize(oos)
        oos.write(functionColor.toByteArray())
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return true
    }
}
