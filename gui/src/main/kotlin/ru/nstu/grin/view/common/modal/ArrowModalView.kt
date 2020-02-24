package ru.nstu.grin.view.common.modal

import javafx.scene.Parent
import ru.nstu.grin.controller.ArrowController
import ru.nstu.grin.model.CanvasType
import ru.nstu.grin.model.view.ArrowViewModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ArrowModalView : Fragment() {
    val type: CanvasType by param()
    val x: Double by param()
    val y: Double by param()

    private val model: ArrowViewModel by inject()
    private val controller: ArrowController = find(mapOf(ArrowController::type to type))

    init {
        println("x=$x y=$y")
        model.x = x
        model.y = y
    }

    override val root: Parent = vbox {
        label("color")
        colorpicker {
            colorpicker().bind(model.arrowColorProperty)
        }

        button("Ok") {
            action {
                controller.sendArrow()
                close()
            }
        }
    }
}