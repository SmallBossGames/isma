package ru.nstu.grin.view.modal

import javafx.scene.Parent
import ru.nstu.grin.controller.ChooseFunctionController
import ru.nstu.grin.model.ChooseFunctionWay
import ru.nstu.grin.model.view.ChooseFunctionViewModel
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
                val modal = controller.getModal()
                modal.openModal()
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