package ru.nstu.isma.core.hsm.service;

import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.var.HMConst;
import ru.nstu.isma.core.hsm.var.HMUnnamedConst;
import ru.nstu.isma.core.hsm.var.HMVariable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * by
 * Bessonov Alex.
 * Date: 14.11.13 Time: 0:21
 */
public class PDEInitialValueCalculator {

    private static final HashSet<String> availableFunctions = new HashSet<>();

    static {
        availableFunctions.add("cos");
        availableFunctions.add("sin");
        availableFunctions.add("pow");
        availableFunctions.add("exp");
        availableFunctions.add("sqrt");
    }

    public static Double calculate(HMConst c) {
        if (isCalculated(c) || c instanceof HMUnnamedConst) {
            return c.getValue();
        }
        HMExpression constExp = c.getRightPart();
        validateExpression(constExp);
        avoidLoopCheck(constExp, null);
        Double value = calcExpression(constExp);
        c.setValue(value);
        return value;
    }

    private static void validateExpression(HMExpression exp) {
        List<EXPToken> tokens = exp.getTokens();
        // проверяем что все токены - константы или функции
        // проверяем что калькулятор "знает" применяемые функции
        for (EXPToken t : tokens) {
            if (t instanceof EXPFunctionOperand f) {
                if (!availableFunctions.contains(f.getName())) {
                    // TODO семантика
                    throw new RuntimeException("Calculator don't undastand function " + f.getName());
                }
                for (HMExpression fExp : f.getArgs()) {
                    validateExpression(fExp);
                }
            } else if (t instanceof EXPOperand) {
                HMVariable var = ((EXPOperand) t).getVariable();
                if (!(var instanceof HMConst)) {
                    // TODO обработка ошибки (семантика)
                    throw new RuntimeException("Calculator don't undastand variable " + var.getCode());
                }
            }
        }
    }

    private static void avoidLoopCheck(HMExpression expression, HashSet<String> prevCheck) {

        var current = new HashSet<String>();
        if (prevCheck != null) {
            current.addAll(prevCheck);
        }
        List<EXPToken> tokens = expression.getTokens();
        for (EXPToken token : tokens) {
            if (token instanceof EXPFunctionOperand f) {
                for(var fExp : f.getArgs()) {
                    avoidLoopCheck(fExp, current);
                }
            } else if (token instanceof EXPOperand) {
                HMConst var = (HMConst) ((EXPOperand) token).getVariable();
                if (current.contains(var.getCode())) {
                    // TODO получили петлю -  семантика обработка ошибки
                    throw new RuntimeException("Const contain loop!");
                } else {
                    if (!(var instanceof HMUnnamedConst) && var.getValue()==null) {
                        avoidLoopCheck(var.getRightPart(), current);
                    }
                }
            }
        }
    }

    private static Double calcExpression(HMExpression expression) {
        var tokens = expression.getTokens();

        var stack = new Stack<Double>();
        for (EXPToken token : tokens) {
            if (token instanceof EXPFunctionOperand f) {
                stack.push(calculateFunction(f));
            } else if (token instanceof EXPOperand o) {
                var c = (HMConst) o.getVariable();
                stack.push(calculate(c));
            } else if (token instanceof EXPOperator) {
                doOperation(stack, (EXPOperator) token);
            }
        }
        return stack.pop();
    }

    private static Double calculateFunction(EXPFunctionOperand function) {
        var args = new LinkedList<Double>();
        for (HMExpression exp : function.getArgs()) {
            args.add(calcExpression(exp));
        }
        return switch (function.getName()) {
            case "cos" -> Math.cos(Math.toRadians(args.get(0)));
            case "sin" -> Math.sin(Math.toRadians(args.get(0)));
            case "exp" -> Math.exp(args.get(0));
            case "pow" -> Math.pow(args.get(0), args.get(1));
            case "sqrt" -> Math.sqrt(args.get(0));
            default -> throw new RuntimeException("I undastand function " + function.getName());
        };
    }

    private static void doOperation(Stack<Double> values, EXPOperator o) {
        double result = 0;
        if (o.getType() != EXPOperator.Type.ALGEBRAIC) {
            // TODO семантическая ошибка
        }
        if (o.getArity() == EXPOperator.ArityType.BINARY) {
            double v1 = values.pop();
            double v2 = values.pop();
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
                default:
                    // TODO неизвестный операнд
                    break;
            }

        } else if (o.getArity() == EXPOperator.ArityType.UNARY) {
            double v1 = values.pop();
            EXPOperator.Code c = o.getCode();
            switch (c) {
                case ADDITION:
                    result = +v1;
                case SUBTRACTION:
                    result = -v1;
                    break;
            }
        }
        values.push(result);
    }

    private static boolean isCalculated(HMConst c) {
        return c.getValue() != null;
    }

}
