package ru.nstu.grin.concatenation.function.view

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.controller.CopyFunctionController
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel

class CopyFunctionFragment(
    private val model: CopyFunctionModel,
    private val controller: CopyFunctionController
): BorderPane() {
    init {
        top = VBox(
            propertiesGrid {
                addNode("New Function Name", model.nameProperty)
            }.apply {
                padding = Insets(10.0)
            },
        )

        bottom = HBox(
            Button("Copy").apply {
                setOnAction {
                    controller.copy(model)
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}