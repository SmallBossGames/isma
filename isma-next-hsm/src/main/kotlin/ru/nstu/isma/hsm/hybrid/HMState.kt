package ru.nstu.isma.hsm.hybrid

import ru.nstu.isma.hsm.`var`.HMVariableTable
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:32
 */
open class HMState(code: String) : Serializable {
    var code: String
    var variables: HMVariableTable
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val hmState = other as HMState
        return code == hmState.code
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }

    init {
        variables = HMVariableTable()
        this.code = code
    }
}