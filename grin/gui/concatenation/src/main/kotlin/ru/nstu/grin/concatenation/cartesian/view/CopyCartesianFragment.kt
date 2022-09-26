package ru.nstu.grin.concatenation.cartesian.view

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianViewModel

class CopyCartesianFragment(
    private val viewModel: CopyCartesianViewModel
) : BorderPane() {
    init {
        top = VBox(
            propertiesGrid {
                addNode("New Space Name", viewModel.nameProperty)
                addNode("X Axis Name", viewModel.xAxisNameProperty)
                addNode("Y Axis Name", viewModel.yAxisNameProperty)
            }.apply {
                padding = Insets(10.0)
            },
        )

        bottom = HBox(
            Button("Copy").apply {
                setOnAction {
                    viewModel.copy()
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}

