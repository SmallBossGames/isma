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

class AxisChangeFragment(
    private val model: AxisChangeFragmentModel,
    private val controller: AxisChangeFragmentController,
    private val logFragment: LogarithmicTypeFragment
) : BorderPane() {
    init {
        top = VBox(
            propertiesGrid {
                addNode("Distance between marks", model.distanceBetweenMarksProperty)
                addNode("Font size", model.textSizeProperty)
                addNode("Font", FXCollections.observableArrayList( Font.getFamilies()), model.fontProperty)
                addNode("Font color", model.fontColorProperty)
                addNode("Min", model.minProperty)
                addNode("Max", model.maxProperty)
                addNode("Axis Color", model.axisColorProperty)
                addNode("Hide Axis", model.isHideProperty)
                addNode("Scale Mode", FXCollections.observableArrayList(AxisScalingType.values().toList()), model.markTypeProperty){
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
                    controller.updateAxis()
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }

        //TODO: disabled until log axis completely implemented
        /*tabpane {
            model.markTypeProperty.onChange {
                when (model.axisMarkType!!) {
                    AxisMarkType.LINEAR -> {
                        hide()
                    }
                    AxisMarkType.LOGARITHMIC -> {
                        show()
                        (scene.window as Stage).apply {
                            height = 600.0
                        }
                    }
                }
            }
            when (model.axisMarkType!!) {
                AxisMarkType.LINEAR -> {
                    hide()
                }
                AxisMarkType.LOGARITHMIC -> {
                    show()
                }
            }

            tabs.addAll(
                Tab(null, logFragment)
            )

            tabMaxHeight = 0.0
            tabMinHeight = 0.0
            stylesheet {
                Stylesheet.tabHeaderArea {
                    visibility = FXVisibility.HIDDEN
                }
            }
        }*/
    }
}