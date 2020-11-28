package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.ListView
import ru.nstu.grin.concatenation.function.controller.MirrorFunctionController
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.MirrorFunctionModel
import tornadofx.*

class MirrorFunctionFragment : Fragment() {
    private val model: MirrorFunctionModel by inject()
    private val controller: MirrorFunctionController = find { }
    val isMirrorY: Boolean by param()

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

                controller.mirrorFunction(isMirrorY, function)
                close()
            }
        }
    }

    init {
        controller.getAllFunctions()
    }
}