package ru.nstu.grin.simple.view.modal.function

import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.simple.controller.function.FileFunctionController
import ru.nstu.grin.simple.model.function.FileFunctionModel
import tornadofx.*

class FileFunctionModalView : View() {
    private val model: FileFunctionModel by inject()
    private val controller: FileFunctionController by inject()

    override val root: Parent = form {
        fieldset("Функция") {
            field("Имя") {
                textfield().bind(model.nameProperty)
            }
            field("Цвет") {
                colorpicker().bind(model.colorProperty)
            }
            field("Выберите файл") {
                button("Файл") {
                    action {
                        controller.chooseFile()
                    }
                }
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