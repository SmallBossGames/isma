package ru.isma.next.domain.models

data class SimulationPoint(
    val x: Double,
    val yForDe: DoubleArray,
    val rhs: Array<DoubleArray>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SimulationPoint
        if (that.x.compareTo(x) != 0) return false
        return if (!yForDe.contentEquals(that.yForDe)) false else rhs[1].contentEquals(that.rhs[1])
    }

    override fun hashCode(): Int {
        var result: Int
        val temp: Long = java.lang.Double.doubleToLongBits(x)

        result = (temp xor (temp ushr 32)).toInt()
        result = 31 * result + yForDe.contentHashCode()
        result = 31 * result + rhs[1].contentHashCode()
        return result
    }
}
