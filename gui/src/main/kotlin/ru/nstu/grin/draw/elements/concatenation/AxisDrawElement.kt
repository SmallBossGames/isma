package ru.nstu.grin.draw.elements.concatenation

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.drawable.axis.AbstractAxis
import ru.nstu.grin.view.ChainDrawElement

class AxisDrawElement(
    private val xAxis: AbstractAxis,
    private val yAxis: AbstractAxis
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}