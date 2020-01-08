package ru.nstu.grin.file

import ru.nstu.grin.extensions.readColor
import ru.nstu.grin.model.drawable.Function
import java.io.ObjectInputStream

class FunctionReader : Reader<Function> {
    private val axisReader = AxisReader()

    override fun deserialize(ois: ObjectInputStream): Function {
        return Function(
            name = ois.readUTF(),
            pointArray = ois.readObject() as List<ru.nstu.grin.model.Point>,
            xAxis = axisReader.deserialize(ois),
            yAxis = axisReader.deserialize(ois),
            functionColor = readColor(ois)
        )
    }
}