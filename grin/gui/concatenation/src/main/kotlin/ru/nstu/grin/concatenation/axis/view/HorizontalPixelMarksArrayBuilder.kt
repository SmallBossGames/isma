package ru.nstu.grin.concatenation.axis.view

import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties
import ru.nstu.grin.concatenation.axis.model.AxisStyleProperties
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.utilities.createStringValue
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import kotlin.math.max
import kotlin.math.min

data class Mark(val text: String, val x: Double, val height: Double, val width: Double)



class HorizontalPixelMarksArrayBuilder(
    private val matrixTransformer: MatrixTransformer
) {
    fun buildDoubleMarksArray(
        scaleProperties: AxisScaleProperties,
        styleProperties: AxisStyleProperties,
        direction: Direction
    ): List<Mark> {
        val result = mutableListOf<Mark>()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixelHorizontal()

        val maxDrawingPixel = maxPixel - TEXT_VERTICAL_BORDERS_OFFSET
        val minDrawingPixel = minPixel + TEXT_VERTICAL_BORDERS_OFFSET

        val zeroPixel = matrixTransformer.transformUnitsToPixel(0.0, scaleProperties, direction)

        val zeroPixelOffset = if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            val text = createStringValue(0.0, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(zeroPixel + width / 2 < maxDrawingPixel && zeroPixel - width / 2 > minDrawingPixel){
                result.add(Mark(text, zeroPixel, height, width))
                width / 2
            } else{
                0.0
            }
        } else {
            0.0
        }

        var nextMarkPixel = max(zeroPixel + styleProperties.marksDistance, minDrawingPixel)
        var filledPosition = zeroPixel + zeroPixelOffset

        while (nextMarkPixel < maxDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel - width / 2 - MIN_SPACE_BETWEEN_MARKS > filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ){
                filledPosition = nextMarkPixel + width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel += styleProperties.marksDistance
        }

        nextMarkPixel = min(zeroPixel - styleProperties.marksDistance, maxDrawingPixel)
        filledPosition = zeroPixel - zeroPixelOffset

        while (nextMarkPixel > minDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel + width / 2 + MIN_SPACE_BETWEEN_MARKS < filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ) {
                filledPosition = nextMarkPixel - width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
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