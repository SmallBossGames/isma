package ru.nstu.grin.concatenation.description.view

import javafx.scene.Parent
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.description.controller.ChangeDescriptionController
import ru.nstu.grin.concatenation.description.events.GetDescriptionQuery
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionModel
import tornadofx.*
import java.util.*

class ChangeDescriptionFragment : Fragment() {
    val descriptionId: UUID by param()
    private val model: ChangeDescriptionModel by inject()
    private val controller: ChangeDescriptionController = find { }

    override val root: Parent = form {
        fieldset {
            field("Текст") {
                textfield().bind(model.textProperty)
            }
            field("Размер шрифта") {
                textfield().bind(model.textSizeProperty)
            }
            field("Цвет шрифта") {
                colorpicker().bind(model.colorProperty)
            }
            field("Семейство шрифта") {
                combobox(model.fontProperty, Font.getFamilies())
            }
        }
        button("Ок") {
            controller.updateDescription()
        }
    }

    init {
        fire(GetDescriptionQuery(descriptionId))
    }
}