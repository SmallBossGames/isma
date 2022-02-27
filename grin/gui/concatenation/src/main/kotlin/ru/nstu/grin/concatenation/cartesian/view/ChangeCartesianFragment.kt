package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.cartesian.controller.ChangeCartesianController
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel
import tornadofx.*

class ChangeCartesianFragment(
    private val model: ChangeCartesianSpaceModel,
    private val controller: ChangeCartesianController,
) : Fragment() {

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