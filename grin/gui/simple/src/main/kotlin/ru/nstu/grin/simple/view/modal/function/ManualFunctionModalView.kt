package ru.nstu.grin.simple.view.modal.function

import javafx.scene.Parent
import ru.nstu.grin.simple.controller.function.ManualFunctionController
import ru.nstu.grin.simple.model.function.ManualFunctionModel
import tornadofx.*

class ManualFunctionModalView : View() {
    private val model: ManualFunctionModel by inject()
    private val controller: ManualFunctionController by inject()

    override val root: Parent = form {
        fieldset {
            field("Введите имя функции") {
                textfield(model.nameProperty) {
                    validator {
                        if (it.isNullOrBlank()) {
                            error("Иия функции не может быть пустым")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Цвет функции") {
                colorpicker().bind(model.colorProperty)
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
        button("OK") {
            enableWhen(model.valid)

            action {
                if (!checkSize()) {
                    error("Размеры должны быть эквиваленты")
                    return@action
                }
//                val points = controller.parsePoints()
//                controller.addFunction(points = points, drawSize = drawSize)
                close()
            }
        }
    }

    private fun checkSize(): Boolean {
        return model.yPoints.split(",").size == model.xPoints.split(",").size
    }
}