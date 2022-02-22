package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.Label
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

    override val root: Parent = listview(model.functions) {
        cellFormat {
            graphic = borderpane {
                left {
                    hbox {
                        spacing = 10.0
                        add(Rectangle(15.0, it.lineSize, it.functionColor))
                        add(Label(it.name))
                    }
                }
                right {
                    hbox {
                        spacing = 5.0

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
                                controller.openChangeModal(it.id, currentWindow)
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
    }

    init {
        controller.getAllFunctions()
    }
}