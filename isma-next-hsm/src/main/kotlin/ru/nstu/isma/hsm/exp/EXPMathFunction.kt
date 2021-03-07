package ru.nstu.isma.hsm.exp

import ru.nstu.isma.hsm.`var`.HMVariable
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * on 24.03.2015.
 */
class EXPMathFunction(`var`: HMVariable) : EXPFunctionOperand(`var`.code), Serializable {
    init {
        variable = `var`
    }
}