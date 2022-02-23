package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.function.controller.IntersectionFunctionController
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionModel
import tornadofx.*

class IntersectionFunctionFragment : Fragment() {
    private val model: IntersectionFunctionModel by inject()
    private val controller: IntersectionFunctionController = find { }

    private lateinit var list: ListView<ConcatenationFunction>

    override val root: Parent = vbox {
        listview(model.functions) {
            list = this
            selectionModel.selectionMode = SelectionMode.MULTIPLE

            addEventFilter(MouseEvent.MOUSE_PRESSED) {
                var node = it.pickResult.intersectedNode

                while (node != null && node != list && node !is ListCell<*>) {
                    node = node.parent
                }

                if (node is ListCell<*>) {
                    it.consume()
                    val cell = node
                    val lv = cell.listView

                    lv.requestFocus()

                    if (!cell.isEmpty) {
                        val index = cell.index
                        if (cell.isSelected) {
                            lv.selectionModel.clearSelection(index)
                        } else {
                            lv.selectionModel.select(index)
                        }
                    }
                }
            }
            cellFormat {
                graphic = form {
                    hbox {
                        fieldset("Имя") {
                            label(it.name)
                        }
                    }
                }
            }
        }
        button("Ок") {
            action {
                val functions = list.selectionModel.selectedItems
                if (functions.size != 2) {
                    error("Можно выбрать пересечение только между двумя функциями")
                    return@action
                }
                controller.findIntersection(functions)
                close()
            }
        }
    }
}