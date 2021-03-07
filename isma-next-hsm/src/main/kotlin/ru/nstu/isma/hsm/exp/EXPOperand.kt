package ru.nstu.isma.hsm.exp

import ru.nstu.isma.hsm.`var`.HMVariable
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 0:57
 */
open class EXPOperand : EXPToken, Serializable {
    var variable: HMVariable? = null

    constructor() {}
    constructor(variable: HMVariable?) {
        this.variable = variable
    }

    override fun toString(): String {
        return variable.toString()
    }
}