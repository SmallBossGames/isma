package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis

interface AxisMarksDrawStrategy {
    fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        canvasWidth: Double,
        canvasHeight: Double,
    )
}