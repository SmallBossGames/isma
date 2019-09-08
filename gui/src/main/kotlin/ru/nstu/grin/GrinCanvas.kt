package ru.nstu.grin

import javafx.scene.Parent
import ru.nstu.grin.model.Context
import ru.nstu.grin.model.Label
import ru.nstu.grin.model.Point
import tornadofx.stackpane
import tornadofx.View
import tornadofx.group
import java.util.function.Function

class GrinCanvas : View() {
    companion object {
        private const val WIDTH = 1200.0
        private const val HEIGHT = 800.0
    }

    private val context = Context(WIDTH, HEIGHT)

    override val root: Parent = stackpane {
        group {
            add(context.canvas)
        }
    }

    fun addFunction(array: List<Point>) {

    }

    fun addFunction(array: List<Point>, labels: List<Label>) {

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