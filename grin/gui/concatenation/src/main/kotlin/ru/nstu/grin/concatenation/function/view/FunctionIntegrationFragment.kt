package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.FunctionIntegrationController
import ru.nstu.grin.concatenation.function.events.FunctionQuery
import ru.nstu.grin.concatenation.function.model.FunctionIntegrationFragmentModel
import ru.nstu.grin.concatenation.function.model.IntegrationMethod
import tornadofx.*
import java.util.*

class FunctionIntegrationFragment : Fragment() {
    val functionId: UUID by param()
    private val model: FunctionIntegrationFragmentModel by inject()
    private val controller: FunctionIntegrationController = find(params = params) { }

    override val root: Parent = form {
        fieldset {
            field("Метод интегрирования") {
                combobox(model.integrationMethodProperty, IntegrationMethod.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            IntegrationMethod.TRAPEZE -> "Метод трапеций"
                        }
                    }
                }
            }

            field("Левая граница интеграла") {
                textfield(model.leftBorderProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || it.toDoubleOrNull() ?: -1.0 < 0.0) {
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
                        if (it?.toDoubleOrNull() == null || it.toDoubleOrNull() ?: -1.0 < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
        }
        button("Найти интеграл") {
            enableWhen(model.isValid.toProperty())
            action {
                controller.findIntegral()
                close()
            }
        }
    }

    init {
        fire(FunctionQuery(functionId))
    }
}