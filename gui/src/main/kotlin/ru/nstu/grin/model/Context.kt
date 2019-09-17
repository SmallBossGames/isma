package ru.nstu.grin.model

import javafx.scene.canvas.Canvas
import ru.nstu.grin.Direction

/**
 * @author Konstantin Volivach
 */
data class Context(
    val canvas: Canvas,
    val functions: MutableList<Function>,
    val descriptions: MutableList<Description>,
    val arrows: MutableList<Arrow>,
    val edges: MutableMap<Direction, Edge>
) {
    constructor(width: Double, height: Double) : this(
        Canvas(width, height),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableMapOf()
    )
}