package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.events.GetAllAxisQuery
import ru.nstu.grin.concatenation.canvas.model.AxisListViewModel
import tornadofx.*

class AxisListView : Fragment() {
    private val model: AxisListViewModel by inject()
    private val controller: AxisListViewController = find { }

    override val root: Parent = listview(model.axisesProperty) {
        cellFormat {
            graphic = form {
                hbox {
                    fieldset("Имя") {
                        field {
                            label(it.name)
                        }
                    }
                    spacing = 20.0
                    fieldset("Цвет") {
                        field {
                            label(it.backGroundColor.toString())
                        }
                    }
                    fieldset("Шрифт") {
                        field {
                            label(it.font)
                        }
                    }
                    fieldset("Размер шрифта") {
                        field {
                            label(it.textSize.toString())
                        }
                    }
                }
                button {
                    action {
                        find<AxisChangeFragment>(
                            mapOf(
                                AxisChangeFragment::axisId to it.id
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
        fire(GetAllAxisQuery())
    }
}