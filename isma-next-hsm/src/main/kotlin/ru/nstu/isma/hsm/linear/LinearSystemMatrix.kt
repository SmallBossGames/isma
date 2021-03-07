package ru.nstu.isma.hsm.linear

import java.io.Serializable

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
abstract class LinearSystemMatrix(val size: Int) : Serializable {
    private val a = Array(size) { arrayOfNulls<HMLinearAlg?>(size) }
    private val b = arrayOfNulls<HMLinearAlg?>(size)

    protected abstract fun initMatrix()
    fun getA(y: DoubleArray): Array<DoubleArray> {
        val dA = Array(size) { DoubleArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                dA[i][j] = if (a[i][j] != null) {
                    a[i][j]!!.calculateRightMember(y)
                } else {
                    0.0
                }
            }
        }
        return dA
    }

    fun getB(y: DoubleArray): DoubleArray {
        val db = DoubleArray(size)
        for (i in 0 until size) {
            db[i] = if (b[i] != null) {
                b[i]!!.calculateRightMember(y)
            } else {
                0.0
            }
        }
        return db
    }

    fun addAElem(row: Int, col: Int, a: HMLinearAlg?) {
        this.a[row][col] = a
    }

    fun addBElem(row: Int, a: HMLinearAlg?) {
        b[row] = a
    }
}