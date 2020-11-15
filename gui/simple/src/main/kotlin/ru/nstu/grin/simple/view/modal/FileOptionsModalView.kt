package ru.nstu.grin.simple.view.modal

import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.simple.controller.FileOptionsController
import ru.nstu.grin.simple.model.FileOptionsModel
import tornadofx.*

class FileOptionsModalView : View() {
    private val model: FileOptionsModel by inject(params = params)
    private val controller: FileOptionsController by inject(params = params)

    override val root: Parent = form {
        fieldset("Параметры файла") {
            field("Введите разделитель") {
                textfield().bind(model.delimiterProperty)
            }
        }
        button("ОК") {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS

            action {
                controller.openFile()
                close()
            }
        }
    }
}