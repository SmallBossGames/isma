package ru.nstu.grin.view.modal

import javafx.scene.Parent
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.ExistDirection
import ru.nstu.grin.view.modal.function.AnalyticFunctionModalView
import ru.nstu.grin.view.modal.function.FileFunctionModalView
import ru.nstu.grin.view.modal.function.ManualEnterFunctionModalView
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ChooseFunctionModalView : Fragment() {
    val drawSize: DrawSize by param()
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()

    override val root: Parent = vbox {
        button("Добавить функцию из файла") {
            action {
                find<FileFunctionModalView>(
                    mapOf(
                        FileFunctionModalView::drawSize to drawSize,
                        FileFunctionModalView::xExistDirections to xExistDirections,
                        FileFunctionModalView::yExistDirections to yExistDirections
                    )
                ).openModal()
                close()
            }
        }
        button("Добавить функцию вручную по точкам") {
            action {
                find<ManualEnterFunctionModalView>(
                    mapOf(
                        ManualEnterFunctionModalView::drawSize to drawSize,
                        ManualEnterFunctionModalView::xExistDirections to xExistDirections,
                        ManualEnterFunctionModalView::yExistDirections to yExistDirections
                    )
                ).openModal()
                close()
            }
        }
        button("Добавить функцию аналитически") {
            action {
                find<AnalyticFunctionModalView>(
                    mapOf(
                        AnalyticFunctionModalView::drawSize to drawSize,
                        AnalyticFunctionModalView::xExistDirections to xExistDirections,
                        AnalyticFunctionModalView::yExistDirections to yExistDirections
                    )
                ).openModal()
                close()
            }
        }
    }
}