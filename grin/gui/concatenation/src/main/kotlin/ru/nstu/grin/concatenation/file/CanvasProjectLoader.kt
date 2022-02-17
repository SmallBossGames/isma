package ru.nstu.grin.concatenation.file

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.file.model.SavedCanvas
import tornadofx.Controller
import tornadofx.Scope
import java.io.File

class CanvasProjectLoader(override val scope: Scope) : Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val mapper = createObjectMapper()

    fun save(path: File) {
        val savedCanvas = SavedCanvas(
            cartesians = model.cartesianSpaces,
            descriptions = model.descriptions,
            arrows = model.arrows
        )

        path.bufferedWriter(Charsets.UTF_8).use {
            val length = mapper.writeValueAsString(savedCanvas).length
            println("Length $length")
            val sequenceWriter = mapper.writer().writeValues(it)
            sequenceWriter.write(savedCanvas)
        }
    }

    fun load(path: File) {
        val json = path.readText(Charsets.UTF_8)

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