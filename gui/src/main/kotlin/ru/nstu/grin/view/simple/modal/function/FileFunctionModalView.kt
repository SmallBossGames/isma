package ru.nstu.grin.view.simple.modal.function

import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import ru.nstu.grin.controller.simple.function.FileFunctionController
import ru.nstu.grin.model.simple.function.FileFunctionModel
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
                        val files = chooseFile(
                            "Файл",
                            arrayOf(FileChooser.ExtensionFilter("Путь к файлу", "*.gf")),
                            FileChooserMode.Single
                        )
                        if (files.isNotEmpty()) {
                            val file = files[0]
                            model.filePath = file.path
                        }
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
                if (model.filePath.isEmpty()) {
                    error("Необходимо выбрать файл")
                    return@action
                }
                close()
            }
        }
    }
}