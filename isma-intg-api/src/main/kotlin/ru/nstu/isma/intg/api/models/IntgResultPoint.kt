package ru.nstu.isma.intg.api.models

import ru.nstu.isma.intg.api.calcmodel.DaeSystem

/**
 * @author Maria
 * @since 28.03.2016
 */
class IntgResultPoint(
    val x: Double,
    val yForDe: DoubleArray,
    val rhs: Array<DoubleArray>, // TODO: можно сохранять только алгебраические
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as IntgResultPoint
        if (that.x.compareTo(x) != 0) return false
        return if (!yForDe.contentEquals(that.yForDe)) false else rhs[DaeSystem.RHS_AE_PART_IDX].contentEquals(that.rhs[DaeSystem.RHS_AE_PART_IDX])
    }

    override fun hashCode(): Int {
        var result: Int
        val temp: Long = java.lang.Double.doubleToLongBits(x)

        result = (temp xor (temp ushr 32)).toInt()
        result = 31 * result + yForDe.contentHashCode()
        result = 31 * result + rhs[DaeSystem.RHS_AE_PART_IDX].contentHashCode()
        return result
    }
}