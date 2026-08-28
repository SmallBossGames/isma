package ru.nstu.grin.concatenation.function.controller

import javafx.stage.FileChooser
import javafx.stage.Window
import org.koin.core.scope.Scope
import ru.nstu.grin.concatenation.file.options.view.FileOptionsView
import ru.nstu.grin.concatenation.function.model.FileModel

class FileFragmentController(
    private val model: Lazy<FileModel>,
    private val scope: Scope,
) {
    private val file by model

    fun chooseFile(window: Window? = null) {
        val fileChooser = FileChooser().apply {
            title = "Choose File"
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("File Path", "*.csv", "*.xls", "*.xlsx")
            )
        }

        val selectedFile = fileChooser.showOpenDialog(window) ?: return

        file.file = selectedFile
        FileOptionsView.openModal(scope, window)
    }
}
