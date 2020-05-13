package ru.nstu.grin.concatenation.description.view

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.description.controller.ChangeDescriptionController
import ru.nstu.grin.concatenation.description.events.GetDescriptionQuery
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionModel
import tornadofx.*
import java.util.*

class ChangeDescriptionFragment : Fragment() {
    val descriptionId: UUID by param()
    private val model: ChangeDescriptionModel by inject()
    private val controller: ChangeDescriptionController = find(params = params) { }

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
        vbox {
            hbox {
                spacer()
                button("Ок") {
                    alignment = Pos.BASELINE_CENTER
                    action {
                        controller.updateDescription()
                        close()
                    }
                }
                spacing = 20.0
            }
            spacing = 20.0
            spacer()
        }


    }

    init {
        fire(GetDescriptionQuery(descriptionId))
    }
}