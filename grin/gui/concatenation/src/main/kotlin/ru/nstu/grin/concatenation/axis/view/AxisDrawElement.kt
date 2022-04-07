package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class AxisDrawElement(
    private val model: ConcatenationCanvasModel,
    private val verticalAxisDraw: VerticalAxisDrawStrategy,
    private val horizontalAxisDraw: HorizontalAxisDrawStrategy,
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        for (cartesianSpace in model.cartesianSpaces) {
            val xAxis = cartesianSpace.xAxis
            val yAxis = cartesianSpace.yAxis

            if (!xAxis.isHide) {
                drawBackground(
                    context,
                    xAxis.order,
                    xAxis.direction,
                    xAxis.backGroundColor,
                    canvasWidth,
                    canvasHeight
                )
                drawAxisMarks(
                    context,
                    xAxis.order,
                    xAxis,
                    xAxis.direction,
                    canvasWidth,
                    canvasHeight
                )
            }

            if (!yAxis.isHide) {
                drawBackground(
                    context,
                    yAxis.order,
                    yAxis.direction,
                    yAxis.backGroundColor,
                    canvasWidth,
                    canvasHeight
                )
                drawAxisMarks(
                    context,
                    yAxis.order,
                    yAxis,
                    yAxis.direction,
                    canvasWidth,
                    canvasHeight
                )
            }
        }
    }

    private fun drawAxisMarks(
        context: GraphicsContext,
        order: Int,
        axis: ConcatenationAxis,
        direction: Direction,
        canvasWidth: Double,
        canvasHeight: Double
    ) {
        val startPoint = order * SettingsProvider.getAxisWidth()
        val marksCoordinate = startPoint + MARKS_MARGIN

        when (direction) {
            Direction.LEFT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    axis,
                    marksCoordinate,
                )
            }
            Direction.RIGHT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    axis,
                    canvasWidth - marksCoordinate,
                )
            }
            Direction.TOP -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    axis,
                    marksCoordinate,
                )
            }
            Direction.BOTTOM -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    axis,
                    canvasHeight - marksCoordinate,
                )
            }
        }
    }

    private fun drawBackground(
        context: GraphicsContext,
        order: Int,
        direction: Direction,
        color: Color,
        canvasWidth: Double, 
        canvasHeight: Double
    ) {
        context.fill = color
        val startPoint = order * SettingsProvider.getAxisWidth()
        when (direction) {
            Direction.LEFT -> {
                context.fillRect(
                    startPoint,
                    getTopAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth(),
                    canvasHeight - getBottomAxisSize() * SettingsProvider.getAxisWidth()
                )
            }
            Direction.RIGHT -> {
                context.fillRect(
                    canvasWidth - startPoint,
                    getTopAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth(),
                    canvasHeight - getBottomAxisSize() * SettingsProvider.getAxisWidth()
                )
            }
            Direction.TOP -> {
                context.fillRect(
                    getLeftAxisSize() * SettingsProvider.getAxisWidth(),
                    startPoint,
                    canvasWidth - getRightAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth()
                )
            }
            Direction.BOTTOM -> {
                context.fillRect(
                    getLeftAxisSize() * SettingsProvider.getAxisWidth(),
                    canvasHeight - startPoint - SettingsProvider.getAxisWidth(),
                    canvasWidth - getRightAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth()
                )
            }
        }
    }

    private fun getTopAxisSize(): Int {
        return filterAxis(Direction.TOP).size
    }

    private fun getBottomAxisSize(): Int {
        return filterAxis(Direction.BOTTOM).size
    }

    private fun getLeftAxisSize(): Int {
        return filterAxis(Direction.LEFT).size
    }

    private fun getRightAxisSize(): Int {
        return filterAxis(Direction.RIGHT).size
    }

    private fun filterAxis(direction: Direction) = model.cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten()
        .filter { it.direction == direction }

    private companion object {
        const val MARKS_MARGIN = 20.0
    }
}