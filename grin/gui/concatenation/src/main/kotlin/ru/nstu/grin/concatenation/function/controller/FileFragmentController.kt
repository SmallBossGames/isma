package ru.nstu.grin.concatenation.function.controller

import javafx.scene.control.Alert
import javafx.stage.FileChooser
import javafx.stage.Window
import ru.nstu.grin.concatenation.file.options.view.FileOptionsView
import ru.nstu.grin.concatenation.function.model.FileModel

class FileFragmentController(
    private val model: Lazy<FileModel>,
    private val fileOptionsView: Lazy<FileOptionsView>,
) {
    fun chooseFile(window: Window? = null) {
        val file = FileChooser().run {
            title = "Choose File"
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("File Path", "*.csv", "*.xls", "*.xlsx")
            )

            showOpenDialog(window)
        }

        if (file == null) {
            Alert(Alert.AlertType.ERROR).apply {
                contentText = "File not selected"
                showAndWait()
            }
            return
        }

        model.value.file = file
        fileOptionsView.value.openModal()
    }
}