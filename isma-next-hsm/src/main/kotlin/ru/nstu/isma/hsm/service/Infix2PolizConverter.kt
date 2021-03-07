package ru.nstu.isma.hsm.service

import ru.nstu.isma.hsm.exp.*
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 08.11.13
 * Time: 1:01
 */
class Infix2PolizConverter(protected var input: HMExpression) {
    var poliz: HMExpression? = null
        protected set

    fun convert(): HMExpression {
        val temp = Stack<EXPToken>()
        poliz = HMExpression()
        poliz!!.type = HMExpression.Type.POLIZ
        for (tk in input.tokens) {
            if (tk is EXPOperand) {
                poliz!!.add(tk)
                tryPopUnO(temp)
            } else if (isUnaryOperandToken(tk)) {
                temp.push(tk)
            } else if (isBinaryOperandToken(tk)) {
                val o = tk as EXPOperator
                var peekO = peekOperator(temp)
                while (peekO != null && o.priority <= peekO.priority) {
                    poliz!!.add(temp.pop())
                    peekO = peekOperator(temp)
                }
                temp.push(o)
            } else if (isOpen(tk)) {
                temp.push(tk)
            } else if (isClose(tk)) {
                var popToken = temp.pop()
                while (popToken != null && !isOpen(popToken)) {
                    poliz!!.add(popToken)
                    popToken = if (temp.empty()) {
                        null
                    } else {
                        temp.pop()
                    }
                }
                tryPopUnO(temp)
            }
        }
        while (!temp.empty()) {
            poliz!!.add(temp.pop())
        }
        return poliz!!
    }

    protected fun isClose(tk: EXPToken?): Boolean {
        return tk is EXPParenthesis && tk.type == EXPParenthesis.Type.CLOSE
    }

    protected fun isOpen(tk: EXPToken?): Boolean {
        return tk is EXPParenthesis && tk.type == EXPParenthesis.Type.OPEN
    }

    protected fun peekOperator(temp: Stack<EXPToken>): EXPOperator? {
        val peekToken = if (temp.empty()) null else temp.peek()
        return if (peekToken is EXPOperator) peekToken else null
    }

    protected fun tryPopUnO(temp: Stack<EXPToken>) {
        val peekToken = if (temp.empty()) null else temp.peek()
        if (isUnaryOperandToken(peekToken)) {
            poliz!!.add(temp.pop())
        }
    }

    private fun isUnaryOperandToken(t: EXPToken?): Boolean {
        return if (t is EXPOperator) {
            t.arity == EXPOperator.ArityType.UNARY
        } else false
    }

    private fun isBinaryOperandToken(t: EXPToken?): Boolean {
        return if (t is EXPOperator) {
            t.arity == EXPOperator.ArityType.BINARY
        } else false
    }
}