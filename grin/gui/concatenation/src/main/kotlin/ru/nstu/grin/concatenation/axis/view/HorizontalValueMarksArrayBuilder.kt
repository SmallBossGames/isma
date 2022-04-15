package ru.nstu.grin.concatenation.axis.view

import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties
import ru.nstu.grin.concatenation.axis.model.AxisStyleProperties
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.utilities.createStringValue
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import kotlin.math.max
import kotlin.math.min

class HorizontalValueMarksArrayBuilder(
    private val matrixTransformer: MatrixTransformer,
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

        var nextMarkPixel = max(
            minPixel,
            matrixTransformer.transformUnitsToPixel(styleProperties.marksDistance, scaleProperties, direction)
        )
        var nextMarkValue = matrixTransformer.transformPixelToUnits(nextMarkPixel, scaleProperties, direction)
        var filledPosition = zeroPixel + zeroPixelOffset

        while (nextMarkPixel < maxDrawingPixel){

            val text = createStringValue(nextMarkValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel - width / 2 - MIN_SPACE_BETWEEN_MARKS > filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ){
                filledPosition = nextMarkPixel + width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkValue += styleProperties.marksDistance
            nextMarkPixel = matrixTransformer.transformUnitsToPixel(nextMarkValue, scaleProperties, direction)
        }

        nextMarkPixel = min(
            maxPixel,
            matrixTransformer.transformUnitsToPixel(-styleProperties.marksDistance, scaleProperties, direction)
        )
        nextMarkValue = matrixTransformer.transformPixelToUnits(
            number = nextMarkPixel,
            scaleProperties = scaleProperties,
            direction = direction,
        )
        filledPosition = zeroPixel - zeroPixelOffset

        while (nextMarkPixel > minDrawingPixel){

            val text = createStringValue(nextMarkValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel + width / 2 + MIN_SPACE_BETWEEN_MARKS < filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ) {
                filledPosition = nextMarkPixel - width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkValue -= styleProperties.marksDistance
            nextMarkPixel = matrixTransformer.transformUnitsToPixel(nextMarkValue, scaleProperties, direction)
        }

        return result
    }

    private companion object {
        const val TEXT_VERTICAL_BORDERS_OFFSET = 5.0
        const val MIN_SPACE_BETWEEN_MARKS = 5.0
    }
}