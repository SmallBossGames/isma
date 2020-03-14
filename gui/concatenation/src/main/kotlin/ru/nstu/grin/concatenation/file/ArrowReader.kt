package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.extensions.readColor
import ru.nstu.grin.common.file.Reader
import ru.nstu.grin.common.model.Arrow
import java.io.ObjectInputStream

class ArrowReader : Reader<Arrow> {
    override fun deserialize(ois: ObjectInputStream): Arrow {
        val color = readColor(ois)
        val x = ois.readDouble()
        val y = ois.readDouble()
        return Arrow(color, x, y)
    }
}