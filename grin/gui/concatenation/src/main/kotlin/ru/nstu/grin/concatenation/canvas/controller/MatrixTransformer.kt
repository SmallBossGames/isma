package ru.nstu.grin.concatenation.canvas.controller

import jdk.incubator.vector.DoubleVector
import jdk.incubator.vector.VectorSpecies
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

    fun transformUnitsToPixel(
        input: DoubleArray,
        output: DoubleArray,
        scaleProperties: AxisScaleProperties,
        direction: Direction,
    ) {
        val min = scaleProperties.minValue
        val max = scaleProperties.maxValue
        val sumUnits = abs(max - min)

        val (minPixel, maxPixel) = getMinMaxPixel(direction)
        val sumPixel = maxPixel - minPixel
        val unitPrice = sumPixel / sumUnits

        val upperBound = SPECIES.loopBound(input.size)

        var i = 0

        when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                while (i < upperBound) {
                    val va = DoubleVector.fromArray(SPECIES, input, i)
                    val vb = va.sub(min).mul(unitPrice).sub(maxPixel).neg()
                    vb.intoArray(output, i)

                    i += SPECIES.length()
                }
                while (i < output.size) {
                    output[i] = maxPixel - (input[i] - min) * unitPrice
                    i++
                }
            }
            Direction.TOP, Direction.BOTTOM  -> {
                while (i < upperBound) {
                    val va = DoubleVector.fromArray(SPECIES, input, i)
                    val vb = va.sub(min).mul(unitPrice).add(minPixel)
                    vb.intoArray(output, i)

                    i += SPECIES.length()
                }
                while (i < output.size) {
                    output[i] = minPixel + (input[i] - min) * unitPrice
                    i++
                }
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

    companion object{
        @JvmStatic
        val SPECIES: VectorSpecies<Double> = DoubleVector.SPECIES_PREFERRED
    }
}