package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.model.AnalyticFunctionModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class AnalyticFunctionFragment : Fragment() {
    val model: AnalyticFunctionModel by inject()

    override val root: Parent = form {
        fieldset("Введите формулу") {
            field("формула") {
                textfield().bind(model.textFunctionProperty)
            }
            field("Delta") {
                textfield().bind(model.deltaProperty)
            }
        }
    }
}