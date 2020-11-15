package ru.nstu.grin.concatenation.function.controller

import javafx.stage.FileChooser
import ru.nstu.grin.concatenation.file.options.model.FileOptionsModel
import ru.nstu.grin.concatenation.file.options.view.FileOptionsView
import ru.nstu.grin.concatenation.function.model.FileModel
import tornadofx.Controller
import tornadofx.FileChooserMode

class FileFragmentController : Controller() {
    private val model: FileModel by inject()

    fun chooseFile() {
        val files = tornadofx.chooseFile(
            "Файл",
            arrayOf(FileChooser.ExtensionFilter("Путь к файлу", "*.csv", "*.xls", "*.xlsx")),
            FileChooserMode.Single
        )
        if (files.isEmpty()) {
            tornadofx.error("Файл не был выбран")
            return
        }
        model.file = files[0]
        find<FileOptionsView>().openModal()
    }
}