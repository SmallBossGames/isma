package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.cartesian.model.CartesianCopyDataModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianModel
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService
import tornadofx.*

class CopyCartesianFragment : Fragment() {
    val space: CartesianSpace by param()
    private val model: CopyCartesianModel by inject()
    private val cartesianCanvasService: CartesianCanvasService by inject()

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
        button("Скопировать") {
            action {
                val data = CartesianCopyDataModel(
                    origin = space,
                    name = model.name,
                    xAxisName = model.xAxisName,
                    yAxisName = model.yAxisName
                )
                cartesianCanvasService.copyCartesian(data)
                close()
            }
        }
    }
}