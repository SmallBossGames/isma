package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import kotlin.math.abs
import kotlin.math.min

class SelectionDrawElement(
    private val canvasViewModel: ConcatenationCanvasViewModel,
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        val (isFirstPointSelected, isSecondPointSelected, firstPoint, secondPoint) = canvasViewModel.selectionSettings

        if (!isFirstPointSelected || !isSecondPointSelected) {
            return
        }

        context.stroke = Color.BLACK

        context.strokeRect(
            min(firstPoint.x, secondPoint.x),
            min(firstPoint.y, secondPoint.y),
            abs(secondPoint.x - firstPoint.x),
            abs(secondPoint.y - firstPoint.y)
        )
    }
}