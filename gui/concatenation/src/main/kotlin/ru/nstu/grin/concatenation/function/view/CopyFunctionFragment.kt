package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.CopyFunctionController
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import tornadofx.*
import java.util.*

class CopyFunctionFragment : Fragment() {
    val functionId: UUID by param()
    private val model: CopyFunctionModel by inject()
    private val controller: CopyFunctionController by inject(params = params)

    override val root: Parent = form {
        fieldset {
            field("Имя новой функции") {
                textfield().bind(model.nameProperty)
            }
        }
        button("Ок") {
            action {
                controller.copy()
                close()
            }
        }
    }
}