package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import kotlin.math.abs

class MatrixTransformer(
    private val canvasViewModel: ConcatenationCanvasViewModel,
) {
    fun transformPixelToUnits(
        number: Double,
        scaleProperties: AxisScaleProperties,
        direction: Direction,
    ): Double {
        val min = scaleProperties.minValue
        val max = scaleProperties.maxValue
        val sumUnits = abs(max - min)

        val (minPixel, maxPixel) = getMinMaxPixel(direction)
        val sumPixel = maxPixel - minPixel
        val unitPrice = sumPixel / sumUnits

        return when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                min + (maxPixel - number) / unitPrice
            }
            Direction.TOP, Direction.BOTTOM -> {
                min + (number - minPixel) / unitPrice
            }
        }
    }

    fun transformUnitsToPixel(
        number: Double,
        scaleProperties: AxisScaleProperties,
        direction: Direction,
    ): Double {
        val min = scaleProperties.minValue
        val max = scaleProperties.maxValue
        val sumUnits = abs(max - min)

        val (minPixel, maxPixel) = getMinMaxPixel(direction)
        val sumPixel = maxPixel - minPixel
        val unitPrice = sumPixel / sumUnits

        return when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                maxPixel - (number - min) * unitPrice
            }
            Direction.TOP, Direction.BOTTOM -> {
                minPixel + (number - min) * unitPrice
            }
        }
    }

    /**
     * Узнаём ширину окна за вычетом осей
     */
    fun getMinMaxPixel(direction: Direction): Pair<Double, Double> {
        return when (direction) {
            Direction.LEFT, Direction.RIGHT -> getMinMaxPixelVertical()
            Direction.TOP, Direction.BOTTOM -> getMinMaxPixelHorizontal()
        }
    }

    fun getMinMaxPixelVertical() = Pair(
        canvasViewModel.functionsArea.top,
        canvasViewModel.canvasHeight - canvasViewModel.functionsArea.bottom
    )

    fun getMinMaxPixelHorizontal() = Pair(
        canvasViewModel.functionsArea.left,
        canvasViewModel.canvasWidth - canvasViewModel.functionsArea.right
    )
}