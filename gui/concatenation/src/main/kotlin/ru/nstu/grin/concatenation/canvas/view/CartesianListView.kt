package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.controller.CartesianListViewController
import ru.nstu.grin.concatenation.canvas.events.GetAllCartesiansQuery
import ru.nstu.grin.concatenation.canvas.model.CartesianListViewModel
import tornadofx.*

class CartesianListView : Fragment() {
    private val model: CartesianListViewModel by inject()
    private val controller: CartesianListViewController = find { }

    override val root: Parent = listview(model.cartesianSpacesProperty) {
        cellFormat {
            graphic = form {
                hbox {
                    fieldset("Имя") {
                        field {
                            label(it.name)
                        }
                    }
                    fieldset("Сетка") {
                        label(if (it.isShowGrid) "Да" else "Нет")
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
            }
        }
    }

    init {
        fire(GetAllCartesiansQuery())
    }
}