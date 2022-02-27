package ru.nstu.grin.concatenation.description.view

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.ComboBox
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.description.controller.ChangeDescriptionController
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionModel
import tornadofx.*

class ChangeDescriptionFragment : Fragment() {
    private val model: ChangeDescriptionModel by inject(params = params)
    private val controller: ChangeDescriptionController by inject()

    override val root: Parent = form {
        fieldset {
            field("Текст") {
                textfield().bind(model.textProperty)
            }
            field("Размер шрифта") {
                textfield(model.textSizeProperty)
            }
            field("Цвет шрифта") {
                colorpicker().bind(model.colorProperty)
            }
            field("Семейство шрифта") {
                val fontFamilies = FXCollections.observableArrayList(Font.getFamilies())
                val comboBox = ComboBox(fontFamilies).apply {
                    valueProperty().bindBidirectional(model.fontProperty)
                }
                add(comboBox)
            }

        }
        vbox {
            hbox {
                spacer()
                button("Сохранить") {
                    enableWhen { model.valid }
                    alignment = Pos.BASELINE_CENTER
                    action {
                        controller.updateDescription(model)
                        close()
                    }
                }
                spacing = 20.0
            }
            spacing = 20.0
            spacer()
        }
    }
}