package ru.nstu.grin.concatenation.function.view

import javafx.stage.Stage
import ru.nstu.grin.concatenation.function.controller.CopyFunctionController
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import tornadofx.*

class CopyFunctionFragment(
    private val model: CopyFunctionModel,
    private val controller: CopyFunctionController
): Form() {
    init {
        fieldset {
            field("Имя новой функции") {
                textfield().bind(model.nameProperty)
            }
        }
        button("Скопировать") {
            action {
                controller.copy(model)
                (scene.window as Stage).close()
            }
        }
    }
}