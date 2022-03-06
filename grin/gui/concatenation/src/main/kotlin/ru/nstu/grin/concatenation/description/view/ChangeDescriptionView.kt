package ru.nstu.grin.concatenation.description.view

import javafx.collections.FXCollections.observableArrayList
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.description.controller.ChangeDescriptionController
import ru.nstu.grin.concatenation.description.model.DescriptionViewModel

class ChangeDescriptionView(
    private val controller: ChangeDescriptionController,
    private val model: DescriptionViewModel
) : BorderPane() {

    init {
        top = VBox(
            propertiesGrid {
                addNode("Text", model.textProperty)
                addNode("Font Size", model.textSizeProperty)
                addNode("Font Color", model.colorProperty)
                addNode("Font Family", observableArrayList(Font.getFamilies()), model.fontProperty)
            }.apply {
                padding = Insets(10.0)
            }
        )

        bottom = HBox(
            Button("Save").apply {
                setOnAction {
                    controller.updateOrCreateDescription(model)
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}