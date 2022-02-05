package ru.nstu.grin.concatenation.points.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.points.model.PointSettings
import tornadofx.Controller
import kotlin.math.pow

class PointTooltipsDrawElement : ChainDrawElement, Controller() {
    private val pointTooltips = mutableListOf<Tooltip>()
    private val model: ConcatenationCanvasModel by inject()
    private val transformer: MatrixTransformerController by inject()

    override fun draw(context: GraphicsContext) {
        val stage = model.primaryStage
        val filteredPoints = model.pointToolTipSettings.pointsSettings.filter { pointSettings ->
            !pointTooltips.any { it.text == formatText(pointSettings) }
        }
        context.stroke = Color.BLACK

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
        for (pointSettings in model.pointToolTipSettings.pointsSettings) {
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
        if (model.pointToolTipSettings.isShow.not()) {
            pointTooltips.forEach { it.hide() }
            pointTooltips.clear()
        }
    }

    private fun formatText(pointSettings: PointSettings): String {
        val x = if (pointSettings.xAxisSettings.isLogarithmic) {
            pointSettings.xAxisSettings.logarithmBase.pow(
                transformer.transformPixelToUnits(
                    pointSettings.xGraphic,
                    pointSettings.xAxisSettings,
                    Direction.BOTTOM
                )
            )
        } else {
            transformer.transformPixelToUnits(
                pointSettings.xGraphic,
                pointSettings.xAxisSettings,
                Direction.BOTTOM
            )
        }

        val y = if (pointSettings.yAxisSettings.isLogarithmic) {
            pointSettings.yAxisSettings.logarithmBase.pow(
                transformer.transformPixelToUnits(pointSettings.yGraphic, pointSettings.yAxisSettings, Direction.LEFT)
            )
        } else {
            transformer.transformPixelToUnits(pointSettings.yGraphic, pointSettings.yAxisSettings, Direction.LEFT)
        }
        return "x=${x.round(5)}, y=${y.round(5)}"
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()

    private companion object {
        const val SIZE_OF_CROSS = 20.0
    }
}