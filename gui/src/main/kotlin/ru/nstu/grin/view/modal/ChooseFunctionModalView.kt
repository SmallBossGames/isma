package ru.nstu.grin.view.modal

import javafx.scene.Parent
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.view.FileEnterFunctionView
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.vbox

/**
 * @author Konstantin Volivach
 */
class ChooseFunctionModalView : View() {
    val drawSize: DrawSize by param()

    override val root: Parent = vbox {
        button("Добавить функцию из файла") {
            action {
                find<FileEnterFunctionView>().openModal()
                close()
            }
        }
        button("Добавить функцию вручную по точкам") {
            action {
                find<ManualEnterFunctionModalView>().openModal()
                close()
            }
        }
        button("Добавить функцию аналитически") {
            action {
                find<AnalyticFunctionModalView>(
                    mapOf(
                        AnalyticFunctionModalView::drawSize to drawSize
                    )
                ).openModal()
                close()
            }
        }
    }
}