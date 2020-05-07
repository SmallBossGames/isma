package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import tornadofx.*

class AxisChangeFragment : Fragment() {
    private val model: AxisChangeFragmentModel by inject()

    override val root: Parent = form {
        fieldset("Текст") {
            field("Расстояние между метками") {
                textfield().bind(model.distanceBetweenMarksProperty)
            }
            field("Размер шрифта") {
                textfield().bind(model.textSizeProperty)
            }
            field("Шрифт") {
                combobox(model.fontProperty, Font.getFamilies()).bind(model.fontProperty)
            }
            field("Цвет шрифта") {
                colorpicker().bind(model.fontColorProperty)
            }
        }
        fieldset {
            field("Цвет оси") {
                colorpicker().bind(model.axisColorProperty)
            }
        }
    }
}