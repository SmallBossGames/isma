package ru.nstu.grin.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.Direction
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
    override fun draw(context: GraphicsContext) {
        context.strokePolyline(
            pointArray.map { it.x }.toDoubleArray(),
            pointArray.map { it.y }.toDoubleArray(),
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
}