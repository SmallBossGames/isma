package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.*
import java.util.*

class ChangeFunctionFragment : Fragment() {
    val functionId: UUID by param()
    private val model: ChangeFunctionModel by inject()
    private val controller: ChangeFunctionController = find(
        params = params
    )

    override val root: Parent = form {
        fieldset {
            field("Имя") {
                textfield().bind(model.nameProperty)
            }
            field("Цвет функции") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Размер линии") {
                textfield().bind(model.lineSizeProperty)
            }
            field("Вид линии") {
                combobox(model.lineTypeProperty, LineType.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            LineType.POLYNOM -> "Полином"
                            LineType.DOTES -> "Точки"
                            LineType.SEGMENTS -> "Отрезки"
                        }
                    }
                }
            }
            button("Ок") {
                action {
                    controller.updateFunction()
                    close()
                }
            }
        }
    }
}