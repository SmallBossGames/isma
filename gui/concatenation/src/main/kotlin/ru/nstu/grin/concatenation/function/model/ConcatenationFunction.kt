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
    var name: String,
    val points: List<Point>,
    var isHide: Boolean = false,
    var isSelected: Boolean = false,
    var functionColor: Color,
    var lineSize: Double,
    var lineType: LineType,
    val details: MutableList<ConcatenationFunctionDetails> = mutableListOf(MirrorDetails())
) : Writer, Cloneable {
    public override fun clone(): ConcatenationFunction {
        return ConcatenationFunction(
            id = id,
            name = name,
            points = points.map { it.clone() as Point },
            functionColor = functionColor,
            lineSize = lineSize,
            lineType = lineType,
            details = details.map {
                when (it) {
                    is MirrorDetails -> it.copy()
                }
            }.toMutableList()
        )
    }

    fun replaceMirrorDetails(details: MirrorDetails) {
        this.details.removeIf { it is MirrorDetails }
        this.details.add(details)
    }

    fun getMirrorDetails() = details.filterIsInstance<MirrorDetails>().first()

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
