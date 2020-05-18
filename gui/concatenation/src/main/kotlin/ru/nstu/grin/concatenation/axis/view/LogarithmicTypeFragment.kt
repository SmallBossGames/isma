package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.*

class LogarithmicTypeFragment : Fragment() {
    private val model: LogarithmicTypeModel by inject()

    override val root: Parent = form {
        fieldset {
            field("Основание логарифма") {
                textfield().bind(model.logarithmBaseProperty)
            }
            field("Только целые значение степеней") {
                textfield().bind(model.onlyIntegerPowProperty)
            }
        }
    }
}