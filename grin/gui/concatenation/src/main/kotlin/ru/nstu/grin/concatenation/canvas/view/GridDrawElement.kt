package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import kotlin.math.roundToInt

class GridDrawElement(
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    private val color: Color,
    private val matrixTransformerController: MatrixTransformerController
) : ChainDrawElement {

    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        context.stroke = color

        if (xAxis.settings.isOnlyIntegerPow) {
            drawXGridForIntegerMarks(context)
        } else {
            drawXGridForDoubleMarks(context)
        }

        if (yAxis.settings.isOnlyIntegerPow) {
            drawYGridForIntegerMarks(context)
        } else {
            drawYGridForDoubleMarks(context)
        }
    }

    private fun drawXGridForDoubleMarks(context: GraphicsContext) {
        val (minYPixel, maxYPixel) = matrixTransformerController.getMinMaxPixel(yAxis.direction)
        val (minXPixel, maxXPixel) = matrixTransformerController.getMinMaxPixel(xAxis.direction)

        var currentX = minXPixel + 10.0
        while (currentX < maxXPixel) {
            context.strokeLine(
                currentX,
                minYPixel,
                currentX,
                maxYPixel
            )
            currentX += xAxis.distanceBetweenMarks
        }
    }

    private fun drawYGridForDoubleMarks(context: GraphicsContext) {
        val (minYPixel, maxYPixel) = matrixTransformerController.getMinMaxPixel(yAxis.direction)
        val (minXPixel, maxXPixel) = matrixTransformerController.getMinMaxPixel(xAxis.direction)

        var currentY = maxYPixel - 10.0
        while (currentY > minYPixel) {
            context.strokeLine(
                minXPixel,
                currentY,
                maxXPixel,
                currentY
            )
            currentY -= yAxis.distanceBetweenMarks
        }
    }

    private fun drawXGridForIntegerMarks(context: GraphicsContext) {
        var currentX = xAxis.settings.min.toInt().toDouble()
        val max = xAxis.settings.max
        val (minYPixel, maxYPixel) = matrixTransformerController.getMinMaxPixel(yAxis.direction)

        while (currentX < max) {
            val stepX = matrixTransformerController.transformUnitsToPixel(currentX, xAxis.settings, xAxis.direction)
            context.strokeLine(
                stepX,
                minYPixel,
                stepX,
                maxYPixel
            )
            currentX += xAxis.settings.integerStep
        }
    }

    private fun drawYGridForIntegerMarks(context: GraphicsContext) {
        var currentY = yAxis.settings.max.roundToInt().toDouble()
        val min = yAxis.settings.min
        val (minXPixel, maxXPixel) = matrixTransformerController.getMinMaxPixel(xAxis.direction)

        while (currentY > min) {
            val stepY = matrixTransformerController.transformUnitsToPixel(currentY, yAxis.settings, yAxis.direction)
            context.strokeLine(
                minXPixel,
                stepY,
                maxXPixel,
                stepY
            )
            currentY -= yAxis.settings.integerStep
        }
    }
}