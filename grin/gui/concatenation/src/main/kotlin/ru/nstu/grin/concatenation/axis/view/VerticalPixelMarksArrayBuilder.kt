package ru.nstu.grin.concatenation.axis.view

import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties
import ru.nstu.grin.concatenation.axis.model.AxisStyleProperties
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.utilities.createStringValue
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import kotlin.math.floor

class VerticalPixelMarksArrayBuilder(
    private val matrixTransformer: MatrixTransformer
) {
    fun buildDoubleMarksArray(
        scaleProperties: AxisScaleProperties,
        styleProperties: AxisStyleProperties,
        direction: Direction
    ): List<DrawingMark> {
        val result = mutableListOf<DrawingMark>()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixelVertical()

        val maxDrawingPixel = maxPixel - TEXT_VERTICAL_BORDERS_OFFSET
        val minDrawingPixel = minPixel + TEXT_VERTICAL_BORDERS_OFFSET

        val zeroPixel = matrixTransformer.transformUnitsToPixel(0.0, scaleProperties, direction)

        val zeroPixelOffset = if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            val text = createStringValue(0.0, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(zeroPixel + height / 2 < maxDrawingPixel && zeroPixel - height / 2 > minDrawingPixel){
                result.add(DrawingMark(text, zeroPixel, height, width))
                height / 2
            } else{
                0.0
            }
        } else {
            0.0
        }

        val step = styleProperties.marksDistance
        val overflowLeft = minDrawingPixel - zeroPixel
        val skipLeft = if (overflowLeft > 0) floor(overflowLeft / step) * step else 0.0
        val overflowRight = zeroPixel - maxDrawingPixel
        val skipRight = if (overflowLeft > 0) floor(overflowRight / step) * step else 0.0

        var nextMarkPixel = zeroPixel + styleProperties.marksDistance + skipLeft
        var filledPosition = zeroPixel + zeroPixelOffset

        while (nextMarkPixel < maxDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel - height / 2 - MIN_SPACE_BETWEEN_MARKS > filledPosition
                && nextMarkPixel + height / 2 < maxDrawingPixel
                && nextMarkPixel - height / 2 > minDrawingPixel
            ){
                filledPosition = nextMarkPixel + height / 2
                result.add(DrawingMark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel += styleProperties.marksDistance
        }

        nextMarkPixel = zeroPixel - styleProperties.marksDistance - skipRight
        filledPosition = zeroPixel - zeroPixelOffset

        while (nextMarkPixel > minDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel + height / 2 + MIN_SPACE_BETWEEN_MARKS < filledPosition
                && nextMarkPixel + height / 2 < maxDrawingPixel
                && nextMarkPixel - height / 2 > minDrawingPixel
            ) {
                filledPosition = nextMarkPixel - height / 2
                result.add(DrawingMark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel -= styleProperties.marksDistance
        }

        return result
    }

    private companion object {
        const val TEXT_VERTICAL_BORDERS_OFFSET = 5.0
        const val MIN_SPACE_BETWEEN_MARKS = 5.0
    }
}