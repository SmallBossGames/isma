package ru.nstu.grin.concatenation.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import javafx.stage.Stage
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.model.PointToolTipSettings

class TooltipsDrawElement(
    private val pointTooltipSettings: PointToolTipSettings,
    private val pointTooltip: Tooltip,
    private val stage: Stage
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        pointTooltip.text = "x=${pointTooltipSettings.x.round(5)}, y=${pointTooltipSettings.y.round(5)}"
        if (pointTooltipSettings.isShow) {
            pointTooltip.show(
                context.canvas,
                stage.x + pointTooltipSettings.xGraphic,
                stage.y + pointTooltipSettings.yGraphic
            )
            context.stroke = Color.BLACK
            context.strokeRect(pointTooltipSettings.xGraphic, pointTooltipSettings.yGraphic, 1.0, 1.0)
        } else {
            pointTooltip.hide()
        }
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()
}