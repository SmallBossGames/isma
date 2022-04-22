package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color
import kotlinx.coroutines.coroutineScope
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.function.transform.IAsyncPointsTransformer
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class PointsCache(
    val transformers: Array<IAsyncPointsTransformer>,
    val transformedPointsX: DoubleArray,
    val transformedPointsY: DoubleArray,
) {
    companion object {
        suspend fun create(
            points: List<Point>,
            transformers: Array<IAsyncPointsTransformer>
        ): PointsCache = coroutineScope {
            var xPoints = DoubleArray(points.size)
            var yPoints = DoubleArray(points.size)

            for (i in points.indices){
                xPoints[i] = points[i].x
                yPoints[i] = points[i].y
            }

            for (transformer in transformers){
                val (newXPoints, newYPoints) = transformer.transform(xPoints, yPoints)

                xPoints = newXPoints
                yPoints = newYPoints
            }

            PointsCache(transformers, xPoints, yPoints)
        }
    }
}

/**
 * @author kostya05983
 */
data class ConcatenationFunction(
    val id: UUID,
    var name: String,
    val points: List<Point>,
    var isHide: Boolean = false,
    var functionColor: Color,
    var lineSize: Double,
    var lineType: LineType,
) : Cloneable {

    private val transformedPointCache = AtomicReference(
        PointsCache(
            arrayOf(),
            points.map { it.x }.toDoubleArray(),
            points.map { it.y }.toDoubleArray(),
        )
    )

    val transformedPoints: Pair<DoubleArray, DoubleArray> get() {
        val cache = transformedPointCache.get()

        return Pair(cache.transformedPointsX, cache.transformedPointsY)
    }

    val transformers get() = transformedPointCache.get().transformers

    var pixelsToDraw: Pair<DoubleArray, DoubleArray>? = null

    suspend fun updateTransformersTransaction(
        operation: (Array<IAsyncPointsTransformer>) -> Array<IAsyncPointsTransformer>
    ): Boolean {
        val oldValue = transformedPointCache.get()

        val newValue = PointsCache.create(points, operation(oldValue.transformers))

        return transformedPointCache.compareAndSet(oldValue, newValue)
    }

    public override fun clone(): ConcatenationFunction {
        return ConcatenationFunction(
            id = id,
            name = name,
            points = points.map { it.copy() },
            functionColor = functionColor,
            lineSize = lineSize,
            lineType = lineType,
        )
    }

    companion object {
        fun isPixelsNearBy(xPixels: DoubleArray, yPixels: DoubleArray, x: Double, y:Double) : Boolean {
            for (i in xPixels.indices) {

                if(Point.isNearBy(x, y, xPixels[i], yPixels[i]))
                {
                    return true
                }
            }

            return false
        }

        fun indexOfPixelsNearBy(xPixels: DoubleArray, yPixels: DoubleArray, x: Double, y:Double) : Int {
            for (i in xPixels.indices) {

                if(Point.isNearBy(x, y, xPixels[i], yPixels[i]))
                {
                    return i
                }
            }

            return -1
        }
    }
}
