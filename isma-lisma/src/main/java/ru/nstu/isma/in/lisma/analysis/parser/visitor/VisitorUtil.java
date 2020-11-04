package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.var.HMAlgebraicEquation;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

/**
 * Created by Alex on 20.12.2015.
 */
public class VisitorUtil {

    public static String getVariableNameWithIndex(String baseName, LismaParser.Cycle_indexContext cycle, ParserContext pc) {
        if (cycle != null) {
            Integer idx = pc.getIdxValue(cycle.cycle_index_idx().Identifier().getText());
            if (cycle.cycle_index_idx().literal() != null) {
                if (cycle.cycle_index_idx().ADD() != null)
                    idx += Integer.valueOf(cycle.cycle_index_idx().literal().getText());
                if (cycle.cycle_index_idx().SUB() != null)
                    idx -= Integer.valueOf(cycle.cycle_index_idx().literal().getText());
            }
            baseName += idx;
            if (cycle.cycle_index_posfix() != null)
                baseName += cycle.cycle_index_posfix().getText();
        }
        return baseName;
    }

    public static HSM parseNotInitEquations(HSM model) {
        HMVariableTable baseTable = model.getVariableTable();
        model.getAutomata().getStates().values().forEach(st -> parseNotInitEquationsWithState(baseTable, st));
        model.getAutomata().getAllPseudoStates().forEach(st -> parseNotInitEquationsWithState(baseTable, st));
        return model;
    }

    public static void parseNotInitEquationsWithState(HMVariableTable baseTable, HMState st) {
        HMVariableTable stateVt = st.getVariables();
        stateVt.getAlgs().forEach(a -> {
            if (!baseTable.contain(a.getCode())) {
                HMAlgebraicEquation ae = new HMAlgebraicEquation(a.getCode());
                ae.setRightPart(HMExpression.getZero());
                baseTable.add(ae);
            }
        });
        stateVt.getOdes().forEach(o -> {
            if (!baseTable.contain(o.getCode())) {
                HMDerivativeEquation oe = new HMDerivativeEquation(o.getCode());
                oe.setRightPart(HMExpression.getZero());
                oe.setInitial(0);
                baseTable.add(oe);
            }
        });
    }
}
