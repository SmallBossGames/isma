package ru.nstu.isma.hsm.exp.cond

import ru.nstu.isma.hsm.exp.*
import ru.nstu.isma.hsm.`var`.*
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:56
 */
class HMAtom : HMVariable(), Serializable {
    var left: HMVariable? = null
    var right: HMVariable? = null
    var op: EXPOperator? = null
}