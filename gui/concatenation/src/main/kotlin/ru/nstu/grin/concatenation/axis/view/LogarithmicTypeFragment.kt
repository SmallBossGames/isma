package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.axis.controller.LogarithmicTypeFragmentController
import ru.nstu.grin.concatenation.axis.events.AxisQuery
import ru.nstu.grin.concatenation.axis.events.GetAxisEvent
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.*
import java.util.*

class LogarithmicTypeFragment : Fragment() {
    val axisId: UUID by param()
    private val model: LogarithmicTypeModel by inject()
    private val controller: LogarithmicTypeFragmentController = find(params = params) { }

    override val root: Parent = form {
        fieldset {
            field("Основание логарифма") {
                textfield().bind(model.logarithmBaseProperty)
            }
            field("Только целые значение степеней") {
                checkbox().bind(model.onlyIntegerPowProperty)
            }
            field("Целочисленные шаг") {
                textfield().bind(model.integerStepProperty)
            }
        }
    }

    init {
        fire(AxisQuery(axisId))
    }
}