package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.view.ChainDrawElement

class DescriptionDrawElement(
    private val descriptions: List<Description>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (description in descriptions) {
            context.stroke = description.color
            context.strokeText(description.text, description.x, description.y, description.textSize)
        }
    }
}