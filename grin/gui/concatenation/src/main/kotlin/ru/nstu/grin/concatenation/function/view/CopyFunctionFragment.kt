package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.CopyFunctionController
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import tornadofx.*

class CopyFunctionFragment : Fragment() {
    private val model: CopyFunctionModel by inject(params = params)
    private val controller: CopyFunctionController by inject(params = params)

    override val root: Parent = form {
        fieldset {
            field("Имя новой функции") {
                textfield().bind(model.nameProperty)
            }
        }
        button("Скопировать") {
            action {
                controller.copy()
                close()
            }
        }
    }
}