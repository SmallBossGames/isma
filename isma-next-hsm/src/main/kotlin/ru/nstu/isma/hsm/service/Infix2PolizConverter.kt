package ru.nstu.isma.hsm.service;

import java.util.Stack;

import ru.nstu.isma.hsm.exp.*;

/**
 * Created by Bessonov Alex
 * Date: 08.11.13
 * Time: 1:01
 */
public class Infix2PolizConverter {
    protected HMExpression input;

    protected HMExpression poliz;

    public Infix2PolizConverter(HMExpression input) {
        this.input = input;
    }

    public HMExpression convert() {
        Stack<EXPToken> temp = new Stack<EXPToken>();
        poliz = new HMExpression();
        poliz.setType(HMExpression.Type.POLIZ);
        

        for (EXPToken tk : input.getTokens()) {
            if (tk instanceof EXPOperand) {
                poliz.add(tk);
                tryPopUnO(temp);
            } else if (isUnaryOperandToken(tk)) {
                temp.push(tk);
            } else if (isBinaryOperandToken(tk)) {
                EXPOperator o = (EXPOperator) tk;
                EXPOperator peekO = peekOperator(temp);

                while (peekO != null && o.getPriority() <= peekO.getPriority()) {
                    poliz.add(temp.pop());
                    peekO = peekOperator(temp);
                }
                temp.push(o);

            } else if (isOpen(tk)) {
                temp.push(tk);
            } else if (isClose(tk)) {
                EXPToken popToken = temp.pop();
                while (popToken != null && !isOpen(popToken)) {
                    poliz.add(popToken);
                    if (temp.empty()) {
                        popToken = null;
                    } else {
                        popToken = temp.pop();
                    }
                }
                tryPopUnO(temp);
            }
        }
        while (!temp.empty()) {
            poliz.add(temp.pop());
        }
        return poliz;
    }

    public HMExpression getPoliz() {
        return poliz;
    }

    protected boolean isClose(EXPToken tk) {
        return (tk instanceof EXPParenthesis && ((EXPParenthesis) tk).getType() == EXPParenthesis.Type.CLOSE);
    }

    protected boolean isOpen(EXPToken tk) {
        return (tk instanceof EXPParenthesis && ((EXPParenthesis) tk).getType() == EXPParenthesis.Type.OPEN);
    }

    protected EXPOperator peekOperator(Stack<EXPToken> temp) {
        EXPToken peekToken = temp.empty() ? null : temp.peek();
        return peekToken instanceof EXPOperator ? (EXPOperator) peekToken : null;
    }

    protected void tryPopUnO(Stack<EXPToken> temp) {
        EXPToken peekToken = temp.empty() ? null : temp.peek();
        if (isUnaryOperandToken(peekToken)) {
            poliz.add(temp.pop());
        }
    }

    private boolean isUnaryOperandToken(EXPToken t) {
        if (t instanceof EXPOperator) {
            return ((EXPOperator) t).getArity() == EXPOperator.ArityType.UNARY;
        }
        return false;
    }

    private boolean isBinaryOperandToken(EXPToken t) {
        if (t instanceof EXPOperator) {
            return ((EXPOperator) t).getArity() == EXPOperator.ArityType.BINARY;
        }
        return false;
    }





}
