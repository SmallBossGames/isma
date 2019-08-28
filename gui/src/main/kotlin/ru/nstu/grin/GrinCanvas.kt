package ru.nstu.grin

import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import ru.nstu.grin.axis.Axis
import tornadofx.stackpane
import tornadofx.View
import tornadofx.group
import tornadofx.canvas
import java.util.function.Function

class GrinCanvas : View() {

    val canvas: Canvas = Canvas()

    companion object {
        private const val WIDTH = 1200.0
        private const val HEIGHT = 800.0
    }

    private val myFavouritePoints = drawByFunction(0.0, 100.0, 0.1, Function { myLovelyFunction(it) })

    override val root: Parent = stackpane {
        group {
            canvas(WIDTH, HEIGHT) {
                Axis(
                    this,
                    200.0,
                    10.0,
                    listOf("0", "2500", "5000", "7500", "10000"),
                    MappingPosition.LEFT,
                    backGroundColor = Color.RED,
                    delimiterColor = Color.WHITE
                )
//                Axis(graphicsContext2D, 0.0, 0.0, MappingPosition.BOTTOM)
//                ru.nstu.grin.Function(
//                    this,
//                    myFavouritePoints,
//                    MappingPosition.LEFT,
//                    MappingPosition.BOTTOM
//                )
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