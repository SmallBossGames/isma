package ru.nstu.grin.concatenation.function.view

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.scope.Scope
import ru.nstu.grin.concatenation.function.controller.LocalizeFunctionController
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LocalizeFunctionData
import ru.nstu.grin.concatenation.file.utilities.showError

class LocalizeFunctionFragment(
    private val model: LocalizeFunctionData,
    private val controller: LocalizeFunctionController,
) : VBox() {

    init {
        styleClass.add("localize-function-fragment")
        padding = Insets(10.0)
        spacing = 10.0

        val header = Label("Выберите функцию для локализации")
        header.styleClass.add("header")

        val list = ListView<ConcatenationFunction>(model.functions).apply {
            cellFactory = { _ ->
                object : ListCell<ConcatenationFunction>() {
                    override fun updateItem(function: ConcatenationFunction?, empty: Boolean) {
                        super.updateItem(function, empty)
                        graphic = if (function != null && !empty) {
                            HBox(Label(function.name)).apply {
                                HBox.setHgrow(this, Priority.ALWAYS)
                            }
                        } else {
                            null
                        }
                    }
                }
            }
        }

        val okButton = Button("Ок").apply {
            setOnAction {
                val function = list.selectionModel.selectedItem
                if (function == null) {
                    showError("Необходимо выбрать функцию")
                    return@setOnAction
                }
                controller.localize(function)
            }
        }

        children.setAll(header, list, okButton)
        VBox.setVgrow(list, Priority.ALWAYS)
    }

    companion object {
        fun openModal(koinScope: Scope, owner: Window? = null) {
            val view = koinScope.get<LocalizeFunctionFragment>()
            Stage().apply {
                scene = Scene(view)
                title = "Локализация функции"
                initModality(Modality.WINDOW_MODAL)
                if (owner != null) initOwner(owner)
                showAndWait()
            }
        }
    }
}
