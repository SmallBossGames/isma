package ru.nstu.grin.concatenation.description.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel

class DescriptionDrawElement(
    private val canvasModel: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        context.save()

        for (description in canvasModel.descriptions) {
            context.stroke = description.color
            context.fill = description.color
            context.font = Font.font(description.font, description.textSize)

            context.fillText(description.text, description.x, description.y)

            if (canvasViewModel.selectedDescriptions.contains(description)) {
                context.stroke = Color.BLACK

                context.strokeRect(
                    description.x - description.textSize / 5, description.y - description.textSize * 1.25,
                    description.text.length * (description.textSize/1.25), description.textSize * 2
                )
            }
        }

        context.restore()
    }
}