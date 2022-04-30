package ru.nstu.grin.concatenation.function.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.math.IntersectionSearcher

class FunctionOperationsService(
    private val view: ConcatenationCanvas,
    private val matrixTransformer: MatrixTransformer,
    private val canvasModel: ConcatenationCanvasModel,
    private val canvasController: ConcatenationCanvasController,
) {
    suspend fun showInterSections(
        firstFunction: ConcatenationFunction,
        secondFunction: ConcatenationFunction,
        mergeDistance: Double,
    ) {
        coroutineScope {
            val (xPoints1, yPoints1) = firstFunction.pixelsToDraw!!
            val (xPoints2, yPoints2) = secondFunction.pixelsToDraw!!

            val space = canvasModel.cartesianSpaces.first{ it.functions.contains(firstFunction) }

            val xScaleProps = space.xAxis.scaleProperties
            val xDirection = space.xAxis.direction

            val yScaleProps = space.yAxis.scaleProperties
            val yDirection = space.yAxis.direction

            val points = IntersectionSearcher
                .findIntersections(xPoints1, yPoints1, xPoints2, yPoints2)
                .map {
                    Pair(
                        matrixTransformer.transformPixelToUnits(it.first, xScaleProps, xDirection),
                        matrixTransformer.transformPixelToUnits(it.second, yScaleProps, yDirection),
                    )
                }.mergeIntervals(mergeDistance)

            launch(Dispatchers.JavaFx) {
                for(point in points){
                    canvasController.addPointDescription(space, point.first, point.second)
                }
            }
        }
    }

    fun localizeFunction(function: ConcatenationFunction) {
        val cartesianSpace = canvasModel.cartesianSpaces.first { it.functions.contains(function) }
        val pixels = function.pixelsToDraw ?: return

        val xPoints = pixels.first.map {
            matrixTransformer.transformPixelToUnits(
                it,
                cartesianSpace.xAxis.scaleProperties,
                cartesianSpace.xAxis.direction
            )
        }

        val yPoints = pixels.second.map {
            matrixTransformer.transformPixelToUnits(
                it,
                cartesianSpace.xAxis.scaleProperties,
                cartesianSpace.xAxis.direction
            )
        }

        val minY = yPoints.minOrNull() ?: return
        val maxY = yPoints.maxOrNull() ?: return
        val minX = xPoints.minOrNull() ?: return
        val maxX = xPoints.maxOrNull() ?: return

        cartesianSpace.xAxis.scaleProperties = cartesianSpace.xAxis.scaleProperties.copy(
            minValue = minX,
            maxValue = maxX
        )

        cartesianSpace.yAxis.scaleProperties = cartesianSpace.yAxis.scaleProperties.copy(
            minValue = minY,
            maxValue = maxY
        )

        view.redraw()
    }

    companion object {
        @JvmStatic
        fun List<Pair<Double, Double>>.mergeIntervals(mergeDistance: Double): List<Pair<Double, Double>> {
            if (this.isEmpty()) return this

            val powMergeDistance = mergeDistance * mergeDistance
            val resultList = mutableListOf(this[0])

            var beginInterval = this[0]

            for (i in 1 until this.size){
                val current = resultList.last()
                val next = this[i]
                val dX = next.first - current.first
                val dY = next.second - current.second
                val distance = dX * dX + dY * dY

                if(distance < powMergeDistance){
                    if (current === beginInterval){
                        resultList.add(next)
                    } else {
                        resultList[resultList.lastIndex] = next
                    }
                } else {
                    resultList.add(next)
                    beginInterval = next
                }
            }

            return resultList
        }
    }
}