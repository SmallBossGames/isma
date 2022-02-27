package ru.nstu.grin.concatenation.description.view

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.text.Font
import javafx.stage.Stage
import ru.nstu.grin.concatenation.description.controller.ChangeDescriptionController
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionModel
import tornadofx.*

class ChangeDescriptionFragment(
    private val controller: ChangeDescriptionController,
    private val model: ChangeDescriptionModel
) : Form() {

    init {
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
                    alignment = Pos.BASELINE_CENTER
                    action {
                        controller.updateDescription(model)
                        (scene.window as Stage).close()
                    }
                }
                spacing = 20.0
            }
            spacing = 20.0
            spacer()
        }
    }
}