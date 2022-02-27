package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import ru.nstu.grin.concatenation.cartesian.controller.CartesianListViewController
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace

class CartesianListView(
    model: CartesianListViewModel,
    private val controller: CartesianListViewController
) : ListView<CartesianSpace>(model.cartesianSpaces) {
    init {
        setCellFactory {
            object : ListCell<CartesianSpace>() {
                override fun updateItem(item: CartesianSpace?, empty: Boolean) {
                    super.updateItem(item, empty)

                    graphic = if (item == null) null else createItem(item)
                }
            }
        }
    }

    private fun createItem(space: CartesianSpace) = BorderPane().apply {
        left = HBox(
            Label(space.name),
            Label("Grid: ${if (space.isShowGrid) "Yes" else "No"}")
        ).apply {
            spacing = 10.0
        }

        right = HBox(
            Button(null, ImageView(Image("copy.png")).apply {
                fitHeight = 20.0
                fitWidth = 20.0
            }).apply {
                tooltip = Tooltip("Скопировать")

                setOnAction {
                    controller.openCopyModal(space, scene.window)
                }
            },
            Button(null, ImageView(Image("edit-tool.png")).apply {
                fitHeight = 20.0
                fitWidth = 20.0
            }).apply {
                setOnAction {
                    controller.openEditModal(space, scene.window)
                }
                tooltip = Tooltip("Редактировать")
            },
            Button(null, ImageView(Image("send-to-trash.png")).apply {
                fitHeight = 20.0
                fitWidth = 20.0
            }).apply {
                setOnAction {
                    controller.deleteCartesian(space)
                }
                tooltip = Tooltip("Удалить")
            },
        ).apply {
            spacing = 5.0
        }
    }
}