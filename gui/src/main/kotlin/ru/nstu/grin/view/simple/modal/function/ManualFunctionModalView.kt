package ru.nstu.grin.view.simple.modal.function

import javafx.scene.Parent
import ru.nstu.grin.controller.simple.function.ManualFunctionController
import ru.nstu.grin.model.simple.function.ManualFunctionModel
import tornadofx.View
import tornadofx.form

class ManualFunctionModalView : View() {
    private val model: ManualFunctionModel by inject()
    private val controller: ManualFunctionController by inject()

    override val root: Parent = form {

    }
}