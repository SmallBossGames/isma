package ru.nstu.grin.concatenation.points.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.AxisScalingType
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.koin.MainGrinScope
import ru.nstu.grin.concatenation.points.model.PointSettings
import kotlin.math.pow

class PointTooltipsDrawElement(
    private val mainGrinScope: MainGrinScope,
    private val transformer: MatrixTransformer,
    private val canvasViewModel: ConcatenationCanvasViewModel,
) : ChainDrawElement {
    private val pointTooltips = mutableListOf<Tooltip>()

    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        val stage = mainGrinScope.primaryStage
        val filteredPoints = canvasViewModel.pointToolTipSettings.pointsSettings.filter { pointSettings ->
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
        for (pointSettings in canvasViewModel.pointToolTipSettings.pointsSettings) {
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
        if (canvasViewModel.pointToolTipSettings.isShow.not()) {
            pointTooltips.forEach { it.hide() }
            pointTooltips.clear()
        }
    }

    private fun formatText(pointSettings: PointSettings): String {
        val x = if (pointSettings.xAxisProperties.scalingType == AxisScalingType.LOGARITHMIC) {
            pointSettings.xAxisProperties.scalingLogBase.pow(
                transformer.transformPixelToUnits(
                    pointSettings.xGraphic,
                    pointSettings.xAxisProperties,
                    Direction.BOTTOM
                )
            )
        } else {
            transformer.transformPixelToUnits(
                pointSettings.xGraphic,
                pointSettings.xAxisProperties,
                Direction.BOTTOM
            )
        }

        val y = if (pointSettings.xAxisProperties.scalingType == AxisScalingType.LOGARITHMIC) {
            pointSettings.yAxisProperties.scalingLogBase.pow(
                transformer.transformPixelToUnits(
                    pointSettings.yGraphic,
                    pointSettings.yAxisProperties,
                    Direction.LEFT
                )
            )
        } else {
            transformer.transformPixelToUnits(
                pointSettings.yGraphic,
                pointSettings.yAxisProperties,
                Direction.LEFT
            )
        }
        return "x=${x.round(5)}, y=${y.round(5)}"
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()

    private companion object {
        const val SIZE_OF_CROSS = 20.0
    }
}