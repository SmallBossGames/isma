package ru.nstu.grin.file

import ru.nstu.grin.extensions.readColor
import ru.nstu.grin.model.drawable.Description
import java.io.ObjectInputStream

class DescriptionReader : Reader<Description> {
    override fun deserialize(ois: ObjectInputStream): Description {
        return Description(
            text = ois.readObject() as String,
            size = ois.readDouble(),
            x = ois.readDouble(),
            y = ois.readDouble(),
            color = readColor(ois)
        )
    }
}