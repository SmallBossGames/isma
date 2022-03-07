package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.function.controller.MirrorFunctionController
import ru.nstu.grin.concatenation.function.model.MirrorFunctionModel
import tornadofx.*

class MirrorFunctionFragment : Fragment() {
    private val model: MirrorFunctionModel by inject()
    private val controller: MirrorFunctionController = find { }
    val isMirrorY: Boolean by param()

    override val root: Parent = vbox {
        val list = listview(model.functions) {
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
                    error("Необходимо выбрать функцию")
                    return@action
                }

                controller.mirrorFunction(isMirrorY, function)
                close()
            }
        }
    }
}