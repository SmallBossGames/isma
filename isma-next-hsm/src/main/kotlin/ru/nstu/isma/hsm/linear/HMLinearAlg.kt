package ru.nstu.isma.hsm.linear

import java.io.Serializable
import java.util.function.Function

/**
 * Created by Bessonov Alex
 * on 25.03.2015.
 */
class HMLinearAlg(
    val index: Int,
    private val function: Function<DoubleArray, Double>,
    private var description: String
) : Serializable {
    fun calculateRightMember(y: DoubleArray): Double {
        return function.apply(y)
    }

    fun setDescription(description: String) {
        this.description = description
    }

    override fun toString(): String {
        return description
    }
}