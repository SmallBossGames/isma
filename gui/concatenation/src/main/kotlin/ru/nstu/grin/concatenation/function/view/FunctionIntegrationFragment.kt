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
                textfield().bind(model.leftBorderProperty)
            }
            field("Правая граница интеграла") {
                textfield().bind(model.rightBorderProperty)
            }
        }
        button("Ок") {
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