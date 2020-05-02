package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import javafx.stage.Stage
import ru.nstu.grin.common.model.PointSettings
import ru.nstu.grin.common.model.PointToolTipsSettings
import ru.nstu.grin.common.view.ChainDrawElement

class PointTooltipsDrawElement(
    private val pointTooltipSettings: PointToolTipsSettings,
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
        }
        for (pointSettings in pointTooltipSettings.pointsSettings) {
            context.strokeLine(
                pointSettings.xGraphic - SIZE_OF_CROSS,
                pointSettings.yGraphic,
                pointSettings.xGraphic + SIZE_OF_CROSS,
                pointSettings.yGraphic
            )
            context.strokeLine(
                pointSettings.xGraphic,
                pointSettings.yGraphic - SIZE_OF_CROSS,
                pointSettings.xGraphic,
                pointSettings.yGraphic + SIZE_OF_CROSS
            )
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

    private companion object {
        const val SIZE_OF_CROSS = 20.0
    }
}