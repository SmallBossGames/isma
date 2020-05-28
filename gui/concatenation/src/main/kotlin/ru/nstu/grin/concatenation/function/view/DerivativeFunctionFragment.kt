package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.DerivativeFunctionController
import ru.nstu.grin.concatenation.function.model.DerivativeFunctionModel
import ru.nstu.grin.concatenation.function.model.DerivativeType
import tornadofx.*
import java.util.*

class DerivativeFunctionFragment : Fragment() {
    private val model: DerivativeFunctionModel by inject()
    private val controller: DerivativeFunctionController = find(params = params) { }
    val functionId: UUID by param()

    override val root: Parent = form {
        fieldset {
            field("Степень") {
                textfield(model.degreeProperty) {
                    validator {
                        if (it?.toIntOrNull() == null || it?.toIntOrNull() ?: -1 < 0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Тип производной") {
                combobox(model.derivativeTypeProperty, DerivativeType.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            DerivativeType.LEFT -> "Левая"
                            DerivativeType.RIGHT -> "Правая"
                            DerivativeType.BOTH -> "Обе"
                        }
                    }
                }
            }
            button("Найти производную") {
                enableWhen(model.isValid.toProperty())
                action {
                    controller.enableDerivative()
                    close()
                }
            }
        }
    }
}