package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point
import java.util.*

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
    var mirrorDetails: MirrorDetails = MirrorDetails(),
    var derivativeDetails: DerivativeDetails? = null,
    var waveletDetails: WaveletDetails? = null,
) : Cloneable {
    var pixelsToDraw: Pair<DoubleArray, DoubleArray>? = null

    public override fun clone(): ConcatenationFunction {
        return ConcatenationFunction(
            id = id,
            name = name,
            points = points.map { it.copy() },
            functionColor = functionColor,
            lineSize = lineSize,
            lineType = lineType,
            mirrorDetails = mirrorDetails.copy(),
            derivativeDetails = derivativeDetails?.copy(),
            waveletDetails = waveletDetails?.copy(),
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
