package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.ListView
import ru.nstu.grin.concatenation.function.controller.LocalizeFunctionController
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LocalizeFunctionModel
import tornadofx.*

class LocalizeFunctionFragment : Fragment() {
    private val model: LocalizeFunctionModel by inject()
    private val controller: LocalizeFunctionController = find { }

    private lateinit var list: ListView<ConcatenationFunction>

    override val root: Parent = vbox {
        listview(model.functionsProperty) {
            list = this
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
                val function = list.selectedItem ?: let {
                    tornadofx.error("Необходимо выбрать функцию")
                    return@action
                }
                controller.localize(function.id)
            }
        }
    }


    init {
        controller.getAllFunctions()
    }
}