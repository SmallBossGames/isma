package ru.nstu.grin.concatenation.file

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.file.model.SavedCanvas
import tornadofx.Controller
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Path

class CanvasProjectLoader : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val mapper = createObjectMapper()

    fun save(path: Path) {
        val savedCanvas = SavedCanvas(
            cartesians = model.cartesianSpaces,
            descriptions = model.descriptions,
            arrows = model.arrows
        )
        FileOutputStream(path.toFile()).use {
            val length = mapper.writeValueAsString(savedCanvas).length
            println("Length $length")
            val sequenceWriter = mapper.writer().writeValues(it)
            sequenceWriter.write(savedCanvas)
        }
    }

    fun load(path: Path) {
        val json = FileInputStream(path.toAbsolutePath().toFile()).use {
            it.readBytes().toString(Charsets.UTF_8)
        }

        val savedCanvas = mapper.readValue<SavedCanvas>(json)
        model.cartesianSpaces.clear()
        model.cartesianSpaces.setAll(savedCanvas.cartesians)
        model.descriptions.clear()
        model.descriptions.setAll(savedCanvas.descriptions)
        model.arrows.clear()
        model.arrows.setAll(savedCanvas.arrows)
    }

    fun createObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}