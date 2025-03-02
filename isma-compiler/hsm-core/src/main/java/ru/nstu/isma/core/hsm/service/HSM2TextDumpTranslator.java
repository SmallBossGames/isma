package ru.nstu.isma.core.hsm.service;

import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMTransaction;
import ru.nstu.isma.core.hsm.linear.HMLinearEquation;
import ru.nstu.isma.core.hsm.linear.HMLinearVar;
import ru.nstu.isma.core.hsm.var.*;
import ru.nstu.isma.core.hsm.var.pde.HMBoundaryCondition;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSpatialVariable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 05.12.13
 * Time: 13:28
 */
public class HSM2TextDumpTranslator {
    public static String convert(HSM model) {
        StringBuilder sb = new StringBuilder();
        printVariableTable("GLOBAL", model.getVariableTable(), sb);
        for (String s : model.getAutomata().getStates().keySet()) {
            HMState ss = model.getAutomata().getState(s);
            printState(ss, sb);
        }
        sb.append("=== Transactions ========================\n");
        for (HMTransaction ts : model.getAutomata().getTransactions()) {
            sb.append("from ").append(ts.getSource().getCode());
            sb.append(" to ").append(ts.getTarget().getCode());
            sb.append(" when ");
            printExpression(ts.getCondition(), sb);
            sb.append("\n");
        }

        sb.append("=== Pseudo states ========================\n");
        model.getAutomata().getAllPseudoStates().stream().forEach(ps -> {
            sb.append("if ");
            printExpression(ps.getCondition(), sb);
            sb.append(" then ");
            printState(ps, sb);
        });

        sb.append("=== Linear system  ========================\n");
        if (model.getLinearSystem() != null) {
            if (model.getLinearSystem().getVars() != null)
                model.getLinearSystem().getVars().values().forEach(v -> printLinearVar(v, sb));
            if (model.getLinearSystem().getEquations() != null)
                model.getLinearSystem().getEquations().forEach(e -> printLinearEq(model, e, sb));
        }

        return sb.toString();
    }

    private static StringBuilder printVariableTable(String name, HMVariableTable vt, StringBuilder sb) {
        sb.append("=== Variable table dump: ").append(name).append("========================\n");

        printUnnamedConsts(vt, sb);
        printConsts(vt, sb);

        printEquations(vt, sb);

        return sb;
    }

    private static StringBuilder printState(HMState st, StringBuilder sb) {
        printVariableTable(st.getCode(), st.getVariables(), sb);
        return sb;
    }


    private static StringBuilder printConsts(HMVariableTable vt, StringBuilder sb) {
        sb.append("// --- Constants: --------------------\n");

        for (String vk : vt.keySet()) {
            HMVariable vv = vt.get(vk);
            if (vv instanceof HMConst && !(vv instanceof HMUnnamedConst)) {
                printConst((HMConst) vv, sb);
                sb.append("\n");
            }
        }
        sb.append("\n");
        return sb;
    }

    private static StringBuilder printUnnamedConsts(HMVariableTable vt, StringBuilder sb) {
        sb.append("// --- Unnamed constants: -------------------- \n");

        for (String vk : vt.keySet()) {
            HMVariable vv = vt.get(vk);
            if (vv instanceof HMUnnamedConst) {
                printUConst((HMUnnamedConst) vv, sb);
                sb.append("\n");
            }

        }
        sb.append("\n");
        return sb;
    }

    private static StringBuilder printEquations(HMVariableTable vt, StringBuilder sb) {
        sb.append("// --- Equations: -------------------- \n");

        for (String vk : vt.keySet()) {
            HMVariable vv = vt.get(vk);
            if (vv instanceof HMAlgebraicEquation) {
                printAlgebraicEquation((HMAlgebraicEquation) vv, sb);
                sb.append("\n");
            } else if (vv instanceof HMPartialDerivativeEquation) {
                printPartialDerEquation((HMPartialDerivativeEquation) vv, sb);
                sb.append("\n");
            } else if (vv instanceof HMDerivativeEquation) {
                printDerivativeEquation((HMDerivativeEquation) vv, sb);
                sb.append("\n");
            }
        }
        sb.append("\n");
        return sb;
    }

