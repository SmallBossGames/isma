package ru.nstu.grin.concatenation.cartesian.view

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.cartesian.controller.ChangeCartesianController
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel

class ChangeCartesianFragment(
    private val model: ChangeCartesianSpaceModel,
    private val controller: ChangeCartesianController,
) : BorderPane() {

    init {
        top = VBox(
            propertiesGrid {
                addNode("Name", model.nameProperty)
                addNode("Show Grid", model.isShowGridProperty)
            }.apply {
                padding = Insets(10.0)
            }
        )

        bottom = HBox(
            Button("Save").apply {
                setOnAction {
                    controller.updateCartesianSpace(model)
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}