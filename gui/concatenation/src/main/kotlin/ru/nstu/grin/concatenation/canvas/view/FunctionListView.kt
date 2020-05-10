package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.axis.view.AxisChangeFragment
import ru.nstu.grin.concatenation.canvas.controller.FunctionListViewController
import ru.nstu.grin.concatenation.canvas.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import tornadofx.*

class FunctionListView : Fragment() {
    private val model: FunctionListViewModel by inject()
    private val controller: FunctionListViewController = find { }

    override val root: Parent = listview(model.functions) {
        cellFormat {
            graphic = form {
                hbox {
                    spacing = 20.0
                    fieldset("Имя") {
                        label(it.name)
                    }
                    fieldset("цвет") {
                        label(it.functionColor.toString())
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
                button {
                    action {
                        find<ChangeFunctionFragment>(
                            mapOf(
                                ChangeFunctionFragment::functionId to it.id
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
}