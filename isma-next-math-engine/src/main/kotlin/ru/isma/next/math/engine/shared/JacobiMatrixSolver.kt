package ru.isma.next.math.engine.shared

import kotlin.math.abs
import kotlin.math.max

//Non-concurrent
class JacobiMatrixSolver(size: Int) {
    val vectorBuffer1 = DoubleArray(size)
    val vectorBuffer2 = DoubleArray(size)

    inline fun solve(y: DoubleArray, outJacobiMatrix: Matrix2D, equations: StationaryODE){
        equations(y, vectorBuffer1)
        y.copyInto(vectorBuffer2)
        for (i in vectorBuffer2.indices){
            val r = max(1e-14, 1e-7 * abs(y[i]))
            val jacobiColumn = outJacobiMatrix.columns[i]
            vectorBuffer2[i] += r
            equations(vectorBuffer2, jacobiColumn)
            for (j in jacobiColumn.indices){
                jacobiColumn[j] = (jacobiColumn[j] - vectorBuffer1[j]) / r
            }
            vectorBuffer2[i] = y[i]
        }
    }

    inline fun solve(t: Double, y: DoubleArray, outJacobiMatrix: Matrix2D, equations: NonStationaryODE){
        equations(t, y, vectorBuffer1)
        y.copyInto(vectorBuffer2)
        for (i in vectorBuffer2.indices){
            val r = max(1e-14, 1e-7 * abs(y[i]))
            val jacobiColumn = outJacobiMatrix.columns[i]
            vectorBuffer2[i] += r
            equations(t, vectorBuffer2, jacobiColumn)
            for (j in jacobiColumn.indices){
                jacobiColumn[j] = (jacobiColumn[j] - vectorBuffer1[j]) / r
            }
            vectorBuffer2[i] = y[i]
        }
    }
}