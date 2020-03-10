package ru.nstu.grin.model.drawable

import javafx.scene.paint.Color
import ru.nstu.grin.extensions.toByteArray
import ru.nstu.grin.file.Writer
import ru.nstu.grin.model.Locationable
import java.io.ObjectOutputStream

/**
 * @author kostya05983
 * Class represents arrow on plot
 * Help to show how description relative to function
 */
data class Arrow(
    var color: Color,
    val x: Double,
    val y: Double
) : Locationable, Writer {
    override fun isOnIt(x: Double, y: Double): Boolean {
        return true
    }

    /**
     * color
     * x
     * y
     */
    override fun serialize(oos: ObjectOutputStream) {
        oos.write(color.toByteArray())
        oos.writeDouble(x)
        oos.writeDouble(y)
    }
}