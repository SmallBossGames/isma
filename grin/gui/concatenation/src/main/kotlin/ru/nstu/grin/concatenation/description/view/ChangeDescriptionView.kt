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
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionViewModel

class ChangeDescriptionView(
    private val viewModel: ChangeDescriptionViewModel
) : BorderPane() {
    init {
        top = VBox(
            propertiesGrid {
                addNode("Text", viewModel.textProperty)
                addNode("Font Size", viewModel.textSizeProperty)
                addNode("Font Color", viewModel.colorProperty)
                addNode("Font Family", observableArrayList(Font.getFamilies()), viewModel.fontProperty)
            }.apply {
                padding = Insets(10.0)
            }
        )

        bottom = HBox(
            Button("Save").apply {
                setOnAction {
                    viewModel.commit()
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}