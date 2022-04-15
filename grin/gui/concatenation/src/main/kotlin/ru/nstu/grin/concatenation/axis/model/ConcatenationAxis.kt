package ru.nstu.grin.concatenation.axis.model

import ru.nstu.grin.common.common.SettingsProvider

enum class Direction { LEFT, RIGHT, TOP, BOTTOM }

data class ConcatenationAxis(
    val name: String,
    val order: Int,
    val direction: Direction,
    var styleProperties: AxisStyleProperties = AxisStyleProperties(),
    var scaleProperties: AxisScaleProperties = AxisScaleProperties(),

) {
    fun isLocated(x: Double, y: Double, canvasWidth: Double, canvasHeight: Double): Boolean {
        when (direction) {
            Direction.LEFT -> {
                val maxX = (order + 1) * SettingsProvider.getAxisWidth()
                val minX = order * SettingsProvider.getAxisWidth()
                return x < maxX && x > minX
            }
            Direction.RIGHT -> {
                val maxX = canvasWidth - order * SettingsProvider.getAxisWidth()
                val minX = canvasWidth - (order + 1) * SettingsProvider.getAxisWidth()

                return x > minX && x < maxX
            }
            Direction.TOP -> {
                val maxY = (order + 1) * SettingsProvider.getAxisWidth()
                val minY = order * SettingsProvider.getAxisWidth()

                return y > minY && y < maxY
            }
            Direction.BOTTOM -> {
                val maxY = canvasHeight - order * SettingsProvider.getAxisWidth()
                val minY = canvasHeight - (order + 1) * SettingsProvider.getAxisWidth()

                return y > minY && y < maxY
            }
        }
    }

    val isXAxis get() = direction == Direction.TOP || direction == Direction.BOTTOM

    val isYAxis get() = direction == Direction.RIGHT || direction == Direction.LEFT
}