package ru.nstu.grin.concatenation.description.view

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.description.controller.DescriptionListViewController
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel
import tornadofx.*

class DescriptionListView(
    model: DescriptionListViewModel,
    private val controller: DescriptionListViewController
) : ListView<Description>(model.descriptions) {

    init {
        setCellFactory {
            object : ListCell<Description>() {
                override fun updateItem(item: Description?, empty: Boolean) {
                    super.updateItem(item, empty)

                    graphic = if (item == null) null else createItem(item)
                }
            }
        }
    }

    private fun createItem(item: Description): Form {
        return form {
            hbox {
                spacing = 20.0
                fieldset("Текст") {
                    label(item.text)
                }
                fieldset("Размер текста") {
                    label(item.textSize.toString())
                }
                fieldset("Цвет текста") {
                    label(item.color.toString())
                }
                fieldset("Семейство шрифта") {
                    label(item.font)
                }
            }
            hbox {
                spacing = 20.0
                button {
                    action {
                        controller.openChangeModal(item)
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
                        controller.deleteDescription(item)
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