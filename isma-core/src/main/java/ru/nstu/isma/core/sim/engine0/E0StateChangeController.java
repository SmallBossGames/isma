package ru.nstu.isma.core.sim.engine0;

import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.exp.EXPFunctionOperand;
import ru.nstu.isma.core.hsm.exp.EXPOperand;
import ru.nstu.isma.core.hsm.exp.EXPOperator;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.var.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * by
 * Bessonov Alex.
 * Date: 4.10.14
 */
public class E0StateChangeController {
    private IsmaErrorList errors;
    private E0SimulationContext ctx;

    public E0StateChangeController(IsmaErrorList errors, E0SimulationContext ctx) {
        this.ctx = ctx;
        this.errors = errors;
    }

    public Object calculate(HMExpression expression) {
        Stack<Object> stack = new Stack<>();
        expression.getTokens().stream().forEach(token -> {
            if (token instanceof EXPFunctionOperand) {
                EXPFunctionOperand f = (EXPFunctionOperand) token;
                stack.push(calculateFunction(f));
            } else if (token instanceof EXPOperand) {
                HMVariable var = ((EXPOperand) token).getVariable();
                if (var instanceof HMUnnamedConst) {
                    stack.push(((HMUnnamedConst) var).getValue());
                } else if (var instanceof HMConst || var instanceof HMDerivativeEquation || var instanceof HMAlgebraicEquation) {
                    stack.push(ctx.getValue(var.getCode(), true));
                } else {
                    throw new RuntimeException("Unknown variable " + var.getCode());
                }
            } else if (token instanceof EXPOperator) {
                doOperation(stack, (EXPOperator) token);
            }
        });
        return (Boolean) stack.pop();
    }

    private void doOperation(Stack<Object> values, EXPOperator o) {
        Object result = 0;
        if (o.getArity() == EXPOperator.ArityType.BINARY) {
            double v1 = (Double) values.pop();
            double v2 = (Double) values.pop();
            EXPOperator.Code c = o.getCode();
            switch (c) {
                case ADDITION:
                    result = v1 + v2;
                    break;
                case DIVISION:
                    result = v2 / v1;
                    break;
                case SUBTRACTION:
                    result = v2 - v1;
                    break;
                case MULTIPLICATION:
                    result = v1 * v2;
                    break;
                case LESS_OR_EQUAL:
                    result = v2 <= v1;
                    break;

                case LESS_THAN:
                    result = v2 < v1;
                    break;

                case GREATER_OR_EQUAL:
                    result = v2 >= v1;
                    break;

                case GREATER_THAN:
                    result = v2 > v1;
                    break;

                case EQUAL:
                    result = v2 == v1;
                    break;

                case NOT_EQUAL:
                    result = v2 != v1;
                    break;

                default:
                    throw new RuntimeException("в выражении содержится неизвестный  оператор " + o.toString());
            }

        } else if (o.getArity() == EXPOperator.ArityType.UNARY) {
            double v1 = (double) values.pop();
            EXPOperator.Code c = o.getCode();
            switch (c) {
                case ADDITION:
                    break;
                case SUBTRACTION:
                    result = -v1;
                    break;
            }
        }
        values.push(result);
    }


    private Double calculateFunction(EXPFunctionOperand function) {
        List<Double> args = new LinkedList<>();
        for (HMExpression exp : function.getArgs()) {
            args.add((Double)calculate(exp));
        }
        Double result;
        if (function.getName().equals("cos")) {
            result = Math.cos(args.get(0));//Math.cos(Math.toRadians(args.get(0)));
        } else if (function.getName().equals("sin")) {
            result =Math.sin(args.get(0)); //Math.sin(Math.toRadians(args.get(0)));
        } else if (function.getName().equals("exp")) {
            result = Math.exp(args.get(0));
        } else if (function.getName().equals("pow")) {
            result = Math.pow(args.get(0), args.get(1));
        } else if (function.getName().equals("abs")) {
            result = Math.abs(args.get(0));
        } else {
            throw new RuntimeException("I understand function " + function.getName());
        }
        return result;
    }
}
