package ru.nstu.grin.concatenation.model.axis

import javafx.scene.paint.Color
import ru.nstu.grin.common.extensions.toByteArray
import ru.nstu.grin.common.file.Writer
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.Direction
import java.io.ObjectOutputStream

/**
 * @param zeroPoint - Точка нуля, где он располжен
 * @param marksProvider - поставитель меток оси
 * @param order -
 */
class ConcatenationAxis(
    // Точка откуда начинаем отрисовку
    val zeroPoint: Double,
    val marksProvider: MarksProvider,
    val order: Int,
    val direction: Direction,
    val backGroundColor: Color,
    val delimiterColor: Color
) : Writer {

    override fun serialize(oos: ObjectOutputStream) {
        oos.writeObject(direction)
        oos.writeDouble(zeroPoint)
        oos.write(backGroundColor.toByteArray())
        oos.write(delimiterColor.toByteArray())
    }

    internal companion object {
        const val WIDTH_AXIS = 50.0 // 100 px in default
    }
}