    private static StringBuilder printVariable(HMVariable v, StringBuilder sb) {
        if (v instanceof HMUnnamedConst) {
            printUConst((HMUnnamedConst) v, sb);
        } else if (v instanceof HMConst) {
            printConst((HMConst) v, sb);
        } else if (v instanceof HMAlgebraicEquation) {
            printAlgebraicEquation((HMAlgebraicEquation) v, sb);
        } else if (v instanceof HMDerivativeEquation) {
            printDerivativeEquation((HMDerivativeEquation) v, sb);
        } else if (v instanceof HMPartialDerivativeEquation) {
            printPartialDerEquation((HMPartialDerivativeEquation) v, sb);
        }
        return sb;
    }

    private static StringBuilder printConst(HMConst c, StringBuilder sb) {
        return sb.append("[const] ").append(c.getCode()).append(" = ").append(c.getValue());
    }

    private static StringBuilder printUConst(HMUnnamedConst c, StringBuilder sb) {
        return sb.append("[unnamed_const] ").append(c.getValue()).append(" // \"").append(c.getCode()).append("\"");
    }

    private static StringBuilder printAlgebraicEquation(HMAlgebraicEquation eq, StringBuilder sb) {
        sb.append("[equation] ");
        sb.append(eq.getCode());
        sb.append(" = ");
        printExpression(eq.getRightPart(), sb);
        return sb;
    }

    private static StringBuilder printDerivativeEquation(HMDerivativeEquation eq, StringBuilder sb) {
        sb.append("[dae] ").append(eq.getCode()).append(" = ");
        printExpression(eq.getRightPart(), sb);
        sb.append("   // initial = ");
        printExpression(eq.getInitial().getRightPart(), sb);
        return sb;
    }

    private static StringBuilder printPartialDerEquation(HMPartialDerivativeEquation eq, StringBuilder sb) {
        sb.append("[pde] ");
        for (HMSpatialVariable v : eq.getVariables())
            sb.append("[").append(v.getCode()).append(" ").append(eq.getVariableOrder(v)).append("] ");

        sb.append(eq.getCode()).append(" = ");
        printExpression(eq.getRightPart(), sb);
        sb.append("   //  initial = ");
        printExpression(eq.getInitial().getRightPart(), sb);

        for (HMBoundaryCondition b : eq.getBoundaries()) {
            sb.append("; edge ");
            sb.append(b.getEquation().getCode()).append(" = ");
            sb = printExpression(b.getValue(), sb);
            sb.append(" at ").append(b.getSpatialVar().getCode()).append(" ").append(b.getSide().toString());
        }

        return sb;
    }

    private static StringBuilder printExpression(HMExpression exp, StringBuilder sb) {
        if (exp == null) {
            sb.append("null");
            return sb;
        }
        exp.toString(sb, false);
        return sb;
    }

    private static StringBuilder printLinearVar(HMLinearVar var, StringBuilder sb) {
        sb.append("var ").append(var.getCode())
                .append("[").append(var.getColumnIndex()).append("]\n");
        return sb;
    }

    private static StringBuilder printLinearEq(HSM model, HMLinearEquation eq, StringBuilder sb) {
        List<HMLinearVar> vars = new LinkedList<>();
        model.getLinearSystem().getVars().values().forEach(vars::add);

        sb.append("lin eq: ");

        for (int i = 0; i < eq.getLeftPart().size(); i++) {
            sb.append(getByIdx(vars, i).getCode()).append(" [");
            Poliz2InfixConverter converter = new Poliz2InfixConverter(eq.getLeftPart().get(i));
            printExpression(converter.convert(), sb);
            sb.append("]");
            sb.append("    ");
        }

        sb.append("\n");

        return sb;
    }

    private static HMLinearVar getByIdx(List<HMLinearVar> vars, int idx) {
        for (HMLinearVar v : vars) {
            if (v.getColumnIndex() == idx)
                return v;
        }
        return null;
    }

}
