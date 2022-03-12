package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.function.controller.AddFunctionController
import ru.nstu.grin.concatenation.function.model.AddFunctionModel
import ru.nstu.grin.concatenation.function.model.AxisViewModel
import ru.nstu.grin.concatenation.function.model.InputWay
import ru.nstu.grin.concatenation.function.model.LineType

class AddFunctionModalView(
    private val controller: AddFunctionController,
    private val model: AddFunctionModel,
    private val fileFunctionFragment: FileFunctionFragment,
    private val analyticFunctionFragment: AnalyticFunctionFragment,
    private val manualFunctionFragment: ManualFunctionFragment,
) : BorderPane() {
    init {
        val generalView = VBox(
            propertiesGrid {
                addNode("Space Name", model.cartesianSpaceNameProperty)
                addNode("Function Name", model.functionNameProperty)
                addNode("Line Size", model.functionLineSizeProperty)
                addNode("Line Color", model.functionColorProperty)
                addNode("Show As", FXCollections.observableList(LineType.values().toList()), model.functionLineTypeProperty){
                    object : ListCell<LineType>() {
                        override fun updateItem(item: LineType?, empty: Boolean) {
                            super.updateItem(item, empty)

                            text = when (item) {
                                LineType.POLYNOM -> "Полином"
                                LineType.RECT_FILL_DOTES -> "Прямоугольник заполненные точки"
                                LineType.SEGMENTS -> "Сегменты"
                                LineType.RECT_UNFIL_DOTES -> "Прямоуголник незаполненные точки"
                                LineType.CIRCLE_FILL_DOTES -> "Круг заполненные точки"
                                LineType.CIRCLE_UNFILL_DOTES -> "Круг незаполненные точки"
                                else -> null
                            }
                        }
                    }
                }
                addNode("Drawing Step", model.stepProperty)
            }
        ).apply {
            padding = Insets(10.0)
        }

        val inputView = VBox(
            propertiesGrid {
                addNode("Input Type", FXCollections.observableList(InputWay.values().toList()), model.inputWayProperty) {
                    object: ListCell<InputWay>() {
                        override fun updateItem(item: InputWay?, empty: Boolean) {
                            super.updateItem(item, empty)

                            text = when (item) {
                                InputWay.FILE -> "Файл"
                                InputWay.ANALYTIC -> "Аналитически"
                                InputWay.MANUAL -> "Вручную"
                                else -> null
                            }
                        }
                    }
                }
            },
            VBox().apply {

                fun replaceInputWayControl(){
                    when (model.inputWay) {
                        InputWay.FILE -> {
                            children.setAll(fileFunctionFragment)
                        }
                        InputWay.ANALYTIC -> {
                            children.setAll(analyticFunctionFragment)
                        }
                        InputWay.MANUAL -> {
                            children.setAll(manualFunctionFragment)
                        }
                    }
                }

                replaceInputWayControl()

                model.inputWayProperty.addListener { _,_,_ ->
                    replaceInputWayControl()

                    (scene.window as Stage).sizeToScene()
                }
            },
        ).apply {
            padding = Insets(10.0)
        }

        top = TabPane(
            Tab("Input", inputView),
            Tab("General", generalView),
            Tab("X Axis", createAxisView(model.xAxis)),
            Tab("Y Axis", createAxisView(model.yAxis)),
        ).apply {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        }

        bottom = HBox(
            Button("Add").apply {
                setOnAction {
                    controller.addFunction()

                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }

    init {
/*        subscribe<FileCheckedEvent> {
            fileFunctionModel.points = it.points
            fileFunctionModel.addFunctionsMode = it.addFunctionsMode
        }*/
    }

    companion object {
        private fun createAxisView(axisViewModel: AxisViewModel) =
            HBox(
                propertiesGrid {
                    addNode("Name", axisViewModel.nameProperty)
                    addNode("Direction", FXCollections.observableArrayList(Direction.BOTTOM, Direction.TOP), axisViewModel.directionProperty)
                    addNode("Distance Between Marks", axisViewModel.distanceBetweenMarksProperty)
                    addNode("Font", axisViewModel.textSizeProperty)
                    addNode("Font Size", axisViewModel.fontProperty)
                    addNode("Axis Color", axisViewModel.colorProperty)
                    addNode("Delta Axis Color", axisViewModel.delimiterColorProperty)
                }
            ).apply {
                padding = Insets(10.0)
            }
    }
}