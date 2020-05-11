package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.cartesian.events.CartesianCopyQuery
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianModel
import tornadofx.*
import java.util.*

class CopyCartesianFragment : Fragment() {
    val cartesianId: UUID by param()
    private val model: CopyCartesianModel by inject()

    override val root: Parent = form {
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
        button("Ок") {
            action {
                val event = CartesianCopyQuery(
                    id = cartesianId,
                    name = model.name,
                    xAxisName = model.xAxisName,
                    yAxisName = model.yAxisName
                )
                fire(event)
                close()
            }
        }
    }
}