package ru.nstu.grin.concatenation.view.modal

import javafx.scene.Parent
import ru.nstu.grin.common.controller.DescriptionModalController
import ru.nstu.grin.common.model.view.DescriptionViewModel
import tornadofx.*

class DescriptionModalView : Fragment() {
    val x: Double by param()
    val y: Double by param()

    private val model: DescriptionViewModel by inject()
    private val controller: DescriptionModalController by inject()

    init {
        println("x=$x, y=$y")
        model.x = x
        model.y = y
    }

    override val root: Parent = form {
        fieldset {
            field("Текст") {
                textfield().bind(model.textProperty)
            }
            field("Размер текста") {
                textfield().bind(model.sizeProperty)
            }
            field("Цвет текста") {
                colorpicker().bind(model.textColorProperty)
            }
        }
        button("Готово") {
            enableWhen {
                model.valid
            }
            action {
                controller.addDescription()
                close()
            }
        }
    }
}