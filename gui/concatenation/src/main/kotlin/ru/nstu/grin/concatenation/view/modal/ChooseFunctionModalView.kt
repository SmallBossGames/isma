package ru.nstu.grin.concatenation.view.modal

import javafx.scene.Parent
import ru.nstu.grin.common.model.ChooseFunctionWay
import ru.nstu.grin.concatenation.controller.ChooseFunctionController
import ru.nstu.grin.concatenation.model.ChooseFunctionViewModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ChooseFunctionModalView : Fragment() {
    private val model: ChooseFunctionViewModel by inject(params = params)
    private val controller: ChooseFunctionController by inject(params = params)

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