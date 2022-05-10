package ru.nstu.isma.core.hsm;

import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.linear.HMLinearSystem;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.hsm.var.HMVariable;
import ru.nstu.isma.core.hsm.var.HMVariableTable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 25.10.13
 * Time: 0:04
 */
public final class HSM implements Serializable {
    public final static String INIT_STATE = "init";

    private final HMVariableTable variables = new HMVariableTable();

    private final HMStateAutomata automata = new HMStateAutomata(this);

    private final HMLinearSystem linearSystem = new HMLinearSystem(this);

    private final List<String> out = new LinkedList<>();

    public HSM() {

        HMState init = new HMState(INIT_STATE);
        automata.setInit(init);
        automata.addState(init);
    }

    public HMVariableTable getVariableTable() {
        return variables;
    }

    public List<HMVariable> getVariables() {
        return variables.variables();
    }

    public HMStateAutomata getAutomata() {
        return automata;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out.clear();
        this.out.addAll(out);
    }

    public HMLinearSystem getLinearSystem() {
        return linearSystem;
    }

    public boolean isPDE() {
        return variables.getPdes().size() > 0;
    }

    public HSM initTimeEquation(double start) {
        final String TIME = "TIME";
        HMVariableTable baseTable = getVariableTable();
        if (!baseTable.contain(TIME)) {
            HMDerivativeEquation time = new HMDerivativeEquation(TIME);
            time.setRightPart(HMExpression.getConst(1));
            time.setInitial(start);
            baseTable.add(time);
        } else {
            ((HMDerivativeEquation) baseTable.get(TIME)).setInitial(start);
        }
        return this;
    }
}
