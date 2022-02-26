package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.view.DerivativeFunctionFragment
import ru.nstu.grin.concatenation.function.view.FunctionIntegrationFragment
import ru.nstu.grin.concatenation.function.view.IntersectionFunctionFragment
import ru.nstu.grin.concatenation.function.view.WaveletFunctionFragment
import tornadofx.*

class MathPanel : Fragment() {
    private val model: ConcatenationCanvasModel by inject()
    private val drawer: ConcatenationChainDrawer by inject()

    override val root: Parent = toolbar {
        button {
            val image = Image("intersection.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Найти пересечения")

            action {
                find<IntersectionFunctionFragment>().openModal()
            }
        }

        button {
            val image = Image("derivative.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Применить производную")

            action {
                val function = model.getSelectedFunction()
                if (function != null) {
                    val derivativeDetails = function.derivativeDetails
                    if (derivativeDetails != null) {
                        function.derivativeDetails = null
                        drawer.draw()
                        return@action
                    }
                    find<DerivativeFunctionFragment>(
                        mapOf(
                            "function" to function
                        )
                    ).openModal()
                }
            }
        }
        button {
            val image = Image("wavelet.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Применить вейвлет преобразование")

            action {
                val function = model.getSelectedFunction()
                if (function != null) {
                    val waveletDetails = function.waveletDetails
                    if (waveletDetails != null) {
                        function.waveletDetails = null
                        drawer.draw()
                        return@action
                    }
                    find<WaveletFunctionFragment>(
                        mapOf(
                            "function" to function
                        )
                    ).openModal()
                }
            }
        }
        button {
            val image = Image("integral.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Найти сумму интегралла")

            action {
                val function = model.getSelectedFunction()
                if (function != null) {
                    find<FunctionIntegrationFragment>(
                        mapOf(FunctionIntegrationFragment::functionId to function.id)
                    ).openModal()
                }
            }
        }
    }
}