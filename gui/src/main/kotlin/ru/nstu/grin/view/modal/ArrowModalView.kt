package ru.nstu.grin.view.modal

import javafx.scene.Parent
import ru.nstu.grin.controller.ArrowController
import ru.nstu.grin.view.model.ArrowViewModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ArrowModalView : Fragment() {
    val x: Double by param()
    val y: Double by param()

    private val model: ArrowViewModel by inject()
    private val controller: ArrowController by inject()

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