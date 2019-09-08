package ru.nstu.grin.view

import javafx.collections.ObservableList
import javafx.scene.Parent
import javafx.stage.StageStyle
import ru.nstu.grin.model.Context
import ru.nstu.grin.model.Edge
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.*
import java.util.function.Function

class GrinCanvas : View() {
    companion object {
        private const val WIDTH = 1200.0
        private const val HEIGHT = 800.0
    }

    private val context = Context(
        WIDTH,
        HEIGHT
    )

    private val grinCanvasModel: GrinCanvasModel by inject()


    override val root: Parent = stackpane {
        group {
            add(context.canvas)
        }

        context.canvas.setOnContextMenuRequested { contextEvent ->
            contextmenu {
                menuitem("Add function").action {

                }
                menuitem("Add arrow").action {
                    find<ArrowModalView>(
                        mapOf(
                            ArrowModalView::x to contextEvent.screenX,
                            ArrowModalView::y to contextEvent.screenY
                        )
                    ).openModal(stageStyle = StageStyle.UTILITY)
                }
            }
        }
    }

    private fun drawByFunction(
        min: Double,
        max: Double,
        step: Double,
        func: Function<Double, Double>
    ): List<Pair<Double, Double>> {
        var current = min;
        val results = mutableListOf<Pair<Double, Double>>()
        while (current < max) {
            results.add(Pair(current, func.apply(current)))
            current += step;
        }
        return results
    }

    private fun myLovelyFunction(x: Double) = 2 * x
}