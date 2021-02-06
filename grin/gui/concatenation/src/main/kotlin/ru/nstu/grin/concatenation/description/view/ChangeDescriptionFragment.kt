package ru.nstu.grin.concatenation.description.view

import javafx.geometry.Pos
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
    private val controller: ChangeDescriptionController = find(params = params) { }

    override val root: Parent = form {
        fieldset {
            field("Текст") {
                textfield().bind(model.textProperty)
            }
            field("Размер шрифта") {
                textfield(model.textSizeProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || it.toDoubleOrNull() ?: -1.0 < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
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
                button("Сохранить") {
                    enableWhen { model.valid }
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