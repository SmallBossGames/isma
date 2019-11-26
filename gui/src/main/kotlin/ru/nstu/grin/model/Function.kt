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
    val xDirection: Direction,
    val yDirection: Direction,
    val minDelta: Double,
    val delta: Double,
    val functionColor: Color,
    val xAxisColor: Color,
    val yAxisColor: Color
) : FormType, Drawable {
    override fun draw(context: GraphicsContext) {
        context.strokePolyline(
            pointArray.map { it.x }.toDoubleArray(),
            pointArray.map { it.y }.toDoubleArray(),
            pointArray.size
        )
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
        val byteBuffer = ByteBuffer.allocate(4096)
        byteBuffer.put(xDirection.ordinal.toByte())
        byteBuffer.put(yDirection.ordinal.toByte())
        byteBuffer.putDouble(minDelta)
        byteBuffer.putDouble(delta)

        byteBuffer.put(functionColor.toBytes())
        byteBuffer.put(xAxisColor.toBytes())
        byteBuffer.put(yAxisColor.toBytes())

        pointArray.forEach {
            byteBuffer.putDouble(it.x)
            byteBuffer.putDouble(it.y)
        }
        return byteBuffer.array()
    }

    private fun Color.toBytes(): ByteArray {
        val byteBuffer = ByteBuffer.allocate(12)
        byteBuffer.putDouble(blue)
        byteBuffer.putDouble(green)
        byteBuffer.putDouble(red)
        return byteBuffer.array()
    }
}