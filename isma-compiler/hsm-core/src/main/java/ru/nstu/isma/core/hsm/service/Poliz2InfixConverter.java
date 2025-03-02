package ru.nstu.isma.core.hsm.service;

import ru.nstu.isma.core.hsm.exp.EXPOperand;
import ru.nstu.isma.core.hsm.exp.EXPOperator;
import ru.nstu.isma.core.hsm.exp.EXPToken;
import ru.nstu.isma.core.hsm.exp.HMExpression;

import java.util.Stack;

/**
 * Created by Bessonov Alex
 * Date: 06.12.13
 * Time: 0:37
 */
public class Poliz2InfixConverter {
    protected HMExpression input;

    protected HMExpression infix;

    public Poliz2InfixConverter(HMExpression input) {
        this.input = input;
    }

    public HMExpression convert() {
        Stack<EXPOperand> operands = new Stack<EXPOperand>();
        for (EXPToken t : input.getTokens()) {
            if (t instanceof EXPOperand) {
                operands.push((EXPOperand) t);
            } else if (t instanceof EXPOperator) {
                if (((EXPOperator) t).getArity() == EXPOperator.ArityType.BINARY) {
                    InfixTreeNode node = new InfixTreeNode();
                    EXPOperand o1 = operands.pop();
                    EXPOperand o2 = operands.pop();
                    if (o1 instanceof InfixTreeNode) {
                        if (((InfixTreeNode) o1).getO().getPriority() <= ((EXPOperator) t).getPriority()) {
                            ((InfixTreeNode) o1).setNeedParant(true);
                        }
                    }
                    if (o2 instanceof InfixTreeNode) {
                        if (((InfixTreeNode) o2).getO().getPriority() <= ((EXPOperator) t).getPriority()) {
                            ((InfixTreeNode) o2).setNeedParant(true);
                        }
                    }
                    node.setLeft(o2);
                    node.setRight(o1);
                    node.setO((EXPOperator) t);
                    operands.push(node);
                } else if (((EXPOperator) t).getArity() == EXPOperator.ArityType.UNARY) {
                    InfixTreeNodeUnary node = new InfixTreeNodeUnary();
                    EXPOperand o1 = operands.pop();
                    if (o1 instanceof InfixTreeNode) {
                        if (((InfixTreeNode) o1).getO().getPriority() <= ((EXPOperator) t).getPriority()) {
                            ((InfixTreeNode) o1).setNeedParant(true);
                        }
                    }
                    node.setLeft(o1);
                    node.setO((EXPOperator) t);
                    operands.push(node);
                }

            }
        }
        if (operands.size() > 1) {
            throw new RuntimeException("Poliz2InfixConverter: operator size at the end more then 1!");
        }

        infix = new HMExpression();

        EXPOperand oper = operands.pop();
        if (oper instanceof InfixTreeNode) {
            ((InfixTreeNode) oper).makeExpression(infix);
        } else {
            infix.add(oper);
        }
        return infix;
    }

    protected class InfixTreeNode extends EXPOperand {
        protected boolean needParant = false;

        protected EXPOperator o;

        protected EXPOperand left;

        protected EXPOperand right;

        public boolean isNeedParant() {
            return needParant;
        }

        public void setNeedParant(boolean needParant) {
            this.needParant = needParant;
        }

        public EXPOperator getO() {
            return o;
        }

        public void setO(EXPOperator o) {
            this.o = o;
        }

        public EXPOperand getLeft() {
            return left;
        }

        public void setLeft(EXPOperand left) {
            this.left = left;
        }

        public EXPOperand getRight() {
            return right;
        }

        public void setRight(EXPOperand right) {
            this.right = right;
        }

        protected HMExpression makeExpression(HMExpression e) {
            if (needParant) {
                e.openParenthesis();
            }
            if (left instanceof InfixTreeNode) {
                ((InfixTreeNode) left).makeExpression(e);
            } else {
                e.add(left);
            }
            e.add(o);
            if (right instanceof InfixTreeNode) {
                ((InfixTreeNode) right).makeExpression(e);
            } else {
                e.add(right);
            }
            if (needParant) {
                e.closeParenthesis();
            }
            return e;
        }

    }

    protected class InfixTreeNodeUnary extends InfixTreeNode {
        @Override
        protected HMExpression makeExpression(HMExpression e) {
            e.add(o);

            if (needParant) {
                e.openParenthesis();
            }
            if (left instanceof InfixTreeNode) {
                ((InfixTreeNode) left).makeExpression(e);
            } else {
                e.add(left);
            }

            if (needParant) {
                e.closeParenthesis();
            }
            return e;
        }
    }

}
