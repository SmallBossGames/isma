package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.common.extensions.toByteArray
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.file.Writer
import java.io.ObjectOutputStream
import java.util.*

/**
 * @author kostya05983
 */
data class ConcatenationFunction(
    val id: UUID,
    val name: String,
    val points: List<Point>,
    val functionColor: Color,
    val lineSize: Double,
    val lineType: LineType
) : Writer {
    fun getShape(): Shape {
        return Line(0.0, 10.0, 0.0, 20.0)
    }

    override fun serialize(oos: ObjectOutputStream) {
        oos.writeObject(points)
//        xAxis.serialize(oos)
//        yAxis.serialize(oos)
        oos.write(functionColor.toByteArray())
    }
}
