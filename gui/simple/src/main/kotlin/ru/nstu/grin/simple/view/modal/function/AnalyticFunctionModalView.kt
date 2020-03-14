package ru.nstu.grin.simple.view.modal.function

import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.simple.controller.function.AnalyticFunctionController
import ru.nstu.grin.simple.model.function.AnalyticFunctionModel
import tornadofx.*

class AnalyticFunctionModalView : View() {
    private val model: AnalyticFunctionModel by inject()
    private val controller: AnalyticFunctionController by inject()

    override val root: Parent = form {
        fieldset("Функция") {
            field("Имя") {
                textfield().bind(model.nameProperty)
            }
            field("Цвет") {
                colorpicker().bind(model.colorProperty)
            }
        }
        fieldset("Введите формулу") {
            field("формула") {
                textfield().bind(model.textProperty)
            }
            field("Delta") {
                textfield().bind(model.deltaProperty)
            }
        }
        button("OK") {
            enableWhen {
                model.valid
            }
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            action {
                controller.addFunction()
                close()
            }
        }
    }
}