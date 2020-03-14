package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.extensions.readColor
import ru.nstu.grin.common.file.Reader
import ru.nstu.grin.common.model.Description
import java.io.ObjectInputStream

class DescriptionReader : Reader<Description> {
    override fun deserialize(ois: ObjectInputStream): Description {
        return Description(
            text = ois.readUTF(),
            size = ois.readDouble(),
            x = ois.readDouble(),
            y = ois.readDouble(),
            color = readColor(ois)
        )
    }
}