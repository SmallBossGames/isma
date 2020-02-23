package ru.nstu.grin.view.simple.modal.function

import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.controller.simple.function.AnalyticFunctionController
import ru.nstu.grin.model.simple.function.AnalyticFunctionModel
import tornadofx.*

class AnalyticFunctionModalView : View() {
    private val model: AnalyticFunctionModel by inject()
    private val controller: AnalyticFunctionController by inject()

    override val root: Parent = form {
        fieldset("Функция") {
            field("Имя") {
                textfield().bind(model.functionNameProperty)
            }
            field("Цвет") {
                textfield().bind(model.functionColorProperty)
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