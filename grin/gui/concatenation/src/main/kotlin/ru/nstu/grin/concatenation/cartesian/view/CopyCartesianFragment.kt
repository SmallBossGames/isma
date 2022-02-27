package ru.nstu.grin.concatenation.cartesian.view

import javafx.stage.Stage
import ru.nstu.grin.concatenation.cartesian.controller.CopyCartesianFragmentController
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianModel
import tornadofx.*

class CopyCartesianFragment(
    private val controller: CopyCartesianFragmentController,
    private val model: CopyCartesianModel
) : Form() {
    init {
        fieldset {
            field("Имя нового пространства") {
                textfield().bind(model.nameProperty)
            }
            field("Имя оси абцисс нового пространства") {
                textfield().bind(model.xAxisNameProperty)
            }
            field("Имя оси ординат нового пространства") {
                textfield().bind(model.yAxisNameProperty)
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

