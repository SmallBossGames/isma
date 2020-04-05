package ru.nstu.grin.concatenation.draw.elements.axis

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.Direction

interface AxisMarksDrawStrategy {
    fun drawMarks(
        context: GraphicsContext,
        zeroPoint: Double,
        direction: Direction,
        marksProvider: MarksProvider,
        marksCoordinate: Double
    )
}