package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import tornadofx.*

class LogarithmicTypeFragment(
    private val model: LogarithmicFragmentModel,
) : Fragment() {
    override val root: Parent = fieldset {
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