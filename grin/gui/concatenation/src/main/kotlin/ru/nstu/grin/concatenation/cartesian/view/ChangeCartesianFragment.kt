package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.cartesian.controller.ChangeCartesianController
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel
import tornadofx.*

class ChangeCartesianFragment : Fragment() {
    private val model: ChangeCartesianSpaceModel by inject(params = params)
    private val controller: ChangeCartesianController by inject()

    override val root: Parent = form {
        fieldset {
            field("Имя") {
                textfield().bind(model.nameProperty)
            }
            field("Отображать сетку") {
                checkbox().bind(model.isShowGridProperty)
            }
        }
        button("Сохранить") {
            action {
                controller.updateCartesianSpace(model)
                close()
            }
        }
    }
}