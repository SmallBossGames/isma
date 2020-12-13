package ru.nstu.grin.simple.controller.function

import javafx.stage.FileChooser
import ru.nstu.grin.simple.dto.SimpleFunctionDTO
import ru.nstu.grin.simple.events.FileCheckedEvent
import ru.nstu.grin.simple.events.SimpleFunctionEvent
import ru.nstu.grin.simple.model.FileOptionsModel
import ru.nstu.grin.simple.model.function.FileFunctionModel
import ru.nstu.grin.simple.view.modal.FileOptionsModalView
import tornadofx.Controller
import tornadofx.FileChooserMode

class FileFunctionController : Controller() {
    private val model: FileFunctionModel by inject()

    fun chooseFile() {
        val files = tornadofx.chooseFile(
            title = "Файл",
            filters = arrayOf(FileChooser.ExtensionFilter("Путь к файлу", "*.gf")),
            mode = FileChooserMode.Single
        )
        if (files.isEmpty()) {
            tornadofx.error("Файл не был выбран")
            return
        }
        find<FileOptionsModalView>(
            mapOf(
                FileOptionsModel::file to files[0]
            )
        ).openModal()
    }

    fun addFunction() {
        val points = model.points?.mapIndexedNotNull { index, point ->
            if (index%model.step == 0) {
                point
            } else {
                null
            }
        }
        if (points == null) {
            tornadofx.error("Необходимо загрузить точки")
            return
        }
        val function = SimpleFunctionDTO(
            name = model.name,
            points = points,
            color = model.color
        )
        fire(
            SimpleFunctionEvent(function)
        )
    }

    init {
        subscribe<FileCheckedEvent> {
            model.points = it.points
        }
    }
}