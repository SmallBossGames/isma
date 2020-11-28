package ru.nstu.grin.concatenation.file.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.file.model.ColorDTO

class ColorDeserializer(clazz: Class<*>? = null) : StdDeserializer<Color>(clazz) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Color {
        return p.readValueAs(ColorDTO::class.java).let { Color.color(it.red, it.green, it.blue, it.opacity) }
    }
}