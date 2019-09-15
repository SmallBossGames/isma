package ru.nstu.grin.view

import javafx.scene.Parent
import ru.nstu.grin.controller.ManualEnterFunctionController
import ru.nstu.grin.model.ManualEnterFunctionModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionView : View() {

    private val model: ManualEnterFunctionModel by inject()
    private val controller: ManualEnterFunctionController by inject()

    override val root: Parent = vbox {
        hbox {
            label("Введите ниже точки:")
        }
        textarea {
            textProperty().bindBidirectional(model.textProperty)
        }
        button("OK") {
            action {
                val function = controller.parseFunction()
                controller.addFunction(function)
                closeModal()
            }
        }

    }
}