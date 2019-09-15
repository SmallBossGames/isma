package ru.nstu.grin.view

import javafx.scene.Parent
import tornadofx.View
import tornadofx.button
import tornadofx.vbox

/**
 * @author Konstantin Volivach
 */
class ChooseFunctionModalView : View() {

    override val root: Parent = vbox {
        button("Добавить функцию из файла") {
            find<FileEnterFunctionView>().openModal()
        }
        button("Добавить функцию вручную по точкам") {
            find<ManualEnterFunctionView>().openModal()
        }
        button("Добавить функцию аналитически") {
            find<AnalyticFunctionModalView>().openModal()
        }
    }
}