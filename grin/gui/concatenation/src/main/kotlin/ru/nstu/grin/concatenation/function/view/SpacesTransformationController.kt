package ru.nstu.grin.concatenation.function.view

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

class SpacesTransformationController(
    private val matrixTransformer: MatrixTransformer,
    private val model: ConcatenationCanvasModel,
) {
    suspend fun transformSpaces() = coroutineScope {
        model.cartesianSpaces.forEach { space ->
            space.functions.forEach { function ->
                ensureActive()

                if (function.isHide) {
                    function.pixelsToDraw = null
                } else{
                    launch {
                        function.pixelsToDraw = transformPoints(
                            function,
                            space.xAxis,
                            space.yAxis,
                        )
                    }
                }
            }
        }
    }

    private suspend fun transformPoints(
        function: ConcatenationFunction,
        xAxis: ConcatenationAxis,
        yAxis: ConcatenationAxis,
    ): Pair<DoubleArray, DoubleArray> = coroutineScope {
        val xScaleProperties = xAxis.scaleProperties
        val yScaleProperties = yAxis.scaleProperties

        val transformedPoints = function.transformedPoints

        val xResults = DoubleArray(transformedPoints.first.size)
        val yResults = DoubleArray(transformedPoints.second.size)

        matrixTransformer.transformUnitsToPixel(
            transformedPoints.first,
            xResults,
            xScaleProperties,
            xAxis.direction,
        )

        matrixTransformer.transformUnitsToPixel(
            transformedPoints.second,
            yResults,
            yScaleProperties,
            yAxis.direction,
        )

        Pair(xResults, yResults)
    }
}