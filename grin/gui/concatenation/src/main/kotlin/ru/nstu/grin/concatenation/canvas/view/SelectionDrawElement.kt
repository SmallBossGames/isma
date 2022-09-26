package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.model.EditModeViewModel
import kotlin.math.abs
import kotlin.math.min

class SelectionDrawElement(
    private val editModeViewModel: EditModeViewModel,
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        val (isFirstPointSelected, isSecondPointSelected, firstPoint, secondPoint) = editModeViewModel.selectionSettings

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