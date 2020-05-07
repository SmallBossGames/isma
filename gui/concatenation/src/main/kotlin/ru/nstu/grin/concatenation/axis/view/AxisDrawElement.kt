package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.marks.MarksProvider
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis

class AxisDrawElement(
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    cartesianSpaces: List<CartesianSpace>
) : ChainDrawElement {

    private val verticalAxisDraw =
        VerticalAxisDrawStrategy(
            yAxis.settings, cartesianSpaces
        )
    private val horizontalAxisDraw =
        HorizontalAxisDrawStrategy(
            xAxis.settings, cartesianSpaces
        )

    override fun draw(context: GraphicsContext) {
        drawBackground(context, xAxis.order, xAxis.direction, xAxis.backGroundColor)
        drawBackground(context, yAxis.order, yAxis.direction, yAxis.backGroundColor)
        drawAxisMarks(context, xAxis.order, xAxis.zeroPoint, xAxis.direction, xAxis.marksProvider, xAxis.fontColor)
        drawAxisMarks(context, yAxis.order, yAxis.zeroPoint, yAxis.direction, yAxis.marksProvider, yAxis.fontColor)
    }

    private fun drawAxisMarks(
        context: GraphicsContext,
        order: Int,
        zeroPoint: Double,
        direction: Direction,
        marksProvider: MarksProvider,
        color: Color
    ) {
        context.stroke = color
        val startPoint = order * SettingsProvider.getAxisWidth()
        val marksCoordinate = startPoint + MARKS_MARGIN
        val correlatedZeroPoint = zeroPoint + when (direction) {
            Direction.LEFT, Direction.RIGHT -> yAxis.settings.correlation
            Direction.TOP, Direction.BOTTOM -> xAxis.settings.correlation
        }

        when (direction) {
            Direction.LEFT -> {
                verticalAxisDraw.drawMarks(context, correlatedZeroPoint, direction, marksProvider, marksCoordinate)
            }
            Direction.RIGHT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    correlatedZeroPoint,
                    direction,
                    marksProvider,
                    SettingsProvider.getCanvasWidth() - marksCoordinate
                )
            }
            Direction.TOP -> {
                horizontalAxisDraw.drawMarks(
                    context, correlatedZeroPoint, direction, marksProvider, marksCoordinate
                )
            }
            Direction.BOTTOM -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    correlatedZeroPoint,
                    direction,
                    marksProvider,
                    SettingsProvider.getCanvasHeight() - marksCoordinate
                )
            }
        }
    }

    private fun drawBackground(context: GraphicsContext, order: Int, direction: Direction, color: Color) {
        context.fill = color
        val startPoint = order * SettingsProvider.getAxisWidth()
        when (direction) {
            Direction.LEFT -> {
                context.fillRect(startPoint, 0.0, SettingsProvider.getAxisWidth(), SettingsProvider.getCanvasHeight())
            }
            Direction.RIGHT -> {
                context.fillRect(
                    SettingsProvider.getCanvasWidth() - startPoint,
                    0.0,
                    SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasHeight()
                )
            }
            Direction.TOP -> {
                context.fillRect(
                    0.0,
                    startPoint,
                    SettingsProvider.getCanvasWidth(),
                    SettingsProvider.getAxisWidth()
                )
            }
            Direction.BOTTOM -> {
                context.fillRect(
                    0.0,
                    SettingsProvider.getCanvasHeight() - startPoint - SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasWidth(),
                    SettingsProvider.getAxisWidth()
                )
            }
        }
    }

    private companion object {
        const val MARKS_MARGIN = 20.0
    }
}