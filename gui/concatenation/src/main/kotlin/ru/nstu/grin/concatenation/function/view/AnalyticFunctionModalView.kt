package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.concatenation.function.controller.AnalyticFunctionController
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.function.model.AnalyticFunctionModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class AnalyticFunctionModalView : AbstractAddFunctionModal() {
    private val controller: AnalyticFunctionController by inject()
    private val model: AnalyticFunctionModel by inject()

    override val root: Parent = form {
        fieldset {
            field("Введите имя функции") {
                textfield().bind(model.functionNameProperty)
            }
            field("Шаг рисования") {
                textfield().bind(model.stepProperty)
            }
        }
        fieldset("Введите формулу") {
            field("формула") {
                textfield().bind(model.textProperty)
            }
            field("Delta") {
                textfield().bind(model.deltaProperty)
            }
        }
        fieldset("Ось x") {
            field("Имя") {
                textfield().bind(model.xAxisNameProperty)
            }
            field("Напрвление") {
                val default = Direction.values().map {
                    ExistDirection(
                        it,
                        null
                    )
                }
                val existDirections = xExistDirections
                combobox(model.xDirectionProperty, default + existDirections) {
                    cellFormat {
                        text = if (it.functionName != null) {
                            "Напрвление ${it.direction.name}, функция ${it.functionName}"
                        } else {
                            it.direction.name
                        }
                    }
                }
            }
        }
        fieldset("Ось y") {
            field("Имя") {
                textfield().bind(model.yAxisNameProperty)
            }
            field("Направление") {
                val default = Direction.values().map {
                    ExistDirection(
                        it,
                        null
                    )
                }
                val existDirections = yExistDirections
                combobox(model.yDirectionProperty, default + existDirections) {
                    cellFormat {
                        text = if (it.functionName != null) {
                            "Напрвление ${it.direction.name}, функция ${it.functionName}"
                        } else {
                            it.direction.name
                        }
                    }
                }
            }
        }
        fieldset("Цвета") {
            field("Цвет функций") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Цвет x оси") {
                colorpicker().bind(model.xAxisColorProperty)
            }
            field("Цвет дельт оси x") {
                colorpicker().bind(model.xDelimeterColorProperty)
            }
            field("Цвет y оси") {
                colorpicker().bind(model.yAxisColorProperty)
            }
            field("Цвте дельт оси y") {
                colorpicker().bind(model.yDelimiterColorProperty)
            }
        }
        button("OK") {
            enableWhen {
                model.valid
            }

            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            action {
                controller.addFunction(drawSize)
                close()
            }
        }
    }
}