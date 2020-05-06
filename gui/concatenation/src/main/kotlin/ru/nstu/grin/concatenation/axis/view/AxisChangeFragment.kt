package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import tornadofx.*

class AxisChangeFragment : Fragment() {
    override val root: Parent = form {
        fieldset("Текст") {
            field("Расстояние между метками") {
                textfield()
            }
            field("Размер шрифта") {
                textfield()
            }
            field("Шрифт") {

            }
            field("Цвет шрифта") {

            }
        }
        fieldset {
            field("Цвет оси") {

            }
        }
    }


}