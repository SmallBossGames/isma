package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.stage.Stage
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.*

class ChangeFunctionFragment(
    private val model: ChangeFunctionModel,
    private val controller: ChangeFunctionController,
) : Form() {
    init {
        fieldset {
            field("Имя") {
                textfield().bind(model.nameProperty)
            }
            field("Цвет функции") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Размер линии") {
                textfield(model.lineSizeProperty)
            }
            field("Отображать ли функцию") {
                checkbox().bind(model.isHideProperty)
            }
            field("Зеркларовать по x") {
                checkbox().bind(model.isMirrorXProperty)
            }
            field("Зеркалировать по y") {
                checkbox().bind(model.isMirrorYProperty)
            }
            field("Вид линии") {
                val types = FXCollections.observableArrayList(LineType.values().toList())
                val comboBox = ComboBox(types).apply {
                    buttonCell = createCell()
                    setCellFactory {
                        createCell()
                    }
                    valueProperty().bindBidirectional(model.lineTypeProperty)
                }
                add(comboBox)
            }
            button("Сохранить") {
                //enableWhen(model.isValid.toProperty())
                action {
                    controller.updateFunction(model)
                    (scene.window as Stage).close()
                }
            }
        }
    }

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