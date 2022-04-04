package ru.nstu.grin.concatenation.function.controller

import javafx.stage.FileChooser
import javafx.stage.Window
import ru.nstu.grin.concatenation.file.options.view.FileOptionsView
import ru.nstu.grin.concatenation.function.model.FileModel

class FileFragmentController(
    model: Lazy<FileModel>,
    fileOptionsView: Lazy<FileOptionsView>,
) {
    private val model by model
    private val fileOptionsView by fileOptionsView

    fun chooseFile(window: Window? = null) {
        val file = FileChooser().run {
            title = "Choose File"
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("File Path", "*.csv", "*.xls", "*.xlsx")
            )

            showOpenDialog(window)
        } ?: return

        model.file = file
        fileOptionsView.openModal()
    }
}