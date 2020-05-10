package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.axis.model.AxisSettings
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import tornadofx.Controller
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

class MatrixTransformerController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()

    fun transformPixelToUnits(number: Double, axisSettings: AxisSettings, direction: Direction): Double {
        val min = axisSettings.min
        val max = axisSettings.max
        val sumUnits = getUnitsLength(min, max)

        val (minPixel, maxPixel) = getMinMaxPixel(direction)
        val sumPixel = maxPixel - minPixel
        val unitPrice = sumPixel / sumUnits

        return min - (number - minPixel) / unitPrice
    }

    fun transformUnitsToPixel(number: Double, axisSettings: AxisSettings, direction: Direction): Double {
        val min = axisSettings.min
        val max = axisSettings.max
        val sumUnits = getUnitsLength(min, max)

        val (minPixel, maxPixel) = getMinMaxPixel(direction)
        val sumPixel = maxPixel - minPixel
        val unitPrice = sumPixel / sumUnits

        return when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                if (max <= 0) {
                    minPixel + (-max.absoluteValue - number) * unitPrice
                } else {
                    minPixel + (max.absoluteValue - number) * unitPrice
                }
            }
            Direction.TOP, Direction.BOTTOM -> {
                if (min < 0) {
                    minPixel + (min.absoluteValue + number) * unitPrice
                } else {
                    minPixel + (-min.absoluteValue + number) * unitPrice
                }
            }
        }
    }

    private fun getUnitsLength(min: Double, max: Double): Double {
        return when {
            min >= 0 && max >= 0 -> {
                max - min
            }
            min <= 0 && max >= 0 -> {
                min.absoluteValue + max.absoluteValue
            }
            min <= 0 && max <= 0 -> {
                min.absoluteValue - max.absoluteValue
            }
            else -> throw IllegalArgumentException("Such min and max can't be $min and $max")
        }
    }

    private fun getMinMaxPixel(direction: Direction): Pair<Double, Double> {
        return when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
                Pair(
                    getTopAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()
                )
            }
            Direction.TOP, Direction.BOTTOM -> {
                Pair(
                    getLeftAxisSize() * SettingsProvider.getAxisWidth(),
                    SettingsProvider.getCanvasWidth() - getRightAxisSize() * SettingsProvider.getAxisWidth()
                )

            }
        }
    }

    private fun getLeftAxisSize(): Int {
        return model.cartesianSpaces.filter { it.xAxis.direction == Direction.LEFT || it.yAxis.direction == Direction.LEFT }
            .size
    }

    private fun getRightAxisSize(): Int {
        return model.cartesianSpaces.filter { it.xAxis.direction == Direction.RIGHT || it.yAxis.direction == Direction.RIGHT }
            .size
    }

    private fun getBottomAxisSize(): Int {
        return model.cartesianSpaces.filter {
            it.xAxis.direction == Direction.BOTTOM
                || it.yAxis.direction == Direction.BOTTOM
        }
            .size
    }

    private fun getTopAxisSize(): Int {
        return model.cartesianSpaces.filter { it.xAxis.direction == Direction.TOP || it.yAxis.direction == Direction.TOP }
            .size
    }
}