package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Controller

class AxisDrawElement : ChainDrawElement, Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val verticalAxisDraw: VerticalAxisDrawStrategy by inject()
    private val horizontalAxisDraw: HorizontalAxisDrawStrategy by inject()

    override fun draw(context: GraphicsContext) {
        for (cartesianSpace in model.cartesianSpaces) {
            val xAxis = cartesianSpace.xAxis
            val yAxis = cartesianSpace.yAxis

            if (xAxis.isHide.not()) {
                drawBackground(context, xAxis.order, xAxis.direction, xAxis.backGroundColor)
                drawAxisMarks(context, xAxis.order, xAxis, xAxis.direction, xAxis.fontColor)
            }
            if (yAxis.isHide.not()) {
                drawBackground(context, yAxis.order, yAxis.direction, yAxis.backGroundColor)
                drawAxisMarks(context, yAxis.order, yAxis, yAxis.direction, yAxis.fontColor)
            }
        }
    }

    private fun drawAxisMarks(
        context: GraphicsContext,
        order: Int,
        axis: ConcatenationAxis,
        direction: Direction,
        color: Color
    ) {
        context.stroke = color
        val startPoint = order * SettingsProvider.getAxisWidth()
        val marksCoordinate = startPoint + MARKS_MARGIN

        when (direction) {
            Direction.LEFT -> {
                verticalAxisDraw.drawMarks(context, axis, marksCoordinate)
            }
            Direction.RIGHT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    axis,
                    SettingsProvider.getCanvasWidth() - marksCoordinate
                )
            }
            Direction.TOP -> {
                horizontalAxisDraw.drawMarks(
                    context, axis, marksCoordinate
                )
            }
            Direction.BOTTOM -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    axis,
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
                context.fillRect(
                    startPoint,
                    getTopAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()
                )
            }
            Direction.RIGHT -> {
                context.fillRect(
                    SettingsProvider.getCanvasWidth() - startPoint,
                    getTopAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()
                )
            }
            Direction.TOP -> {
                context.fillRect(
                    getLeftAxisSize() * SettingsProvider.getAxisWidth(),
                    startPoint,
                    SettingsProvider.getCanvasWidth() - getRightAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getAxisWidth()
                )
            }
            Direction.BOTTOM -> {
                context.fillRect(
                    getLeftAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasHeight() - startPoint - SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasWidth() - getRightAxisSize() * SettingsProvider.getAxisWidth(),
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