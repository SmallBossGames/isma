package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.cartesian.controller.CartesianListViewController
import ru.nstu.grin.concatenation.cartesian.events.GetAllCartesiansQuery
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import tornadofx.*

class CartesianListView : Fragment() {
    private val model: CartesianListViewModel by inject()
    private val controller: CartesianListViewController = find { }

    override val root: Parent = listview(model.cartesianSpaces) {
        cellFormat {
            graphic = form {
                hbox {
                    spacing = 20.0
                    fieldset("Имя") {
                        field {
                            label(it.name)
                        }
                    }
                    fieldset("Сетка") {
                        label(if (it.isShowGrid) "Да" else "Нет")
                    }
                }
                hbox {
                    spacing = 20.0
                    button {
                        action {
                            find<CopyCartesianFragment>(
                                mapOf(
                                    CopyCartesianFragment::cartesianId to it.id
                                )
                            ).openModal()
                            val image = Image("copy.png")
                            val imageView = ImageView(image)
                            imageView.fitHeight = 20.0
                            imageView.fitWidth = 20.0
                            graphic = imageView
                            tooltip = Tooltip("Скопировать")
                        }
                    }
                    button {
                        action {
                            find<ChangeCartesianFragment>(
                                mapOf(
                                    ChangeCartesianFragment::cartesianId to it.id
                                )
                            ).openModal()
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
                            controller.deleteCartesian(it.id)
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

    init {
        fire(GetAllCartesiansQuery())
    }
}