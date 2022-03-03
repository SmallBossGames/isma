package ru.nstu.grin.concatenation.cartesian.view

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.cartesian.controller.CopyCartesianController
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianModel

class CopyCartesianFragment(
    private val controller: CopyCartesianController,
    private val model: CopyCartesianModel
) : BorderPane() {
    init {
        top = VBox(
            propertiesGrid {
                addNode("New Space Name", model.nameProperty)
                addNode("X Axis Name", model.xAxisNameProperty)
                addNode("Y Axis Name", model.yAxisNameProperty)
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

