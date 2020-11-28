package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.function.controller.WaveletFunctionFragmentController
import ru.nstu.grin.concatenation.function.model.WaveletFunctionFragmentModel
import tornadofx.*
import java.util.*

class WaveletFunctionFragment : Fragment() {
    private val model: WaveletFunctionFragmentModel by inject()
    private val controller: WaveletFunctionFragmentController = find(params = params) { }
    val functionId: UUID by param()

    override val root: Parent = form {
        fieldset {
            field("Тип вейвлета") {
                combobox(model.waveletTransformFunProperty, WaveletTransformFun.values().toList())
            }
            field("Какую ось преобразовывать") {
                combobox(model.waveletDirectionProperty, WaveletDirection.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            WaveletDirection.X -> {
                                "Ось абсцисс"
                            }
                            WaveletDirection.Y -> {
                                "Ось ординат"
                            }
                            WaveletDirection.BOTH -> {
                                "Обе оси"
                            }
                        }
                    }
                }
            }
        }
        button("Применить вейвлет преобразование") {
            action {
                controller.enableWavelet()
                close()
            }
        }
    }
}