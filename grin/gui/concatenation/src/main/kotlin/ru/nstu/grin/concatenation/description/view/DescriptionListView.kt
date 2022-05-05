package ru.nstu.grin.concatenation.description.view

import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import ru.nstu.grin.concatenation.description.controller.DescriptionListViewController
import ru.nstu.grin.concatenation.description.model.Description
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel

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

    private fun createItem(item: Description): BorderPane {
        return BorderPane().apply {
            left = Label(item.text).apply {
                font = item.font
            }

            right = HBox(
                Button(null,  ImageView(Image("edit-tool.png")).apply {
                    fitHeight = 20.0
                    fitWidth = 20.0
                }).apply {
                    tooltip = Tooltip("Отредактировать")

                    setOnAction {
                        controller.openChangeModal(item, scene.window)
                    }
                },
                Button(null,  ImageView(Image("send-to-trash.png")).apply {
                    fitHeight = 20.0
                    fitWidth = 20.0
                }).apply {
                    tooltip = Tooltip("Удалить")

                    setOnAction {
                        controller.deleteDescription(item)
                    }
                },
            ).apply {
                spacing = 5.0
            }
        }
    }
}