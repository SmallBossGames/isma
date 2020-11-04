package ru.nstu.isma.in.lisma.service;

import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.hybrid.HMTransaction;
import ru.nstu.isma.core.hsm.var.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 06.12.13 Time: 1:49
 */
public class Lisma2007Converter {

    public static String convert(HSM model) {
        StringBuilder sb = new StringBuilder();
        printConsts(model.getVariableTable(), sb);
        printEquations(model.getVariableTable(), sb, false);
        for(String key : model.getAutomata().getStates().keySet()) {
            if (key.equals(HSM.INIT_STATE)) {
                continue;
            }
            printState(model.getAutomata().getState(key), model.getAutomata(), sb);
        }
        return sb.toString();
    }

    private static StringBuilder printConsts(HMVariableTable vt,  StringBuilder sb) {
        List<String> keys = new LinkedList<String>();

        for (String vk : vt.keySet()) {
            HMVariable vv = vt.get(vk);
            if (vv instanceof HMConst && !(vv instanceof HMUnnamedConst)) {
                keys.add(vv.getCode());
            }
        }
        Collections.sort(keys);
        for(String k : keys) {
            HMVariable vv = vt.get(k);
            printConst((HMConst) vv, sb);
            sb.append(";\n");
        }

        sb.append("\n");
        return sb;
    }



    private static StringBuilder printEquations(HMVariableTable vt,  StringBuilder sb, boolean needShift) {
        List<String> algKeys = new LinkedList<String>();
        List<String> odeKeys = new LinkedList<String>();
        for (String vk : vt.keySet()) {
            HMVariable vv = vt.get(vk);
            if (vv instanceof HMAlgebraicEquation) {
                algKeys.add(vv.getCode());
            } else if (vv instanceof HMDerivativeEquation) {
                odeKeys.add(vv.getCode());
            }
        }
        Collections.sort(algKeys);
        Collections.sort(odeKeys);

        sb.append("//======== dae equations\n");
        for (String k : odeKeys) {
            HMVariable vv = vt.get(k);
            if (needShift)  sb.append("   ");
            printDerivativeEquation((HMDerivativeEquation) vv, sb);
            sb.append(";\n");
        }
        sb.append("//======== dae initial; equations\n");
        for (String k : odeKeys) {
            HMVariable vv = vt.get(k);
            if (needShift)  sb.append("   ");
            printDerivativeEquationInitial((HMDerivativeEquation) vv, sb);
            sb.append(";\n");
        }

        sb.append("\n");
        sb.append("//======== alg equations\n");
        for (String k : algKeys) {
            HMVariable vv = vt.get(k);
            if (needShift)  sb.append("   ");
            printAlgebraicEquation((HMAlgebraicEquation) vv, sb);
            sb.append(";\n");
        }

        sb.append("\n");
        return sb;
    }

    private static StringBuilder printConst(HMConst c, StringBuilder sb) {
        return sb.append(c.getCode()).append(" = ").append(c.getValue());
    }

    private static StringBuilder printAlgebraicEquation(HMAlgebraicEquation eq, StringBuilder sb) {
        sb.append(eq.getCode());
        sb.append(" ~= ");
        eq.getRightPart().toString(sb, true);
        return sb;
    }

    private static StringBuilder printDerivativeEquation(HMDerivativeEquation eq, StringBuilder sb) {
        sb.append(eq.getCode());
        sb.append("' = ");
        eq.getRightPart().toString(sb, true);
        return sb;
    }

    private static StringBuilder printDerivativeEquationInitial(HMDerivativeEquation eq, StringBuilder sb) {
        sb.append(eq.getCode());
        sb.append(" = ");
        sb.append(eq.getInitial().getValue());
        return sb;
    }

    private static StringBuilder printState(HMState st, HMStateAutomata automata, StringBuilder sb) {
        sb.append(st.getCode()).append("[");
        List<HMTransaction> to = automata.getAllTo(st);
        if (to.size() == 0) {
            throw new RuntimeException("State haven't transactions!");
        }
        to.get(0).getCondition().toString(sb, true);
        sb.append("] is\n");
        printEquations(st.getVariables(), sb, true);
        sb.append("from ");
        boolean first = true;
        for (HMTransaction t : to) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(t.getSource().getCode());
        }
        sb.append(";");
        return sb;
    }
}
