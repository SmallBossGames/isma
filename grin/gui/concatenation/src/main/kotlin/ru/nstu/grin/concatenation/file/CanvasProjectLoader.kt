package ru.nstu.grin.concatenation.file

import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.project.ProjectSnapshot
import ru.nstu.grin.concatenation.canvas.model.project.toModel
import ru.nstu.grin.concatenation.canvas.model.project.toSnapshot
import tornadofx.Controller
import tornadofx.Scope

//TODO: implement arrows saving
class CanvasProjectLoader(override val scope: Scope) : Controller(), KoinComponent {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()

    fun save(window: Window? = null) {
        val file = FileChooser().run {
            title = "Save Chart"
            extensionFilters.addAll(grinChartDataFileFilters)
            showSaveDialog(window)
        } ?: return

        val project = ProjectSnapshot(
            spaces = model.cartesianSpaces.map { it.toSnapshot() },
            descriptions = model.descriptions.map { it.toSnapshot() }
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

        model.cartesianSpaces.setAll(project.spaces.map { it.toModel() })
        model.descriptions.setAll(project.descriptions.map { it.toModel() })
        model.arrows.clear()

        coroutineScope.launch {
            model.reportUpdateAll()
        }
    }

    private companion object {
        val grinChartDataFileFilters = arrayOf(
            FileChooser.ExtensionFilter("Grin Chart Data", "*.chart.json")
        )
    }
}