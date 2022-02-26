package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.events.FunctionQuery
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.*

class ChangeFunctionFragment : Fragment() {
    private val function: ConcatenationFunction by param()
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
                textfield(model.lineSizeProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Отображать ли функцию") {
                checkbox().bind(model.isHideProperty)
            }
            field("Зеркларовать по x") {
                checkbox().bind(model.isMirrorXProperty)
            }
            field("Зеркалировать по y") {
                checkbox().bind(model.isMirrorYProperty)
            }
            field("Вид линии") {
                combobox(model.lineTypeProperty, LineType.values().toList()) {
                    cellFormat {
                        text = when (it!!) {
                            LineType.POLYNOM -> "Полином"
                            LineType.RECT_FILL_DOTES -> "Прямоугольник заполненные точки"
                            LineType.SEGMENTS -> "Сегменты"
                            LineType.RECT_UNFIL_DOTES -> "Прямоуголник незаполненные точки"
                            LineType.CIRCLE_FILL_DOTES -> "Круг заполненные точки"
                            LineType.CIRCLE_UNFILL_DOTES -> "Круг незаполненные точки"
                        }
                    }
                }
            }
            button("Сохранить") {
                enableWhen(model.isValid.toProperty())
                action {
                    controller.updateFunction()
                    close()
                }
            }
        }
    }

    init {
        fire(FunctionQuery(function.id))
    }
}