package ru.nstu.grin.file

import ru.nstu.grin.extensions.readColor
import ru.nstu.grin.model.drawable.Arrow
import java.io.ObjectInputStream

class ArrowReader : Reader<Arrow> {
    override fun deserialize(ois: ObjectInputStream): Arrow {
        val color = readColor(ois)
        val x = ois.readDouble()
        val y = ois.readDouble()
        return Arrow(color, x, y)
    }
}