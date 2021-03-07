package ru.nstu.isma.hsm.linear

import ru.nstu.isma.print.MatrixPrint.print
import kotlin.math.abs

/**
 * Created by Дмитрий Достовалов
 * on 13.03.2015.
 */
class LinearEquationsSolver(A: Array<DoubleArray>) {
    var LU // LU-разложение матрицы A для задачи Ax=b.
            : Array<DoubleArray>
    var kd // При LU-разложении выполняется выбор ведущего элемента,
            : IntArray

    // т.е. перестановки строк. Здесь запоминаются перестановки.
    var size: Int
    private fun Dec() {
        var m: Int
        var t: Double
        kd[size - 1] = 1
        if (size > 1) {
            for (j in 0 until size - 1) {
                m = j
                for (i in j + 1 until size) if (abs(LU[i][j]) > abs(LU[m][j])) m = i
                kd[j] = m
                t = LU[m][j]
                if (m != j) {
                    kd[size - 1] = -kd[size - 1]
                    LU[m][j] = LU[j][j]
                    LU[j][j] = t
                }
                if (abs(t) < 1.0e-20) {
                    kd[size - 1] = 0
                    throw RuntimeException("Matrix A is singular!")
                }
                t = 1.0 / t
                for (i in j + 1 until size) LU[i][j] = -LU[i][j] * t
                for (i in j + 1 until size) {
                    t = LU[m][i]
                    LU[m][i] = LU[j][i]
                    LU[j][i] = t
                    if (abs(t) > 1.0e-20) for (k in j + 1 until size) LU[k][i] = LU[k][i] + LU[k][j] * t
                }
            }
        }
        if (abs(LU[size - 1][size - 1]) < 1.0e-20) {
            kd[size - 1] = 0
            throw RuntimeException("Matrix A is singular!")
        }
    }

    fun solve(b: DoubleArray?): DoubleArray {
        print("LU", LU, b!!)
        if (b.size != size) throw RuntimeException("Different size!")
        val x = DoubleArray(size)
        for (i in 0 until size) x[i] = b[i]
        var t: Double
        var m: Int
        if (size > 1) {
            for (j in 0 until size - 1) {
                m = kd[j]
                t = x[m]
                x[m] = x[j]
                x[j] = t
                for (i in j + 1 until size) x[i] = x[i] + LU[i][j] * t
            }
            for (j in size - 1 downTo 1) {
                x[j] = x[j] / LU[j][j]
                t = -x[j]
                for (i in 0 until j) x[i] = x[i] + LU[i][j] * t
            }
        }
        x[0] = x[0] / LU[0][0]
        return x
    }

    init {
        print("input", A)
        size = A.size
        LU = Array(size) { DoubleArray(size) }
        for (i in 0 until size) {
            if (A[i].size != size) throw RuntimeException("Different matrix A size!")
            for (j in 0 until size) {
                LU[i][j] = A[i][j]
            }
        }
        kd = IntArray(size)
        Dec() // Выполняется LU-разложение
    }
}