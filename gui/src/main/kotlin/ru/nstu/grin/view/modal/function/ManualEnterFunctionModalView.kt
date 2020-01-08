package ru.nstu.grin.view.modal.function

import javafx.collections.FXCollections
import javafx.scene.Parent
import ru.nstu.grin.controller.ManualEnterFunctionController
import ru.nstu.grin.model.Direction
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
                        if (it == null || !POINT_REGEX.matches(it)) {
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
                        if (it == null || !POINT_REGEX.matches(it)) {
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
                val directions = FXCollections.observableArrayList(
                    Direction.values().map { it.name }
                )
                val existDirections = xExistDirections.map {
                    "Направление ${it.direction.name} и функция ${it.functionName}"
                }
                combobox<String>(model.xDirectionProperty, existDirections + directions)
            }
            field("Ось y") {
                val directions = FXCollections.observableArrayList(
                    Direction.values().map { it.name }
                )
                val existDirections = yExistDirections.map {
                    "Направление ${it.direction.name} и функция ${it.functionName}"
                }
                combobox<String>(model.yDirectionProperty, existDirections + directions)
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