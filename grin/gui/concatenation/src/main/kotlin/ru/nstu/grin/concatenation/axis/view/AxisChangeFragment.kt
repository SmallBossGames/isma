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
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.AxisScalingType
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.model.MarksDistanceType

class AxisChangeFragment(
    private val model: AxisChangeFragmentModel,
    private val controller: AxisChangeFragmentController
) : BorderPane() {
    init {
        val distanceTypes = FXCollections.observableArrayList(MarksDistanceType.values().toList())
        val scalingTypes = FXCollections.observableArrayList(AxisScalingType.values().toList())
        val fontFamilies = FXCollections.observableArrayList(Font.getFamilies())
        val availableDirections = when(model.direction){
            Direction.LEFT, Direction.RIGHT -> FXCollections.observableArrayList(Direction.LEFT, Direction.RIGHT)
            Direction.TOP, Direction.BOTTOM -> FXCollections.observableArrayList(Direction.TOP, Direction.BOTTOM)
        }

        top = VBox(
            propertiesGrid {
                addNode("Name", model.nameProperty)
                addNode("Name", availableDirections, model.directionProperty)
                addNode("Distance between marks", model.distanceBetweenMarksProperty)
                addNode("Distance type", distanceTypes, model.marksDistanceTypeProperty)
                addNode("Border height", model.borderHeightProperty)
                addNode("Font size", model.textSizeProperty)
                addNode("Font", fontFamilies, model.fontProperty)
                addNode("Font color", model.fontColorProperty)
                addNode("Min", model.minProperty)
                addNode("Max", model.maxProperty)
                addNode("Axis Color", model.axisColorProperty)
                addNode("Hide Axis", model.isHideProperty)
                addNode("Scale Mode", scalingTypes, model.markTypeProperty){
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
                    controller.updateAxis(model)
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}