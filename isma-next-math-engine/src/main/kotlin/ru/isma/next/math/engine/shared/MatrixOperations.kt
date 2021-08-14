package ru.isma.next.math.engine.shared

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

private const val rMin = 1e-14

fun findJacobiMatrix(inY:DoubleArray,
                            t: Double,
                            equations: (t: Double, inY: DoubleArray, outF: DoubleArray) -> Unit,
                            outJacobiMatrix: Matrix2D
) {
    val tempY = inY.clone()
    val tempF = DoubleArray(tempY.size)
    val tempFWithDelta = DoubleArray(tempY.size)

    for (i in tempY.indices){

        val r = max(rMin, (sqrt(rMin) * abs(tempY[i])))
        tempY[i] += r

        equations(t, inY, tempF)
        equations(t, tempY, tempFWithDelta)

        for (j in tempY.indices){
            outJacobiMatrix[j, i] = (tempFWithDelta[j] - tempF[j]) / r
        }

        tempY[i] = inY[i]
    }
}

fun multipleMatrix2D(constant: Double, matrix: Matrix2D, outMatrix: Matrix2D){
    for (i in outMatrix.indices)
        for (j in outMatrix.indices)
            outMatrix[i, j] = matrix[i, j] * constant
}

fun sumMatrix2D(m1: Matrix2D, m2: Matrix2D, outMatrix: Matrix2D){
    for (i in outMatrix.indices)
        for (j in outMatrix.indices)
            outMatrix[i, j] = m1[i, j] + m2[i, j]
}

fun subtractMatrix2D(m1: Matrix2D, m2: Matrix2D, outMatrix: Matrix2D){
    for (i in outMatrix.indices)
        for (j in outMatrix.indices)
            outMatrix[i, j] = m1[i, j] - m2[i, j]
}

fun createUnitMatrix2D(size: Int) : Matrix2D
{
    val e = Matrix2D(size)

    for (i in e.indices)
        e[i, i] = 1.0

    return e
}