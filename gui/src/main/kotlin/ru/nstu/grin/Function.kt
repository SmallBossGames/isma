package ru.nstu.grin

import javafx.scene.canvas.Canvas

/**
 * Class represents a graphic, drawing in grin
 */
class Function(
    private val canvas: Canvas,
    private val list: List<Pair<Double, Double>>,
    private val yMapping: Direction,
    private val xMapping: Direction
) {
    init {
        val gc = canvas.graphicsContext2D
        when {
            xMapping == Direction.BOTTOM && yMapping == Direction.LEFT -> {
                for (i in 0 until list.size - 1) {
                    gc.strokeLine(
                        list[i].first,
                        canvas.height - list[i].second,
                        list[i + 1].first,
                        canvas.height - list[i + 1].second
                    )
                }
            }
        }
    }
}