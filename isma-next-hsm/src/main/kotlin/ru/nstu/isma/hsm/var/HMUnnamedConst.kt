package ru.nstu.isma.hsm.`var`

import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable
import java.util.*

/**
 * by
 * Bessonov Alex.
 * Date: 14.11.13 Time: 0:23
 */
class HMUnnamedConst(value: Double) : HMConst("unnamed_const@" + UUID.randomUUID().toString()), Serializable {
    override var rightPart: HMExpression?
        get() = HMExpression.getConst(value!!)
        set(rightPart) {
            super.rightPart = rightPart
        }

    override fun toString(): String {
        return value.toString()
    }

    init {
        this.value = value
    }
}