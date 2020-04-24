package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.extensions.readColor
import ru.nstu.grin.common.file.Reader
import ru.nstu.grin.concatenation.model.ConcatenationAxis
import ru.nstu.grin.concatenation.model.Direction
import java.io.ObjectInputStream

class AxisReader : Reader<ConcatenationAxis> {
    override fun deserialize(ois: ObjectInputStream): ConcatenationAxis {
        val direction = ois.readObject() as Direction
        val startPoint = ois.readDouble()
        val minDelta = ois.readDouble()
        val deltaMarks = ois.readObject() as List<Double>
        val backGroundColor = readColor(ois)
        val delimiterColor = readColor(ois)
        TODO("Implement read of axis")
//        return  ConcatenationAxis(
//
//        )
    }
}