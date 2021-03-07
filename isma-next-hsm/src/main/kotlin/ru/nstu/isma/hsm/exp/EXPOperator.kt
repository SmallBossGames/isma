package ru.nstu.isma.hsm.exp

import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 1:01
 */
class EXPOperator(var code: Code?, var type: Type, var arity: ArityType, var priority: Int) : EXPToken(), Serializable {
    override fun toString(): String {
        when (code) {
            Code.ADDITION -> return "+"
            Code.SUBTRACTION -> return "-"
            Code.MULTIPLICATION -> return "*"
            Code.DIVISION -> return "/"
            Code.INVOLUTION -> return "^"
            Code.GREATER_THAN -> return ">"
            Code.LESS_THAN -> return "<"
            Code.GREATER_OR_EQUAL -> return ">="
            Code.LESS_OR_EQUAL -> return "<="
            Code.EQUAL -> return "=="
            Code.NOT_EQUAL -> return "!="
            Code.AND -> return "and"
            Code.OR -> return "or"
            Code.NOT -> return "!"
        }
        return super.toString()
    }

    enum class ArityType : Serializable {
        UNARY, BINARY, TRINAR
    }

    enum class Type : Serializable {
        COMPARE, ALGEBRAIC, LOGICAL
    }

    // аналог символьного отображения
    enum class Code : Serializable {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, INVOLUTION, GREATER_THAN, LESS_THAN, GREATER_OR_EQUAL, LESS_OR_EQUAL, EQUAL, NOT_EQUAL, AND, OR, NOT
    }

    companion object {
        fun un_plus(): EXPOperator {
            return EXPOperator(Code.ADDITION, Type.ALGEBRAIC, ArityType.UNARY, 92)
        }

        fun un_minus(): EXPOperator {
            return EXPOperator(Code.SUBTRACTION, Type.ALGEBRAIC, ArityType.UNARY, 91)
        }

        operator fun not(): EXPOperator {
            return EXPOperator(Code.NOT, Type.LOGICAL, ArityType.UNARY, 90)
        }

        fun mult(): EXPOperator {
            return EXPOperator(Code.MULTIPLICATION, Type.ALGEBRAIC, ArityType.BINARY, 80)
        }

        fun div(): EXPOperator {
            return EXPOperator(Code.DIVISION, Type.ALGEBRAIC, ArityType.BINARY, 80)
        }

        fun add(): EXPOperator {
            return EXPOperator(Code.ADDITION, Type.ALGEBRAIC, ArityType.BINARY, 70)
        }

        fun sub(): EXPOperator {
            return EXPOperator(Code.SUBTRACTION, Type.ALGEBRAIC, ArityType.BINARY, 70)
        }

        fun less(): EXPOperator {
            return EXPOperator(Code.LESS_THAN, Type.COMPARE, ArityType.BINARY, 60)
        }

        fun less_equal(): EXPOperator {
            return EXPOperator(Code.LESS_OR_EQUAL, Type.COMPARE, ArityType.BINARY, 60)
        }

        fun greater(): EXPOperator {
            return EXPOperator(Code.GREATER_THAN, Type.COMPARE, ArityType.BINARY, 50)
        }

        fun greater_equal(): EXPOperator {
            return EXPOperator(Code.GREATER_OR_EQUAL, Type.COMPARE, ArityType.BINARY, 50)
        }

        fun equal(): EXPOperator {
            return EXPOperator(Code.EQUAL, Type.COMPARE, ArityType.BINARY, 40)
        }

        fun not_equal(): EXPOperator {
            return EXPOperator(Code.NOT_EQUAL, Type.COMPARE, ArityType.BINARY, 30)
        }

        fun and(): EXPOperator {
            return EXPOperator(Code.AND, Type.LOGICAL, ArityType.BINARY, 20)
        }

        fun or(): EXPOperator {
            return EXPOperator(Code.OR, Type.LOGICAL, ArityType.BINARY, 10)
        }
    }
}