package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.model.PointToolTipSettings
import javax.swing.ToolTipManager

class TooltipsDrawElement(
    private val pointTooltipSettings: PointToolTipSettings,
    private val pointTooltip: Tooltip
) : ChainDrawElement {

    override fun draw(context: GraphicsContext) {
        pointTooltip.text = "x=${pointTooltipSettings.x}, y=${pointTooltipSettings.y}"
        if (pointTooltipSettings.isShow) {
            println("Show")
            pointTooltip.show(context.canvas, pointTooltipSettings.x, pointTooltipSettings.y)
        } else {
            println("Hide")
            pointTooltip.hide()
        }
    }
}