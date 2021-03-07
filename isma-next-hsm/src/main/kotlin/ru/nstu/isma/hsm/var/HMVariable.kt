package ru.nstu.isma.hsm.`var`

import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:18
 */
open class HMVariable : Serializable {
    var code: String? = null

    constructor() {}
    constructor(code: String?) {
        this.code = code
    }

    override fun toString(): String {
        return code!!
    }
}