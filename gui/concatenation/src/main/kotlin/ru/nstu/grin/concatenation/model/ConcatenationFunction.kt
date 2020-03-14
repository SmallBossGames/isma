package ru.nstu.grin.concatenation.model

import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.common.extensions.toByteArray
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.file.Writer
import ru.nstu.grin.concatenation.model.axis.AbstractAxis
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
) : Locationable, Writer {
    //TODO move to draw scale

    // TODO move to draw chain
//    fun moveFunctionOnPlot(value: Double, direction: DraggedDirection): Drawable {
//        return when (direction) {
//            DraggedDirection.LEFT -> {
//                ConcatenationFunction(
//                    name,
//                    points.map { Point(it.x - value, it.y) },
//                    xAxis.changeDeltas(value, direction),
//                    yAxis,
//                    functionColor
//                )
//            }
//            DraggedDirection.RIGHT -> {
//                ConcatenationFunction(
//                    name,
//                    points.map { Point(it.x + value, it.y) },
//                    xAxis.changeDeltas(value, direction),
//                    yAxis,
//                    functionColor
//                )
//            }
//            DraggedDirection.UP -> {
//                ConcatenationFunction(
//                    name,
//                    points.map { Point(it.x, it.y + value) },
//                    xAxis,
//                    yAxis.changeDeltas(value, direction),
//                    functionColor
//                )
//            }
//            DraggedDirection.DOWN -> {
//                ConcatenationFunction(
//                    name,
//                    points.map { Point(it.x, it.y - value) },
//                    xAxis,
//                    yAxis.changeDeltas(value, direction),
//                    functionColor
//                )
//            }
//            DraggedDirection.UNDEFINED -> {
//                ConcatenationFunction(
//                    name, points, xAxis, yAxis, functionColor
//                )
//            }
//        }
//    }

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
