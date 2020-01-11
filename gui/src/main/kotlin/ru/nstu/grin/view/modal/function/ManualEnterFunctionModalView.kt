package ru.nstu.grin.view.modal.function

import javafx.scene.Parent
import ru.nstu.grin.controller.ManualEnterFunctionController
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.ExistDirection
import ru.nstu.grin.model.view.function.ManualEnterFunctionViewModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionModalView : AbstractAddFunctionModal() {
    private val controller: ManualEnterFunctionController by inject()
    private val model: ManualEnterFunctionViewModel by inject()

    override val root: Parent = form {
        fieldset {
            field("Введите имя функции") {
                textfield(model.functionNameProperty) {
                    validator {
                        if (it.isNullOrBlank()) {
                            error("Иия функции не может быть пустым")
                        } else {
                            null
                        }
                    }
                }
            }
        }
        fieldset("Введите ниже точки") {
            field("Точки x") {
                textfield(model.xPointsProperty) {
                    validator {
                        if (it == null) {
                            error("Введите цифры в следующем формате 22.3, 23.4")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Точки y") {
                textfield(model.yPointsProperty) {
                    validator {
                        if (it == null) {
                            error("Введите цифры в следующем формате 22.3, 23.4")
                        } else {
                            null
                        }
                    }
                }
            }
        }
        fieldset("Направления осей") {
            field("Ось x") {
                val default = Direction.values().map { ExistDirection(it, null) }
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
            field("Ось y") {
                val default = Direction.values().map { ExistDirection(it, null) }
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
            enableWhen(model.valid)

            action {
                if (!checkSize()) {
                    error("Размеры должны быть эквиваленты")
                    return@action
                }
                val points = controller.parsePoints()
                controller.addFunction(points = points, drawSize = drawSize)
                close()
            }
        }
    }

    private fun checkSize(): Boolean {
        return model.yPoints.split(",").size == model.xPoints.split(",").size
    }

    companion object {
        private val POINT_REGEX = Regex("(\\d+.\\d+,\\s*)*\\d+.\\d+\\s*")
    }
}