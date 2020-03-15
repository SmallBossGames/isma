package ru.nstu.grin.concatenation.model.axis

import javafx.scene.paint.Color
import ru.nstu.grin.common.extensions.toByteArray
import ru.nstu.grin.common.file.Writer
import ru.nstu.grin.concatenation.model.Direction
import java.io.ObjectOutputStream

abstract class AbstractAxis(
    // Точка откуда начинаем отрисовку
    val zeroPoint: Double,
    private val minDelta: Double,
    private val deltaMarks: List<Double>,
    private val backGroundColor: Color,
    private val delimiterColor: Color
) : Writer {

    abstract fun getDirection(): Direction

    override fun serialize(oos: ObjectOutputStream) {
        oos.writeObject(getDirection())
        oos.writeDouble(zeroPoint)
        oos.writeDouble(minDelta)
        oos.writeObject(deltaMarks)
        oos.write(backGroundColor.toByteArray())
        oos.write(delimiterColor.toByteArray())
    }

    internal companion object {
        const val WIDTH_AXIS = 50.0 // 100 px in default
    }
}