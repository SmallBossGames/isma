package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.FunctionIntegrationController
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.FunctionIntegrationFragmentModel
import ru.nstu.grin.concatenation.function.model.IntegrationMethod
import tornadofx.*

class FunctionIntegrationFragment : Fragment() {
    private val model = FunctionIntegrationFragmentModel(params["function"] as ConcatenationFunction)
    private val controller: FunctionIntegrationController by inject()

    override val root: Parent = form {
        fieldset {
            field("Метод интегрирования") {
                combobox(model.integrationMethodProperty, IntegrationMethod.values().toList()) {
                    cellFormat { cell ->
                        text = when (cell!!) {
                            IntegrationMethod.TRAPEZE -> "Метод трапеций"
                        }
                    }
                }
            }

            field("Левая граница интеграла") {
                textfield(model.leftBorderProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Правая граница интеграла") {
                textfield(model.rightBorderProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
        }
        button("Найти интеграл") {
            //enableWhen(model.isValid.toProperty())
            action {
                controller.findIntegral(model)
                close()
            }
        }
    }
}