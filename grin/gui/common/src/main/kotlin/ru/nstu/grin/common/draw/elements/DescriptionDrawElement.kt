package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.view.ChainDrawElement

class DescriptionDrawElement(
    private val descriptions: List<Description>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        for (description in descriptions) {
            context.stroke = description.color
            context.fill = description.color
            context.font = Font.font(description.font, description.textSize)

            context.fillText(description.text, description.x, description.y)

            if (description.isSelected) {
                context.stroke = Color.BLACK

                context.strokeRect(
                    description.x - description.textSize / 5, description.y - description.textSize * 1.25,
                    description.text.length * (description.textSize/1.25), description.textSize * 2
                )
            }
        }
    }
}