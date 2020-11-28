package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.model.SelectionSettings
import kotlin.math.abs

class SelectionDrawElement(
    private val selectionSettings: SelectionSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        val (isSelected, firstPoint, secondPoint) = selectionSettings
        if (isSelected.not()) return
        context.stroke = Color.BLACK
        context.strokeRect(
            if (firstPoint.x<secondPoint.x) firstPoint.x else secondPoint.x,
            if (firstPoint.y<secondPoint.y) firstPoint.y else secondPoint.y,
            abs(secondPoint.x - firstPoint.x),
            abs(secondPoint.y - firstPoint.y)
        )

        println("Stroked rect $selectionSettings")
    }
}