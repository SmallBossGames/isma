package ru.nstu.grin.concatenation.axis.extensions

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.model.Offsets
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace

fun List<CartesianSpace>.findLocatedAxisOrNull(
    x: Double,
    y: Double,
    canvasViewModel: CanvasViewModel,
): ConcatenationAxis? {
    val offsets = Offsets()

    val canvasHeight = canvasViewModel.canvasHeight
    val canvasWidth = canvasViewModel.canvasWidth

    for (space in this) {
        for (axis in space.axes) {
            if(axis.styleProperties.isVisible) {
                continue
            }

            val axisHeight = axis.styleProperties.borderHigh

            val isFound = when (axis.direction) {
                Direction.LEFT -> {
                    val maxX = offsets.left + axisHeight
                    val minX = offsets.left

                    x < maxX && x > minX
                }
                Direction.RIGHT -> {
                    val maxX = canvasWidth - offsets.right
                    val minX = canvasWidth - offsets.right - axisHeight

                    x < maxX && x > minX
                }
                Direction.TOP -> {
                    val maxY = offsets.top + axisHeight
                    val minY = offsets.top

                    y < maxY && y > minY
                }
                Direction.BOTTOM -> {
                    val maxY = canvasHeight - offsets.bottom
                    val minY = canvasHeight - offsets.bottom - axisHeight

                    y < maxY && y > minY
                }
            }

            if(isFound){
                return axis
            }

            offsets.increase(axisHeight, axis.direction)
        }
    }

    return null
}