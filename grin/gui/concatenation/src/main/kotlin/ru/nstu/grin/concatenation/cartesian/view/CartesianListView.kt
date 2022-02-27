package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.cartesian.controller.CartesianListViewController
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import tornadofx.*

class CartesianListView(
    override val scope: Scope,
    model: CartesianListViewModel,
    controller: CartesianListViewController
) : Fragment() {
    override val root: Parent = listview(model.cartesianSpaces) {
        cellFormat {
            graphic = borderpane {
                left {
                    hbox {
                        spacing = 10.0

                        add(Label(it.name))
                        add(Label("Grid: ${ if (it.isShowGrid) "Yes" else "No" }"))
                    }
                }
                right {
                    hbox {
                        spacing = 5.0
                        button {
                            action {
                                controller.openCopyModal(it, currentWindow)
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
                                controller.openEditModal(it, currentWindow)
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
                                controller.deleteCartesian(it)
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
}