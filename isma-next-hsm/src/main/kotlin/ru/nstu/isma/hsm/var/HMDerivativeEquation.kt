package ru.nstu.isma.hsm.`var`

import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:31
 */
open class HMDerivativeEquation(code: String?) : HMEquation(), Serializable {
    var initial: HMConst?
    val initialValue: Double?
        get() {
            if (initial == null) {
                return 0.0
            }
            return if (initial?.value == null) {
                0.0
            } else {
                initial!!.value
            }
        }

    fun setInitial(initial: Double) {
        this.initial = HMUnnamedConst(initial)
    }

    init {
        this.code = code
        initial = HMUnnamedConst(0.0)
    }
}