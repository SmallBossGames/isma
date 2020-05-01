package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.FileFragmentController
import tornadofx.*

class FileFunctionFragment : Fragment() {
    private val controller: FileFragmentController by inject()

    override val root: Parent = form {
        fieldset("Работа с файлом") {
            field("Выберите файл") {
                button("Файл") {
                    action {
                        controller.chooseFile()
                    }
                }
            }
        }
    }
}