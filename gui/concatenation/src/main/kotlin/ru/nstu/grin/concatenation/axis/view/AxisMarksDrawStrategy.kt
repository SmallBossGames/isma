package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.concatenation.axis.marks.MarksProvider
import ru.nstu.grin.concatenation.axis.model.Direction

interface AxisMarksDrawStrategy {
    fun drawMarks(
        context: GraphicsContext,
        zeroPoint: Double,
        direction: Direction,
        marksProvider: MarksProvider,
        marksCoordinate: Double
    )
}