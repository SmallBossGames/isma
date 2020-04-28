package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.extensions.readColor
import ru.nstu.grin.common.file.Reader
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import java.io.ObjectInputStream

class FunctionReader : Reader<ConcatenationFunction> {
    private val axisReader = AxisReader()

    override fun deserialize(ois: ObjectInputStream): ConcatenationFunction {
        return ConcatenationFunction(
            name = ois.readUTF(),
            points = ois.readObject() as List<Point>,
            functionColor = readColor(ois)
        )
    }
}