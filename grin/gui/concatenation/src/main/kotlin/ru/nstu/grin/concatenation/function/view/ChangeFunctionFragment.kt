package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.LineType

class ChangeFunctionFragment(
    private val model: ChangeFunctionModel,
    private val controller: ChangeFunctionController,
) : BorderPane() {
    init {
        val types = FXCollections.observableArrayList(LineType.values().toList())

        top = propertiesGrid {
            addNode("Name", model.nameProperty)
            addNode("Color", model.functionColorProperty)
            addNode("Thickness", model.lineSizeProperty)
            addNode("Hide", model.isHideProperty)
            addNode("Mirror X", model.isMirrorXProperty)
            addNode("Mirror Y", model.isMirrorYProperty)
            addNode("Line Type", types, model.lineTypeProperty) {
                createCell()
            }
        }.apply {
            padding = Insets(10.0)
        }

        bottom = HBox(
            Button("Save").apply {
                setOnAction {
                    controller.updateFunction(model)
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }

    companion object {
        @JvmStatic
        private fun createCell() = object : ListCell<LineType>() {
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

}