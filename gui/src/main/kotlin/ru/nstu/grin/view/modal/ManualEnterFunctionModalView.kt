package ru.nstu.grin.view.modal

import javafx.collections.FXCollections
import javafx.scene.Parent
import ru.nstu.grin.model.Direction
import ru.nstu.grin.controller.ManualEnterFunctionController
import ru.nstu.grin.model.view.ManualEnterFunctionViewModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionModalView : View() {
    private val model: ManualEnterFunctionViewModel by inject()
    private val controller: ManualEnterFunctionController by inject()

    override val root: Parent = form {
        fieldset("Введите ниже точки") {
            field("Точки x") {
                textfield(model.xPointsProperty) {
                    validator {
                        if (it == null || !POINT_REGEX.matches(it)) {
                            error("Введите цифры в следующем формате 22.3, 23.4")
                        } else if (it.split(",").size != model.yPoints.split(",").size) {
                            error("Количество точек не совпадает с y")
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
                        } else if (it.split(",").size != model.xPoints.split(",").size) {
                            error("Количество точек не совпадает с x")
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
                combobox<String>(model.xDirectionProperty, directions)
            }
            field("Ось y") {
                val directions = FXCollections.observableArrayList(
                    Direction.values().map { it.name }
                )
                combobox<String>(model.yDirectionProperty, directions)
            }
        }
        fieldset("Цвета") {
            field("Цвет функций") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Цвет x оси") {
                colorpicker().bind(model.xAxisColorProperty)
            }
            field("Цвет y оси") {
                colorpicker().bind(model.yAxisColorProperty)
            }
        }
        button("OK") {
            enableWhen(model.valid)
            action {
                val function = controller.parseFunction()
                controller.addFunction(function = function)
                close()
            }
        }
    }

    companion object {
        private val POINT_REGEX = Regex("(\\d+.\\d+,\\s*)*\\d+.\\d+\\s*")
    }
}