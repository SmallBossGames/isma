package ru.nstu.grin.concatenation.axis.view

import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import tornadofx.*

class LogarithmicTypeFragment(
    private val model: LogarithmicFragmentModel,
) : Fieldset() {
    init {
        field("Основание логарифма") {
            textfield(model.logarithmBaseProperty)
        }
        field("Только целые значение степеней") {
            checkbox().bind(model.onlyIntegerPowProperty)
        }
        field("Целочисленные шаг") {
            textfield(model.integerStepProperty)
        }
    }
}