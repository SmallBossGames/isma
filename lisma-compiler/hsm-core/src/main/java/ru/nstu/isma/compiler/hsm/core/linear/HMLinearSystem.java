package ru.nstu.isma.compiler.hsm.core.linear;

import ru.nstu.isma.compiler.hsm.core.HSM;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
public class HMLinearSystem implements Serializable {
    private final HSM hms;
    private final Map<String, HMLinearVar> vars = new HashMap<>();
    private List<HMLinearEquation> equations;
    private boolean readyForCalc = false;

    private Integer size;

    public HMLinearSystem(HSM hms) {
        this.hms = hms;
    }

    private void init() {
        this.size = vars.size();
        equations = new ArrayList<>(size);
    }

    public void addVar(String name) {
        HMLinearVar var = new HMLinearVar(name);
        addVar(var);
    }

    public void addVar(HMLinearVar var) {
        if (var.getColumnIndex() == null)
            var.setColumnIndex(vars.size());
        vars.put(var.getCode(), var);
        init();
        hms.getVariableTable().add(var);
    }

    public HMLinearVar getLinearVariable(String code) {
        return vars.get(code);
    }

    public HMLinearVar getLinearVariable(Integer index) {
        for (HMLinearVar v : vars.values()) {
            if (Objects.equals(v.getColumnIndex(), index))
                return v;
        }
        return null;
    }

    public HMLinearEquation createEquation() {
        if (size == null)
            throw new RuntimeException("Empty variable list");

        HMLinearEquation equation = new HMLinearEquation(size);
        equations.add(equation);
        return equation;
    }

    public boolean isEmpty() {
        return size == null || size == 0;
    }

    public Map<String, HMLinearVar> getVars() {
        return vars;
    }

    public List<HMLinearEquation> getEquations() {
        return equations;
    }
}
