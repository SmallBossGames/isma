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
    var font: String,
    var isSelected: Boolean = false
) : Writer {
    override fun serialize(oos: ObjectOutputStream) {
        oos.writeUTF(text)
        oos.writeDouble(textSize)
        oos.writeDouble(x)
        oos.writeDouble(y)
        oos.write(color.toByteArray())
    }

    fun isLocated(eventX: Double, eventY: Double): Boolean {
        return x - textSize*2 < eventX && eventX < x + textSize*2 && y - textSize*2 < eventY && eventY < y + textSize*2
    }
}