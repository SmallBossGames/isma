package ru.nstu.isma.core.hsm.exp;

import ru.nstu.isma.core.hsm.var.HMUnnamedConst;
import ru.nstu.isma.core.hsm.var.HMVariable;
import ru.nstu.isma.core.hsm.service.Poliz2InfixConverter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 1:00
 */
public class HMExpression implements Serializable {
    private Type type = Type.INFIX;

    private final List<EXPToken> tokens = new LinkedList<>();

    public List<EXPToken> add(EXPToken t) {
        tokens.add(t);
        return getTokens();
    }

    public List<EXPToken> addOperand(HMVariable v) {
        tokens.add(new EXPOperand(v));
        return getTokens();
    }

    public List<EXPToken> addUnnamedConst(double v) {
        tokens.add(new EXPOperand(new HMUnnamedConst(v)));
        return getTokens();
    }

    public List<EXPToken> openParenthesis() {
        tokens.add(new EXPParenthesis(EXPParenthesis.Type.OPEN));
        return getTokens();
    }

    public List<EXPToken> closeParenthesis() {
        tokens.add(new EXPParenthesis(EXPParenthesis.Type.CLOSE));
        return getTokens();
    }

    public List<EXPToken> getTokens() {
        return tokens;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public StringBuilder toString(StringBuilder sb, boolean toInfix) {

        HMExpression exp = this;
        if (toInfix == true) {
            Poliz2InfixConverter converter = new Poliz2InfixConverter(exp);
            exp = converter.convert();
        }

        for (EXPToken t : exp.getTokens()) {
            if (t instanceof EXPOperator) {
                sb.append(t.toString());

            } else if (t instanceof EXPParenthesis) {
                switch (((EXPParenthesis) t).getType()) {
                    case CLOSE:
                        sb.append(")");
                        break;
                    case OPEN:
                        sb.append("(");
                        break;
                }
            } else if (t instanceof EXPFunctionOperand) {
                sb.append(((EXPFunctionOperand) t).getName());
                sb.append("(");
                int cnt = 0;
                for (HMExpression e : ((EXPFunctionOperand) t).getArgs()) {
                    if (cnt > 0) {
                        sb.append(",");
                    }
                    StringBuilder sbArg = new StringBuilder();
                    sb.append(e.toString(sbArg, true).toString());
                    cnt++;
                }
                sb.append(")");
            } else if (t instanceof EXPPDEOperand) {
                EXPPDEOperand e = (EXPPDEOperand) t;
                sb.append("D(");
                sb.append(e.getVariable().getCode());
                sb.append(", ");
                sb.append(e.getFirstSpatialVariable().getCode());
                sb.append(")");
            } else if (t instanceof EXPOperand) {
                HMVariable vv = ((EXPOperand) t).getVariable();
                if (vv instanceof HMUnnamedConst) {
                    sb.append(((HMUnnamedConst) vv).getValue());
                } else sb.append(vv.getCode());
            }
            sb.append(" ");
        }
        return sb;
    }

    public enum Type implements Serializable {
        INFIX, POLIZ
    }

    public static HMExpression getConst(double value) {
        HMExpression exp = new HMExpression();
        exp.add(new EXPOperand(new HMUnnamedConst(value)));
        return exp;
    }

    public static HMExpression getZero() {
        return getConst(0);
    }

}
