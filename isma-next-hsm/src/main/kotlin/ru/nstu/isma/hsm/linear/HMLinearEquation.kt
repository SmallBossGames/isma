package ru.nstu.isma.hsm.linear

import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
class HMLinearEquation(private val size: Int) : Serializable {
    val leftPart: ArrayList<HMExpression?> = ArrayList(size)
    var rightPart: HMExpression? = null
    fun setEquationElem(`var`: HMLinearVar, expression: HMExpression?) {
        leftPart.add(`var`.columnIndex!!, expression)
    }

}