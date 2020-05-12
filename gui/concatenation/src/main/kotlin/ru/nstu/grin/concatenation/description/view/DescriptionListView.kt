package ru.nstu.grin.concatenation.description.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.description.controller.DescriptionListViewController
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel
import tornadofx.*

class DescriptionListView : Fragment() {
    private val model: DescriptionListViewModel by inject()
    private val controller: DescriptionListViewController by inject()

    override val root: Parent = listview(model.descriptionsProperty) {
        cellFormat {
            graphic = form {
                hbox {
                    spacing = 20.0
                    fieldset("Текст") {
                        label(it.text)
                    }
                    fieldset("Размер текста") {
                        label(it.size.toString())
                    }
                    fieldset("Цвет текста") {
                        label(it.color.toString())
                    }
                    fieldset("Семейство шрифта") {
                        label(it.font)
                    }
                }
                hbox {
                    spacing = 20.0
                    button {
                        action {
                            controller.openCopyModal(it.id)
                        }
                        val image = Image("copy.png")
                        val imageView = ImageView(image)
                        imageView.fitHeight = 20.0
                        imageView.fitWidth = 20.0
                        graphic = imageView
                        tooltip = Tooltip("Скопировать")
                    }
                    button {
                        action {
                            controller.openChangeModal(it.id)
                        }
                        val image = Image("edit-tool.png")
                        val imageView = ImageView(image)
                        imageView.fitHeight = 20.0
                        imageView.fitWidth = 20.0
                        graphic = imageView
                        tooltip = Tooltip("Отредактировать")
                    }
                    button {
                        action {
                            controller.deleteDescription(it.id)
                        }
                        val image = Image("send-to-trash.png")
                        val imageView = ImageView(image)
                        imageView.fitHeight = 20.0
                        imageView.fitWidth = 20.0
                        graphic = imageView
                        tooltip = Tooltip("Удалить")
                    }
                }
            }
        }
    }
}