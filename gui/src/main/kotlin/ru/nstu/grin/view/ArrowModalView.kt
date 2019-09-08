package ru.nstu.grin.view

import javafx.scene.Parent
import javafx.scene.paint.Color
import ru.nstu.grin.dto.ArrowDTO
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ArrowModalView : View() {
    val x: Double by param()
    val y: Double by param()

    private val model: ArrowDTO = ArrowDTO(Color.WHITE, x, y)

    override val root: Parent = vbox {
        label("color")
        colorpicker {
            setOnAction {
                model.color = this.value
            }
        }

        button("Ok") {
            action {
                println("Lul there is an arrow")
            }
        }
    }
}