package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.axis.controller.LogarithmicTypeFragmentController
import ru.nstu.grin.concatenation.axis.events.AxisQuery
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.*
import java.util.*

class LogarithmicTypeFragment : Fragment() {
    val axisId: UUID by param()
    private val model: LogarithmicTypeModel by inject()
    private val controller: LogarithmicTypeFragmentController = find(params = params) { }

    override val root: Parent = fieldset {
        field("Основание логарифма") {
            textfield(model.logarithmBaseProperty) {
                validator {
                    if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                        error("Число должно быть плавающим 20,0 и больше нуля")
                    } else {
                        null
                    }
                }
            }
        }
        field("Только целые значение степеней") {
            checkbox().bind(model.onlyIntegerPowProperty)
        }
        field("Целочисленные шаг") {
            textfield(model.integerStepProperty) {
                validator {
                    if (it?.toIntOrNull() == null || (it.toIntOrNull() ?: -1) < 0) {
                        error("Число должно быть плавающим 20,0 и больше нуля")
                    } else {
                        null
                    }
                }
            }
        }
    }


    init {
        fire(AxisQuery(axisId))
    }
}