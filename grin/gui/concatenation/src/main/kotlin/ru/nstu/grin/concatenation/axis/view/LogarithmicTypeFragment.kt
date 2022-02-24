package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.*

class LogarithmicTypeFragment : Fragment() {
    private val model: LogarithmicTypeModel by inject(params = params)

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
}