package ru.nstu.grin.concatenation.file.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.file.model.ColorDTO

class ColorSerializer(clazz: Class<Color>? = null) : StdSerializer<Color>(clazz) {
    override fun serialize(value: Color, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeObject(ColorDTO(value.red, value.green, value.blue, value.opacity))
    }
}