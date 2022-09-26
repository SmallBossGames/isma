package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.MarksDistanceType
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import kotlin.math.roundToInt

class GridDrawElement(
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    private val color: Color,
    private val matrixTransformer: MatrixTransformer
) : ChainDrawElement {

    private val xScaleProperties = xAxis.scaleProperties
    private val yScaleProperties = yAxis.scaleProperties

    private val xStyleProperties = xAxis.styleProperties
    private val yStyleProperties = yAxis.styleProperties

    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        context.stroke = color

        if (xStyleProperties.marksDistanceType == MarksDistanceType.VALUE) {
            drawXGridForIntegerMarks(context)
        } else {
            drawXGridForDoubleMarks(context)
        }

        if (yStyleProperties.marksDistanceType == MarksDistanceType.VALUE) {
            drawYGridForIntegerMarks(context)
        } else {
            drawYGridForDoubleMarks(context)
        }
    }

    private fun drawXGridForDoubleMarks(context: GraphicsContext) {
        val (minYPixel, maxYPixel) = matrixTransformer.getMinMaxPixel(yAxis.direction)
        val (minXPixel, maxXPixel) = matrixTransformer.getMinMaxPixel(xAxis.direction)

        var currentX = minXPixel + 10.0
        while (currentX < maxXPixel) {
            context.strokeLine(
                currentX,
                minYPixel,
                currentX,
                maxYPixel
            )
            currentX += xStyleProperties.marksDistance
        }
    }

    private fun drawYGridForDoubleMarks(context: GraphicsContext) {
        val (minYPixel, maxYPixel) = matrixTransformer.getMinMaxPixel(yAxis.direction)
        val (minXPixel, maxXPixel) = matrixTransformer.getMinMaxPixel(xAxis.direction)

        var currentY = maxYPixel - 10.0
        while (currentY > minYPixel) {
            context.strokeLine(minXPixel, currentY, maxXPixel, currentY)
            currentY -= xStyleProperties.marksDistance
        }
    }

    private fun drawXGridForIntegerMarks(context: GraphicsContext) {
        var currentX = xScaleProperties.minValue.toInt().toDouble()
        val max = xScaleProperties.maxValue
        val (minYPixel, maxYPixel) = matrixTransformer
            .getMinMaxPixel(yAxis.direction)

        while (currentX < max) {
            val stepX = matrixTransformer.transformUnitsToPixel(currentX, xScaleProperties, xAxis.direction)

            context.strokeLine(stepX, minYPixel, stepX, maxYPixel)
            currentX += xStyleProperties.marksDistance
        }
    }

    private fun drawYGridForIntegerMarks(context: GraphicsContext) {
        var currentY = yScaleProperties.maxValue.roundToInt().toDouble()
        val min = yScaleProperties.minValue
        val (minXPixel, maxXPixel) = matrixTransformer
            .getMinMaxPixel(xAxis.direction)

        while (currentY > min) {
            val stepY = matrixTransformer
                .transformUnitsToPixel(currentY, yScaleProperties, yAxis.direction)

            context.strokeLine(
                minXPixel,
                stepY,
                maxXPixel,
                stepY
            )
            currentY -= yStyleProperties.marksDistance
        }
    }
}