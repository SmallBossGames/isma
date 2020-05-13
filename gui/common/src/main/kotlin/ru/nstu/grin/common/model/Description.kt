package ru.nstu.grin.common.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.extensions.toByteArray
import ru.nstu.grin.common.file.Writer
import java.io.ObjectOutputStream
import java.util.*

/**
 * @author kostya05983
 */
data class Description(
    val id: UUID,
    var text: String,
    var textSize: Double,
    var x: Double,
    var y: Double,
    var color: Color,
    var font: String
) : Writer {
//    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
//        return when (direction) {
//            CoordinateDirection.X -> Description(
//                text, size, x * scale, y, color
//            )
//            CoordinateDirection.Y -> Description(
//                text, size, x, y * scale, color
//            )
//        }
//    }
//
//    override fun draw(context: GraphicsContext) {
//        context.stroke = color
//        context.strokeText(text, x, y, size)
//    }
//
//    override fun isOnIt(x: Double, y: Double): Boolean {
//        return true
//    }

    override fun serialize(oos: ObjectOutputStream) {
        oos.writeUTF(text)
        oos.writeDouble(textSize)
        oos.writeDouble(x)
        oos.writeDouble(y)
        oos.write(color.toByteArray())
    }
}