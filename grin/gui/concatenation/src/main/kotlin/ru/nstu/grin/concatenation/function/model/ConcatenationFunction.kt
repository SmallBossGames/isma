package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color
import kotlinx.coroutines.*
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
    val xPoints: DoubleArray,
    val yPoints: DoubleArray,
    var isHide: Boolean = false,
    var functionColor: Color,
    var lineSize: Double,
    var lineType: LineType,
) {

    private val transformedPointCache = AtomicReference(
        PointsCache(
            emptyTransformersArray,
            emptyTransformersArray,
            xPoints,
            yPoints,
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
    ) = coroutineScope {

        while (true){
            if (tryUpdateCacheCandidate(operation)){
                break
            }
        }

        val updateJob = launch(start = CoroutineStart.LAZY) {
            while (true){
                if (tryApplyCacheCandidate()){
                    cacheUpdateJob.compareAndExchangeRelease(coroutineContext.job, null)
                    break
                }
            }
        }

        val currentJob = cacheUpdateJob.compareAndExchangeAcquire(null, updateJob) ?: updateJob

        currentJob.join()
    }

    private fun tryUpdateCacheCandidate(
        operation: (Array<IAsyncPointsTransformer>) -> Array<IAsyncPointsTransformer>
    ): Boolean {
        val oldCache = transformedPointCache.get()

        val newCache = PointsCache(
            operation(oldCache.transformersCandidate.clone()),
            oldCache.transformers,
            oldCache.transformedPointsX,
            oldCache.transformedPointsY,
        )

        return transformedPointCache.compareAndSet(oldCache, newCache)
    }

    private suspend fun tryApplyCacheCandidate(): Boolean {
            val originCache = transformedPointCache.get()

            if (originCache.transformersCandidate === originCache.transformers) {
                return true
            }

            val newTransformers = originCache.transformersCandidate

            var xPoints = xPoints.clone()
            var yPoints = yPoints.clone()

            for (transformer in newTransformers) {
                if (transformedPointCache.get() !== originCache) {
                    return false
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

            return transformedPointCache.compareAndSet(originCache, newCache)
    }

    fun copy(
        name: String = this.name,
        xPoints: DoubleArray = this.xPoints,
        yPoints: DoubleArray = this.yPoints,
        isHide: Boolean = this.isHide,
        functionColor: Color = this.functionColor,
        lineSize: Double = this.lineSize,
        lineType: LineType = this.lineType,
    ): ConcatenationFunction {
        return ConcatenationFunction(name, xPoints, yPoints, isHide, functionColor, lineSize, lineType).also {
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
