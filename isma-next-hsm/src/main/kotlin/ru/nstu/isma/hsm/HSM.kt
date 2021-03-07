package ru.nstu.isma.hsm;

import ru.nstu.isma.hsm.exp.HMExpression;
import ru.nstu.isma.hsm.hybrid.HMState;
import ru.nstu.isma.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.hsm.linear.HMLinearSystem;
import ru.nstu.isma.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.hsm.var.HMVariable;
import ru.nstu.isma.hsm.var.HMVariableTable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 25.10.13
 * Time: 0:04
 */
public class HSM implements Serializable {
    public final static String INIT_STATE = "init";

    protected HMVariableTable variables;

    protected HMStateAutomata automata;

    protected HMLinearSystem linearSystem;

    protected Double startTime = 0d;

    protected Double endTime = 1d;

    protected Double stepTime = 0.1;

    protected List<String> out = new LinkedList<>();

    public HSM() {
        variables = new HMVariableTable();
        automata = new HMStateAutomata(this);
        linearSystem = new HMLinearSystem(this);

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

    public void setVariables(HMVariableTable variables) {
        this.variables = variables;
    }

    public HMStateAutomata getAutomata() {
        return automata;
    }

    public void setAutomata(HMStateAutomata automata) {
        this.automata = automata;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public Double getStepTime() {
        return stepTime;
    }

    public void setStepTime(Double stepTime) {
        this.stepTime = stepTime;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
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
