package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.view.ChainDrawElement

class DescriptionDrawElement(
    private val descriptions: List<Description>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (description in descriptions) {
            context.stroke = description.color
            context.font = Font.font(description.font, description.textSize)
            context.strokeText(description.text, description.x, description.y)
            context.stroke = Color.BLACK
            if (description.isSelected) {
                context.strokeRect(
                    description.x-description.textSize/5, description.y - description.textSize*1.25,
                    description.textSize * 2, description.textSize * 2
                )
            }
        }
    }
}