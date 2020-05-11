package ru.nstu.grin.concatenation.canvas.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.axis.view.AxisChangeFragment
import ru.nstu.grin.concatenation.canvas.controller.FunctionListViewController
import ru.nstu.grin.concatenation.canvas.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.canvas.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import tornadofx.*

class FunctionListView : Fragment() {
    private val model: FunctionListViewModel by inject()
    private val controller: FunctionListViewController = find { }

    override val root: Parent = listview(model.functionsProperty) {
        items.addListener { _: ListChangeListener.Change<out ConcatenationFunction> ->
            println("Refresh")
        }
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

    init {
        controller.getAllFunctions()
    }
}