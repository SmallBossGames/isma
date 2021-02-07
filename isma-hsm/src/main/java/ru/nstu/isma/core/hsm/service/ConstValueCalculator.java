package ru.nstu.isma.core.hsm.service;

import error.IsmaError;
import error.IsmaErrorList;
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
public class ConstValueCalculator {
    private final IsmaErrorList errors;

    public ConstValueCalculator(IsmaErrorList errors) {
        this.errors = errors;
    }

    public Double calculateConst(HMExpression expression) {
        HMConst c = new HMConst("cc");
        c.setRightPart(expression);
        return calculateConst(c);
    }

    public Double calculateConst(HMConst c) {
        if (isCalculated(c)) {
            return c.getValue();
        }
        Double value = null;
        try {
            validateExpression(c.getRightPart());
            avoidLoopCheck(c, null);
            value = runCaltulator(c.getRightPart());
            c.setValue(value);
        } catch (Exception e) {
            e.printStackTrace();
            IsmaError err = new IsmaError("ошибка при рассчете константы: " + c.getCode() + "  " + e.getMessage());
            err.setType(IsmaError.Type.SEM);
            if (errors != null)
                errors.add(err);
            value = 0d;
        }

        return value;
    }

    public void validateExpression(HMExpression expression) {
        // проверяем что все токены - константы
        for (EXPToken t : expression.getTokens()) {
            if (t instanceof EXPFunctionOperand) {
                ((EXPFunctionOperand) t).getArgs().stream()
                        .forEach(exp -> validateExpression(exp));
            } else if (t instanceof EXPOperand) {
                HMVariable var = ((EXPOperand) t).getVariable();
                if (!(var instanceof HMConst)) {
                    String c = var != null ? var.getCode() : "NULL";
                    throw new RuntimeException("в выражении содержится не константное значение: " + c);
                }
            }
        }
    }

    private void avoidLoopCheck(HMConst c, HashSet<String> prevCheck) {

        if (c instanceof HMUnnamedConst) {
            return;
        }
        HashSet<String> current = new HashSet<String>();
        current.add(c.getCode());
        if (prevCheck != null) {
            for (String s : prevCheck) {
                current.add(s);
            }
        }
        List<EXPToken> tokens = c.getRightPart().getTokens();
        for (EXPToken token : tokens) {
            if (token instanceof EXPOperand && !(token instanceof EXPFunctionOperand)) {
                HMConst var = (HMConst) ((EXPOperand) token).getVariable();

                if (current.contains(var.getCode())) {
                    // получили петлю -  семантика обработка ошибки
                    // TODO тщательно проверить
                    if (var instanceof HMUnnamedConst)
                        var = c;
                    throw new RuntimeException("в выражении содержится петля из-за переменной " + var.getCode());
                } else {
//                    current.add(var.getCode());
                    avoidLoopCheck(var, current);
                }
            }
        }
    }

    private Double runCaltulator(HMExpression expression) {
        Stack<Double> stack = new Stack<Double>();
        for (EXPToken token : expression.getTokens()) {
            if (token instanceof EXPFunctionOperand) {
                Double f = calculateFunction((EXPFunctionOperand) token);
                stack.push(f);
            } else if (token instanceof EXPOperand) {
                HMVariable var = ((EXPOperand) token).getVariable();
                stack.push(calculateConst((HMConst) var));
            } else if (token instanceof EXPOperator) {
                doOperation(stack, (EXPOperator) token);
            }
        }
        return stack.pop();
    }

    private void doOperation(Stack<Double> values, EXPOperator o) {
        double result = 0;
        if (o.getType() != EXPOperator.Type.ALGEBRAIC) {
            // семантическая ошибка
            throw new RuntimeException("в выражении содержится не алгебраический оператор " + o.toString());
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
                    throw new RuntimeException("в выражении содержится неизвестный  оператор " + o.toString());
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
        if (c == null)
            System.out.println();
        return c.getValue() != null;
    }


    private Double calculateFunction(EXPFunctionOperand function) {
        List<Double> args = new LinkedList<>();
        for (HMExpression exp : function.getArgs()) {
            args.add(runCaltulator(exp));
        }
        Double result;
        if (function.getName().equals("cos")) {
            result = Math.cos(args.get(0));//Math.cos(Math.toRadians(args.get(0)));
        } else if (function.getName().equals("sin")) {
            result = Math.sin(args.get(0)); //Math.sin(Math.toRadians(args.get(0)));
        } else if (function.getName().equals("exp")) {
            result = Math.exp(args.get(0));
        } else if (function.getName().equals("pow")) {
            result = Math.pow(args.get(0), args.get(1));
        } else if (function.getName().equals("abs")) {
            result = Math.abs(args.get(0));
        } else if (function.getName().equals("sqrt")) {
            result = Math.sqrt(args.get(0));
        } else {
            throw new RuntimeException("I understand function " + function.getName());
        }
        return result;
    }

}
