package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.function.transform.IAsyncPointsTransformer
import java.util.concurrent.atomic.AtomicReference

class PointsCache(
    val transformersCandidate: Array<IAsyncPointsTransformer>,
    val transformers: Array<IAsyncPointsTransformer>,
    val transformedPointsX: DoubleArray,
    val transformedPointsY: DoubleArray,
)

/**
 * @author kostya05983
 */
class ConcatenationFunction(
    var name: String,
    val points: List<Point>,
    var isHide: Boolean = false,
    var functionColor: Color,
    var lineSize: Double,
    var lineType: LineType,
) {

    private val transformedPointCache = AtomicReference(
        PointsCache(
            emptyTransformersArray,
            emptyTransformersArray,
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

    private val cacheUpdateJob = AtomicReference<Job?>(null)

    suspend fun updateTransformersTransaction(
        operation: (Array<IAsyncPointsTransformer>) -> Array<IAsyncPointsTransformer>
    ): Boolean = coroutineScope {
        val oldCache = transformedPointCache.get()

        val newCache = PointsCache(
            operation(oldCache.transformersCandidate.clone()),
            oldCache.transformers,
            oldCache.transformedPointsX,
            oldCache.transformedPointsY,
        )

        if (!transformedPointCache.compareAndSet(oldCache, newCache)) {
            return@coroutineScope false
        }

        val updateJob = launch(start = CoroutineStart.LAZY) {
            applyCacheCandidate()
        }

        val currentJob = cacheUpdateJob.compareAndExchangeAcquire(null, updateJob) ?: updateJob

        currentJob.join()

        return@coroutineScope true
    }


    private suspend fun applyCacheCandidate() {
        mainLoop@ do {
            val originCache = transformedPointCache.get()

            if (originCache.transformersCandidate === originCache.transformers) {
                break
            }

            val newTransformers = originCache.transformersCandidate

            var xPoints = DoubleArray(points.size)
            var yPoints = DoubleArray(points.size)

            for (i in points.indices) {
                xPoints[i] = points[i].x
                yPoints[i] = points[i].y
            }

            for (transformer in newTransformers) {
                if (transformedPointCache.get() !== originCache) {
                    continue@mainLoop
                }

                val (newXPoints, newYPoints) = transformer.transform(xPoints, yPoints)

                xPoints = newXPoints
                yPoints = newYPoints
            }

            val newCache = PointsCache(
                newTransformers,
                newTransformers,
                xPoints,
                yPoints
            )

            if (transformedPointCache.compareAndSet(originCache, newCache)) {
                break
            }
        } while (true)

        cacheUpdateJob.setRelease(null)
    }

    fun copy(
        name: String = this.name,
        points: List<Point> = this.points,
        isHide: Boolean = this.isHide,
        functionColor: Color = this.functionColor,
        lineSize: Double = this.lineSize,
        lineType: LineType = this.lineType,
    ): ConcatenationFunction {
        return ConcatenationFunction(name, points, isHide, functionColor, lineSize, lineType).also {
            it.transformedPointCache.set(transformedPointCache.get())
        }
    }

    companion object {
        private val emptyTransformersArray = emptyArray<IAsyncPointsTransformer>()

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
