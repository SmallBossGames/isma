package ru.nstu.grin.concatenation.description.view

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.description.controller.DescriptionModalController
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
        vgrow = Priority.ALWAYS
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
            field("Шрифт") {
                combobox(model.fontProperty, Font.getFamilies())
            }
        }
        vbox {
            hbox {
                alignment = Pos.CENTER
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
            spacing = 10.0
            spacer()
        }
    }
}