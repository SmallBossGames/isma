package ru.nstu.grin.concatenation.file.options.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.scope.Scope
import ru.nstu.grin.concatenation.file.options.controller.FileOptionsController
import ru.nstu.grin.concatenation.file.options.model.CsvDetails
import ru.nstu.grin.concatenation.file.options.model.ExcelDetails
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.file.utilities.getFileType
import ru.nstu.grin.concatenation.file.utilities.showError
import ru.nstu.grin.concatenation.function.model.FileModel
import ru.nstu.grin.concatenation.function.model.FileType

class FileOptionsView(
    private val model: FileModel,
    private val controller: FileOptionsController,
) : VBox() {

    init {
        run {
            val fileType = model.file.getFileType()
            if (fileType != FileType.XLS && fileType != FileType.XLSX && fileType != FileType.CSV) return@run

            when (fileType) {
                FileType.XLS, FileType.XLSX -> model.details = ExcelDetails()
                FileType.CSV -> model.details = CsvDetails()
            }

        styleClass.add("file-options-view")
        padding = Insets(10.0)
        spacing = 10.0

        val header = Label("Опции чтения")
        header.styleClass.add("header")

        val sheetNameField = TextField().apply {
            if (model.details is ExcelDetails) {
                textProperty().bindBidirectional((model.details as ExcelDetails).sheetNameProperty)
            } else {
                isVisible = false
            }
        }

        val rangeField = TextField().apply {
            if (model.details is ExcelDetails) {
                textProperty().bindBidirectional((model.details as ExcelDetails).rangeProperty)
            } else {
                isVisible = false
            }
        }

        val delimiterField = TextField().apply {
            if (model.details is CsvDetails) {
                textProperty().bindBidirectional((model.details as CsvDetails).delimiterProperty)
            } else {
                isVisible = false
            }
        }

        val readerModeCombo = ComboBox<FileReaderMode>(
            FXCollections.observableArrayList(FileReaderMode.values().toList())
        ).apply {
            valueProperty().bindBidirectional(model.readerModeProperty)
            cellFactory = { _: javafx.scene.control.ListView<FileReaderMode> ->
                object : ListCell<FileReaderMode>() {
                    override fun updateItem(item: FileReaderMode?, empty: Boolean) {
                        super.updateItem(item, empty)
                        text = when (item) {
                            FileReaderMode.ONE_TO_MANY -> "Один ко многим"
                            FileReaderMode.SEQUENCE -> "Функции подряд"
                            else -> ""
                        }
                    }
                }
            }
        }

        val readButton = Button("Прочитать").apply {
            setOnAction {
                when (val details = model.details) {
                    is ExcelDetails -> {
                        if (!details.range.contains(":")) {
                            showError("Формат должен быть записан в следующем виде A0:B2")
                            return@setOnAction
                        }
                    }
                    is CsvDetails -> {}
                    else -> {}
                }
                controller.openPointsWindow()
            }
        }

        children.setAll(header, sheetNameField, rangeField, delimiterField, readerModeCombo, readButton)
        }
    }

    companion object {
        fun openModal(koinScope: Scope, owner: Window? = null) {
            val view = koinScope.get<FileOptionsView>()
            Stage().apply {
                scene = Scene(view)
                title = "Опции файла"
                initModality(Modality.WINDOW_MODAL)
                if (owner != null) initOwner(owner)
                showAndWait()
            }
        }
    }
}
