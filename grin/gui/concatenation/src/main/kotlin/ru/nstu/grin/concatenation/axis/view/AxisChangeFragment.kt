package ru.nstu.grin.concatenation.axis.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentViewModel
import ru.nstu.grin.concatenation.axis.model.AxisScalingType
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.model.MarksDistanceType

class AxisChangeFragment(
    private val viewModel: AxisChangeFragmentViewModel,
) : BorderPane() {
    init {
        val distanceTypes = FXCollections.observableArrayList(MarksDistanceType.values().toList())
        val scalingTypes = FXCollections.observableArrayList(AxisScalingType.values().toList())
        val fontFamilies = FXCollections.observableArrayList(Font.getFamilies())
        val availableDirections = when(viewModel.direction){
            Direction.LEFT, Direction.RIGHT -> FXCollections.observableArrayList(Direction.LEFT, Direction.RIGHT)
            Direction.TOP, Direction.BOTTOM -> FXCollections.observableArrayList(Direction.TOP, Direction.BOTTOM)
        }

        top = VBox(
            propertiesGrid {
                addNode("Name", viewModel.nameProperty)
                addNode("Name", availableDirections, viewModel.directionProperty)
                addNode("Distance between marks", viewModel.distanceBetweenMarksProperty)
                addNode("Distance type", distanceTypes, viewModel.marksDistanceTypeProperty)
                addNode("Border height", viewModel.borderHeightProperty)
                addNode("Font size", viewModel.textSizeProperty)
                addNode("Font", fontFamilies, viewModel.fontProperty)
                addNode("Font color", viewModel.fontColorProperty)
                addNode("Min", viewModel.minProperty)
                addNode("Max", viewModel.maxProperty)
                addNode("Axis Color", viewModel.axisColorProperty)
                addNode("Hide Axis", viewModel.isHideProperty)
                addNode("Scale Mode", scalingTypes, viewModel.markTypeProperty){
                    object : ListCell<AxisScalingType>() {
                        override fun updateItem(item: AxisScalingType?, empty: Boolean) {
                            super.updateItem(item, empty)

                            text = when (item) {
                                AxisScalingType.LINEAR -> "Линейный"
                                AxisScalingType.LOGARITHMIC -> "Логарифмический"
                                else -> null
                            }
                        }
                    }
                }
            }.apply {
                padding = Insets(10.0)
            },
            TabPane()
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