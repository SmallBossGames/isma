package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.canvas.events.AxisQuery
import ru.nstu.grin.concatenation.canvas.events.GetAxisEvent
import ru.nstu.grin.concatenation.canvas.events.UpdateAxisEvent
import tornadofx.*
import java.util.*

class AxisChangeFragment : Fragment() {
    val axisId: UUID by param()
    private val model: AxisChangeFragmentModel by inject()
    private val controller: AxisChangeFragmentController by inject(params = params)

    override val root: Parent = form {
        println(controller.params)
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
        button("Ок") {
            action {
                val event = UpdateAxisEvent(
                    id = axisId,
                    distance = model.distanceBetweenMarks.toDouble(),
                    textSize = model.textSize.toDouble(),
                    font = model.font,
                    fontColor = model.fontColor,
                    axisColor = model.axisColor
                )
                fire(event)
                close()
            }
        }
    }

    init {
        fire(AxisQuery(axisId))
    }
}