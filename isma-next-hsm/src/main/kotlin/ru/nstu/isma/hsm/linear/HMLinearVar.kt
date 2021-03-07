package ru.nstu.isma.hsm.linear

import ru.nstu.isma.hsm.`var`.HMVariable
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
class HMLinearVar : HMVariable, Serializable {
    var columnIndex: Int? = null

    constructor() {}
    constructor(code: String?) : super(code) {}
}