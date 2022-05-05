package ru.nstu.grin.math

import kotlin.math.absoluteValue
import kotlin.math.sign

object IntersectionSearcher {

    fun findIntersections(
        firstX: DoubleArray,
        firstY: DoubleArray,
        secondX: DoubleArray,
        secondY: DoubleArray
    ): List<Pair<Double, Double>> {
        val resultIndices = mutableListOf<Pair<Double, Double>>()

        for (i in 0 until firstX.size - 1) {
            for (j in 0 until secondX.size - 1) {
                val leftCenterX = firstX[i + 1] - firstX[i]
                val leftCenterY = firstY[i + 1] - firstY[i]

                val leftUpX = secondX[j] - firstX[i]
                val leftUpY = secondY[j] - firstY[i]

                val leftDownX = secondX[j + 1] - firstX[i]
                val leftDownY = secondY[j + 1] - firstY[i]

                val leftUpCross = cross(leftCenterX, leftCenterY, leftUpX, leftUpY)
                val leftDownCross = cross(leftCenterX, leftCenterY, leftDownX, leftDownY)

                if (leftUpCross.sign == leftDownCross.sign) {
                    continue
                }

                val rightCenterX = secondX[j + 1] - secondX[j]
                val rightCenterY = secondY[j + 1] - secondY[j]

                val rightUpX = firstX[i] - secondX[j]
                val rightUpY = firstY[i] - secondY[j]

                val rightDownX = firstX[i + 1] - secondX[j]
                val rightDownY = firstY[i + 1] - secondY[j]

                val rightUpCross = cross(rightCenterX, rightCenterY, rightUpX, rightUpY)
                val rightDownCross = cross(rightCenterX, rightCenterY, rightDownX, rightDownY)

                if (rightUpCross.sign == rightDownCross.sign) {
                    continue
                }


                val intersectionPointX =
                    firstX[i] + leftCenterX * leftUpCross.absoluteValue / (leftDownCross - leftUpCross).absoluteValue
                val intersectionPointY =
                    firstY[i] + leftCenterY * leftUpCross.absoluteValue / (leftDownCross - leftUpCross).absoluteValue

                resultIndices.add(Pair(intersectionPointX, intersectionPointY))
            }
        }

        return resultIndices
    }

    private fun cross(x1: Double, y1: Double, x2: Double, y2: Double) = x1 * y2 - x2 * y1
}