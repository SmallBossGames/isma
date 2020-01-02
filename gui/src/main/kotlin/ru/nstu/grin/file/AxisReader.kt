package ru.nstu.grin.file

import ru.nstu.grin.extensions.readColor
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.drawable.axis.*
import java.io.ObjectInputStream

class AxisReader : Reader<AbstractAxis> {
    override fun deserialize(ois: ObjectInputStream): AbstractAxis {
        val direction = ois.readObject() as Direction
        val startPoint = ois.readDouble()
        val minDelta = ois.readDouble()
        val deltaMarks = ois.readObject() as List<Double>
        val backGroundColor = readColor(ois)
        val delimiterColor = readColor(ois)
        return when (direction) {
            Direction.LEFT -> LeftAxis(startPoint, minDelta, deltaMarks, backGroundColor, delimiterColor)
            Direction.RIGHT -> RightAxis(startPoint, minDelta, deltaMarks, backGroundColor, delimiterColor)
            Direction.TOP -> TopAxis(startPoint, minDelta, deltaMarks, backGroundColor, delimiterColor)
            Direction.BOTTOM -> BottomAxis(startPoint, minDelta, deltaMarks, backGroundColor, delimiterColor)
        }
    }
}