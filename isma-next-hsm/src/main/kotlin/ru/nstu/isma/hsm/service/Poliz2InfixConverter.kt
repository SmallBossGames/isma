package ru.nstu.isma.hsm.service

import ru.nstu.isma.hsm.exp.EXPOperand
import ru.nstu.isma.hsm.exp.EXPOperator
import ru.nstu.isma.hsm.exp.HMExpression
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 06.12.13
 * Time: 0:37
 */
class Poliz2InfixConverter(protected var input: HMExpression?) {
    protected var infix: HMExpression? = null
    fun convert(): HMExpression {
        val operands = Stack<EXPOperand>()
        for (t in input!!.tokens) {
            if (t is EXPOperand) {
                operands.push(t)
            } else if (t is EXPOperator) {
                if (t.arity == EXPOperator.ArityType.BINARY) {
                    val node = InfixTreeNode()
                    val o1 = operands.pop()
                    val o2 = operands.pop()
                    if (o1 is InfixTreeNode) {
                        if (o1.o!!.priority <= t.priority) {
                            o1.isNeedParant = true
                        }
                    }
                    if (o2 is InfixTreeNode) {
                        if (o2.o!!.priority <= t.priority) {
                            o2.isNeedParant = true
                        }
                    }
                    node.left = o2
                    node.right = o1
                    node.o = t
                    operands.push(node)
                } else if (t.arity == EXPOperator.ArityType.UNARY) {
                    val node = InfixTreeNodeUnary()
                    val o1 = operands.pop()
                    if (o1 is InfixTreeNode) {
                        if (o1.o!!.priority <= t.priority) {
                            o1.isNeedParant = true
                        }
                    }
                    node.left = o1
                    node.o = t
                    operands.push(node)
                }
            }
        }
        if (operands.size > 1) {
            throw RuntimeException("Poliz2InfixConverter: operator size at the end more then 1!")
        }
        infix = HMExpression()
        val oper = operands.pop()
        if (oper is InfixTreeNode) {
            oper.makeExpression(infix!!)
        } else {
            infix!!.add(oper)
        }
        return infix!!
    }

    protected open inner class InfixTreeNode : EXPOperand() {
        var isNeedParant = false
        var o: EXPOperator? = null
        var left: EXPOperand? = null
        var right: EXPOperand? = null
        open fun makeExpression(e: HMExpression): HMExpression {
            if (isNeedParant) {
                e.openParenthesis()
            }
            if (left is InfixTreeNode) {
                (left as InfixTreeNode).makeExpression(e)
            } else {
                e.add(left)
            }
            e.add(o)
            if (right is InfixTreeNode) {
                (right as InfixTreeNode).makeExpression(e)
            } else {
                e.add(right)
            }
            if (isNeedParant) {
                e.closeParenthesis()
            }
            return e
        }
    }

    protected inner class InfixTreeNodeUnary : InfixTreeNode() {
        override fun makeExpression(e: HMExpression): HMExpression {
            e.add(o)
            if (isNeedParant) {
                e.openParenthesis()
            }
            if (left is InfixTreeNode) {
                (left as InfixTreeNode).makeExpression(e)
            } else {
                e.add(left)
            }
            if (isNeedParant) {
                e.closeParenthesis()
            }
            return e
        }
    }
}