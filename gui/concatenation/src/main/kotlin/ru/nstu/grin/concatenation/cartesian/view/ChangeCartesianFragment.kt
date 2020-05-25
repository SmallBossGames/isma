package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.cartesian.controller.ChangeCartesianController
import ru.nstu.grin.concatenation.cartesian.events.CartesianQuery
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel
import tornadofx.*
import java.util.*

class ChangeCartesianFragment : Fragment() {
    val cartesianId: UUID by param()
    private val model: ChangeCartesianSpaceModel by inject()
    private val controller: ChangeCartesianController = find(params = params) { }

    override val root: Parent = form {
        fieldset {
            field("Имя") {
                textfield().bind(model.nameProperty)
            }
            field("Отображать сетку") {
                checkbox().bind(model.isShowGridProperty)
            }
        }
        button("Ок") {
            action {
                controller.updateCartesianSpace()
                close()
            }
        }
    }

    init {
        fire(CartesianQuery(cartesianId))
    }
}