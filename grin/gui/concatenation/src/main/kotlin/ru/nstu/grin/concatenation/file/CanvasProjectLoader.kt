package ru.nstu.grin.concatenation.file

import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.project.ProjectSnapshot
import ru.nstu.grin.concatenation.canvas.model.project.toModel
import ru.nstu.grin.concatenation.canvas.model.project.toSnapshot

//TODO: implement arrows saving
class CanvasProjectLoader(
    private val model: ConcatenationCanvasModel,
    private val concatenationCanvasController: ConcatenationCanvasController,
) {
    fun save(window: Window? = null) {
        val file = FileChooser().run {
            title = "Save Chart"
            extensionFilters.addAll(grinChartDataFileFilters)
            showSaveDialog(window)
        } ?: return

        val project = ProjectSnapshot(
            spaces = model.cartesianSpaces.map { it.toSnapshot() },
        )

        file.bufferedWriter(Charsets.UTF_8).use {
            it.write(Json.encodeToString(project))
        }
    }

    fun load(window: Window? = null) {
        val file = FileChooser().run {
            title = "Load Chart"
            extensionFilters.addAll(grinChartDataFileFilters)
            showOpenDialog(window)
        } ?: return

        val json = file.readText(Charsets.UTF_8)
        val project = Json.decodeFromString<ProjectSnapshot>(json)

        concatenationCanvasController.replaceAll(
            project.spaces.map { it.toModel() }
        )
    }

    private companion object {
        val grinChartDataFileFilters = arrayOf(
            FileChooser.ExtensionFilter("Grin Chart Data", "*.chart.json")
        )
    }
}