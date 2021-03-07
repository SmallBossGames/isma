package ru.nstu.isma.hsm.exp

import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 02.12.13
 * Time: 12:28
 */
class EXPParenthesis(var type: Type) : EXPToken(), Serializable {

    enum class Type : Serializable {
        OPEN, CLOSE
    }

    override fun toString(): String {
        return when (type) {
            Type.OPEN -> "("
            Type.CLOSE -> ")"
        }
    }
}