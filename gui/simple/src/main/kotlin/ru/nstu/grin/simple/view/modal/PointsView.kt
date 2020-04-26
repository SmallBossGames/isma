package ru.nstu.grin.simple.view.modal

import javafx.scene.Parent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.simple.controller.PointsViewController
import ru.nstu.grin.simple.model.PointsViewModel
import ru.nstu.grin.common.model.WaveletTransformFun
import tornadofx.*

class PointsView : View() {
    private val controller: PointsViewController by inject(params = params)
    private val model: PointsViewModel by inject(params = params)

    override val root: Parent = form {
        controller.readPoints()
        fieldset("Вейвлет преобразование") {
            field("Включить") {
                checkbox().bind(model.isWaveletProperty)
            }
            field("Тип вейвлета") {
                combobox(model.waveletTransformFunProperty, WaveletTransformFun.values().toList()) {
                    enableWhen {
                        model.isWaveletProperty
                    }
                }
            }
            field("Направление преобразования") {
                combobox(model.waveletDirectionProperty, WaveletDirection.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            WaveletDirection.X -> {
                                "Ось абцисс"
                            }
                            WaveletDirection.Y -> {
                                "Ось ординат"
                            }
                            WaveletDirection.BOTH -> {
                                "Обе оси"
                            }
                        }
                    }
                    enableWhen {
                        model.isWaveletProperty
                    }
                }
            }
        }
        tableview(model.pointsProperty) {
            readonlyColumn("x", Point::x)
            readonlyColumn("y", Point::y)
        }
        button("Ok") {
            action {
                controller.fireCheckedEvent()
                close()
            }
        }
    }
}