package ru.nstu.grin.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.view.ChainDrawElement

class DescriptionDrawElement(
    private val descriptions: List<Description>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (description in descriptions) {
            context.stroke = description.color
            context.strokeText(description.text, description.x, description.y, description.size)
        }
    }
}