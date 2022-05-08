package ru.nstu.grin.concatenation.cartesian.view

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceViewModel

class ChangeCartesianFragment(
    private val viewModel: ChangeCartesianSpaceViewModel,
) : BorderPane() {

    init {
        top = VBox(
            propertiesGrid {
                addNode("Name", viewModel.nameProperty)
                addNode("Show Grid", viewModel.isShowGridProperty)
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