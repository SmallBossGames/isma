package ru.nstu.isma.core.sim.engine0;

import error.IsmaError;
import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.exp.EXPFunctionOperand;
import ru.nstu.isma.core.hsm.exp.EXPOperand;
import ru.nstu.isma.core.hsm.exp.EXPOperator;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.var.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * by
 * Bessonov Alex.
 * Date: 4.10.14
 */
public class E0Calculator {
    private IsmaErrorList errors;

    private E0SimulationContext ctx;

    public E0Calculator(IsmaErrorList errors) {
        this.errors = errors;
    }

    public Double calculate(HMEquation e, E0SimulationContext ctx, boolean isFirst) {
        this.ctx = ctx;
        Double result = 0d;
        try {
            avoidLoopCheck(e, null);

            if (e instanceof HMDerivativeEquation) {
                result = calculate(e.getRightPart(), true);
                result = ctx.getValue(e.getCode(), false) + ctx.getStep() * result;
            } else if (e instanceof HMAlgebraicEquation) {
                result = calculate(e.getRightPart(), isFirst);
            }

        } catch (Exception ex) {
            IsmaError err = new IsmaError("ошибка при рассчете уравнения " + e.getCode() + "  " + ex.getMessage());
            err.setType(IsmaError.Type.SEM);
            errors.add(err);
            ex.printStackTrace();
        } finally {
            return result;
        }
    }

    public Double calculate(HMExpression expression, boolean isDer) {
        Stack<Double> stack = new Stack<>();
        expression.getTokens().stream().forEach(token -> {
            if (token instanceof EXPFunctionOperand) {
                EXPFunctionOperand f = (EXPFunctionOperand) token;
                stack.push(calculateFunction(f));
            } else if (token instanceof EXPOperand) {
                HMVariable var = ((EXPOperand) token).getVariable();
                if (var instanceof HMUnnamedConst) {
                    stack.push(((HMUnnamedConst) var).getValue());
                } else if (var instanceof HMConst || var instanceof HMDerivativeEquation) {
                    stack.push(ctx.getValue(var.getCode(), isDer));
                } else if (var instanceof HMAlgebraicEquation) {
                    stack.push(calculate((HMEquation) var, ctx, isDer));
                } else {
                    throw new RuntimeException("Unknown variable " + var.getCode());
                }
            } else if (token instanceof EXPOperator) {
                doOperation(stack, (EXPOperator) token);
            }
        });
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
                    break;
                case SUBTRACTION:
                    result = -v1;
                    break;
            }
        }
        values.push(result);
    }

    private void avoidLoopCheck(HMEquation c, HashSet<String> prevCheck) {
        if (c instanceof HMUnnamedConst)
            return;

        HashSet<String> current = new HashSet<>();
        if (prevCheck != null)
            prevCheck.stream().forEach(current::add);

        c.getRightPart().getTokens().stream()
                .filter(e -> e instanceof EXPOperand)
                .filter(e -> ((EXPOperand) e).getVariable() instanceof HMAlgebraicEquation)
                .forEach(e -> {
                    HMEquation var = (HMEquation) ((EXPOperand) e).getVariable();
                    if (current.contains(var.getCode()))
                        throw new RuntimeException("в выражении содержится петля из-за уравнения " + var.getCode());
                    else
                        avoidLoopCheck(var, current);
                });
    }


    private Double calculateFunction(EXPFunctionOperand function) {
        List<Double> args = new LinkedList<>();
        for (HMExpression exp : function.getArgs()) {
            args.add(calculate(exp, false));
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
        } else if (function.getName().equals("sqrt")) {
            result = Math.sqrt(args.get(0));
        }else {
            throw new RuntimeException("I understand function " + function.getName());
        }
        return result;
    }
}
