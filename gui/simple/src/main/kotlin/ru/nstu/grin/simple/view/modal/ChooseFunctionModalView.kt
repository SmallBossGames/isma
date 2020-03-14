package ru.nstu.grin.simple.view.modal

import javafx.scene.Parent
import ru.nstu.grin.common.model.ChooseFunctionWay
import ru.nstu.grin.simple.controller.ChooseFunctionController
import ru.nstu.grin.simple.model.ChooseFunctionViewModel
import tornadofx.*

class ChooseFunctionModalView : View() {
    private val model: ChooseFunctionViewModel by inject()
    private val controller: ChooseFunctionController by inject()

    override val root: Parent = form {
        label("Добавить функцию из:")
        combobox(model.wayProperty, ChooseFunctionWay.values().toList()) {
            cellFormat {
                text = formatWay(it)
            }
        }
        button("Ok") {
            action {
                controller.openModal()
                close()
            }
        }
    }

    private fun formatWay(way: ChooseFunctionWay): String {
        return when (way) {
            ChooseFunctionWay.FILE -> "Файл"
            ChooseFunctionWay.INPUT -> "Ручной ввод"
            ChooseFunctionWay.ANALYTIC -> "Аналитически"
        }
    }
}