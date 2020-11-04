package common;

import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.linear.HMLinearVar;
import ru.nstu.isma.core.hsm.service.Poliz2InfixConverter;
import ru.nstu.isma.core.hsm.var.HMAlgebraicEquation;
import ru.nstu.isma.core.hsm.var.HMConst;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.hsm.var.HMVariable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Maria Nasyrova
 * @since 05.10.2015
 */
public class HMExpressionBuilder {

    private IndexProvider context;

    public HMExpressionBuilder(IndexProvider context) {
        this.context = context;
    }

    public String buildExpression(HMExpression exp) {
        return buildExpression(exp, false, false);
    }

    public String buildExpression(HMExpression exp, boolean needDeepAlgCalc) {
        return buildExpression(exp, needDeepAlgCalc, false);
    }

    public String buildExpression(HMExpression exp, boolean needDeepAlgCalc, boolean forOde) {
        StringBuilder str = new StringBuilder();
        if (exp.getType() == HMExpression.Type.POLIZ) {
            Poliz2InfixConverter converter = new Poliz2InfixConverter(exp);
            exp = converter.convert();
        }
        exp.getTokens().stream().forEach(t -> {
            if (t instanceof EXPOperator) {
                doOperator(str, (EXPOperator) t);
            } else if (t instanceof EXPParenthesis) {
                str.append(t.toString());
            } else if (t instanceof EXPFunctionOperand) {
                doFunction(str, (EXPFunctionOperand) t, forOde);
            } else if (t instanceof EXPOperand) {
                HMVariable var = ((EXPOperand) t).getVariable();
                if (var instanceof HMConst) {
                    str.append(((HMConst) var).getValue()); // todo формат double
                } else if (var instanceof HMAlgebraicEquation && needDeepAlgCalc) {
                    str.append(context.getAlgebraicArrayCode(var.getCode()));
                } else if (var instanceof HMDerivativeEquation) {
                    str.append(context.getDifferentialArrayCode(var.getCode()));
                } else if (var instanceof HMLinearVar || (var instanceof HMAlgebraicEquation && !needDeepAlgCalc)) {
                    if (forOde) {
                        str.append(context.getAlgebraicArrayCodeForDifferentialEquation(var.getCode()));
                    } else {
                        str.append(context.getAlgebraicArrayCode(var.getCode()));
                    }
                } /*else if (var instanceof HMAlgebraicEquation) {
                    str.append("(").append(buildExpression(((HMAlgebraicEquation) var).getRightPart())).append(")");
                }*/
            }
            str.append(" ");
        });
        return str.toString();
    }

    private void doOperator(StringBuilder sb, EXPOperator o) {
        switch (o.getCode()) {
            case AND:
                sb.append("&&");
                break;
            case OR:
                sb.append("||");
                break;
            default:
                sb.append(o.toString());
                break;
        }
    }

    private void doFunction(StringBuilder sb, EXPFunctionOperand funct, boolean forOde) {
        String name = funct.getName();
        String javaName = null;
        List<String> args = new LinkedList<>();

        switch (name) {
            case "abs":
                javaName = doOneArgFunct(name, "Math.abs", funct, args, forOde);
                break;
            case "sin":
                javaName = doOneArgFunct(name, "Math.sin", funct, args, forOde);
                break;
            case "cos":
                javaName = doOneArgFunct(name, "Math.cos", funct, args, forOde);
                break;
            case "tg":
                javaName = doOneArgFunct(name, "Math.tan", funct, args, forOde);
                break;

            case "exp":
                javaName = doOneArgFunct(name, "Math.exp", funct, args, forOde);
                break;

            case "sqrt":
                javaName = doOneArgFunct(name, "Math.sqrt", funct, args, forOde);
                break;

            case "pow":
                javaName = doTwoArgFunct(name, "Math.pow", funct, args, forOde);
                break;

            case "max":
                javaName = doTwoArgFunct(name, "Math.max", funct, args, forOde);
                break;

            case "min":
                javaName = doTwoArgFunct(name, "Math.min", funct, args, forOde);
                break;
            case "sign":
                javaName = doOneArgFunct(name, "Math.signum", funct, args, forOde);
                break;
        }

        sb.append(" ").append(javaName).append("(");
        args.stream().forEach(e -> {
            if (!e.equals(args.get(0))) sb.append(", ");
            sb.append(e);
        });
        sb.append(") ");
    }

    private String doOneArgFunct(String name, String javaName, EXPFunctionOperand funct, List<String> args, boolean forOde) {
        int argSize = funct.getArgs().size();
        assertTrue(argSize == 1, name);
        args.add(buildExpression(funct.getArgs().get(0), false, forOde));
        return javaName;
    }

    private String doTwoArgFunct(String name, String javaName, EXPFunctionOperand funct, List<String> args, boolean forOde) {
        int argSize = funct.getArgs().size();
        assertTrue(argSize == 2, name);
        args.add(buildExpression(funct.getArgs().get(0), false, forOde));
        args.add(buildExpression(funct.getArgs().get(1), false, forOde));
        return javaName;
    }

    private void assertTrue(boolean cond, String name) {
        if (!cond) {
            throw new RuntimeException("Incorrect arg count for function: " + name);
        }
    }

}
