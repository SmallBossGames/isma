package ru.nstu.isma.hsm.exp

import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex on 19.02.14.
 */
open class EXPFunctionOperand(var name: String?) : EXPOperand(), Serializable {
    protected val innerArgs: MutableList<HMExpression?> = LinkedList()

    val args: List<HMExpression?>
        get() = innerArgs

    fun addArgExpression(a: HMExpression?) {
        innerArgs.add(a)
    }
}