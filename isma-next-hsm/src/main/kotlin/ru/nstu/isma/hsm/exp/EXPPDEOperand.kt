package ru.nstu.isma.hsm.exp

import ru.nstu.isma.hsm.`var`.HMVariable
import ru.nstu.isma.hsm.`var`.pde.HMSampledSpatialVariable
import ru.nstu.isma.hsm.`var`.pde.HMSpatialVariable
import java.io.Serializable

/**
 * by
 * Bessonov Alex.
 * Date: 04.12.13 Time: 1:51
 */
class EXPPDEOperand(variable: HMVariable?) : EXPOperand(variable), Serializable {
    var firstSpatialVariable: HMSpatialVariable? = null
    var secondSpatialVariable: HMSpatialVariable? = null
    var order = Order.ONE
    val sampledFirstSpatialVar: HMSampledSpatialVariable
        get() = if (firstSpatialVariable is HMSampledSpatialVariable) {
            firstSpatialVariable as HMSampledSpatialVariable
        } else {
            throw RuntimeException("Variable " + firstSpatialVariable!!.code + " can't be sampled!")
        }
    val sampledSecondSpatialVar: HMSampledSpatialVariable
        get() = if (secondSpatialVariable is HMSampledSpatialVariable) {
            secondSpatialVariable as HMSampledSpatialVariable
        } else {
            throw RuntimeException("Variable " + secondSpatialVariable!!.code + " can't be sampled!")
        }
    val isMixedPDEOperand: Boolean
        get() = firstSpatialVariable != null && secondSpatialVariable != null

    enum class Order(var i: Int) : Serializable {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

        companion object {
            fun getByCode(code: Int?): Order? {
                return when (code) {
                    1 -> ONE
                    2 -> TWO
                    3 -> THREE
                    4 -> FOUR
                    5 -> FIVE
                    else -> null
                }
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder(variable!!.code)
        sb.append(" BY ")
        sb.append(sampledFirstSpatialVar.code)
        sb.append(" ON ")
        sb.append(order.name)
        return sb.toString()
    }
}