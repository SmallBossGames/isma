package ru.nstu.grin.concatenation.draw.elements.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.Direction
import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis

class AxisDrawElement(
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    canvasSettings: CanvasSettings,
    cartesianSpaces: List<CartesianSpace>
) : ChainDrawElement {

    private val verticalAxisDraw = VerticalAxisDrawStrategy(
        canvasSettings, cartesianSpaces
    )
    private val horizontalAxisDraw = HorizontalAxisDrawStrategy(
        canvasSettings, cartesianSpaces
    )

    override fun draw(context: GraphicsContext) {
        drawBackground(context, xAxis.order, xAxis.direction, xAxis.backGroundColor)
        drawBackground(context, yAxis.order, yAxis.direction, yAxis.backGroundColor)
        drawAxisMarks(context, xAxis.order, xAxis.zeroPoint, xAxis.direction, xAxis.marksProvider, xAxis.delimiterColor)
        drawAxisMarks(context, yAxis.order, yAxis.zeroPoint, yAxis.direction, yAxis.marksProvider, yAxis.delimiterColor)
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

        when (direction) {
            Direction.LEFT -> {
                verticalAxisDraw.drawMarks(context, zeroPoint, direction, marksProvider, marksCoordinate)
            }
            Direction.RIGHT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    zeroPoint,
                    direction,
                    marksProvider,
                    SettingsProvider.getCanvasWidth() - marksCoordinate
                )
            }
            Direction.TOP -> {
                horizontalAxisDraw.drawMarks(
                    context, zeroPoint, direction, marksProvider, marksCoordinate
                )
            }
            Direction.BOTTOM -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    zeroPoint,
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