package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import javafx.stage.Stage
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.model.PointSettings
import ru.nstu.grin.simple.model.PointToolTipSettings

class TooltipsDrawElement(
    private val pointTooltipSettings: PointToolTipSettings,
    private val pointTooltips: MutableList<Tooltip>,
    private val stage: Stage
) : ChainDrawElement {

    override fun draw(context: GraphicsContext) {
        val filteredPoints = pointTooltipSettings.pointsSettings.filter { pointSettings ->
            !pointTooltips.any { it.text == formatText(pointSettings) }
        }
        println("Filtered ${filteredPoints.size}")
        for (pointSettings in filteredPoints) {
            val pointToolTip = Tooltip()
            pointToolTip.text = formatText(pointSettings)
            pointToolTip.show(
                context.canvas,
                stage.x + pointSettings.xGraphic,
                stage.y + pointSettings.yGraphic
            )
            context.stroke = Color.BLACK
            pointTooltips.add(pointToolTip)
            context.strokeRect(pointSettings.xGraphic, pointSettings.y, 1.0, 1.0)
        }
        if (pointTooltipSettings.isShow.not()) {
            pointTooltips.forEach { it.hide() }
            pointTooltips.clear()
        }
    }

    private fun formatText(pointSettings: PointSettings): String {
        return "x=${pointSettings.x.round(5)}, y=${pointSettings.y.round(5)}"
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()
}