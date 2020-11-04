package ru.nstu.isma.core.hsm.linear;

import common.Calculateable;
import common.IndexMapper;
import ru.nstu.isma.core.hsm.HSM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
public class HMLinearSystem implements Calculateable, Serializable {
    private HSM hms;
    private Map<String, HMLinearVar> vars = new HashMap<>();
    private List<HMLinearEquation> equations;
    private IndexMapper indexMapper;
    private LinearSystemMatrix matrix;
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
            if (v.getColumnIndex() == index)
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

    public int getVarCalulationIndex(HMLinearVar v) {
        return indexMapper.getIndexMap().get(v.getCode());
    }

    @Override
    public void prepareForCalculation(IndexMapper indexMapper) {
        this.indexMapper = indexMapper;
        LinearSystemMatrixBuilder matrixBuilder = new LinearSystemMatrixBuilder(indexMapper);
        matrix = matrixBuilder.build("DefaultLinearSystemMatrix", true);
        readyForCalc = true;
    }

    @Override
    public double[] calculate(double[] y) {
        if (!readyForCalc)
            throw new RuntimeException("Linear system is not ready");

        LinearEquationsSolver solver = new LinearEquationsSolver(matrix.getA(y));
        return solver.solve(matrix.getB(y));
    }

}
