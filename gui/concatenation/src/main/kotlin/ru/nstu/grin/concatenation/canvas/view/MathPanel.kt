package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.function.view.DerivativeFunctionFragment
import ru.nstu.grin.concatenation.function.view.IntersectionFunctionFragment
import tornadofx.*

class MathPanel : Fragment() {
    private val model: ConcatenationCanvasModelViewModel by inject()
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
                    val derivativeDetails = function.getDerivativeDetails()
                    if (derivativeDetails != null) {
                        function.removeDerivativeDetails()
                        drawer.draw()
                        return@action
                    }
                    find<DerivativeFunctionFragment>(
                        mapOf(
                            DerivativeFunctionFragment::functionId to function.id
                        )
                    ).openModal()
                }
            }
        }
    }
}