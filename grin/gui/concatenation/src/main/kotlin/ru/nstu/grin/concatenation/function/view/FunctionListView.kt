package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Rectangle
import ru.nstu.grin.concatenation.function.controller.FunctionListViewController
import ru.nstu.grin.concatenation.function.model.FunctionListViewModel
import tornadofx.*

class FunctionListView : Fragment() {
    private val model: FunctionListViewModel by inject()
    private val controller: FunctionListViewController = find { }

    override val root: Parent = listview(model.functionsProperty) {
        cellFormat {
            graphic = form {
                hbox {
                    spacing = 20.0
                    fieldset("Имя") {
                        label(it.name)
                    }
                    fieldset("Цвет") {
                        add(Rectangle(10.0, 10.0, it.functionColor))
                        /*label(
                            text = "",
                            graphic = Rectangle(10.0, 10.0, it.functionColor)
                        )*/
                    }
                    fieldset("Размер линии") {
                        label(it.lineSize.toString())
                    }
                    fieldset("Тип рисовки") {
                        label(it.lineType.toString())
                    }
                    fieldset("Отображается") {
                        label(if (it.isHide) "Нет" else "Да")
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
                            controller.deleteFunction(it.id)
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
        controller.getAllFunctions()
    }
}