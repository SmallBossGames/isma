package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

import ru.nstu.isma.intg.demo.problems.lotkaVolterra.utils.Pair;

public class LVInitialData {

    private Pair<Integer, Integer> gridDimension;
    private LVHabitatArea habitatArea;
    private Pair<Double, Double> d;
    private Pair<LVFunction, LVFunction> f;
    private Pair<LVFunction, LVFunction> c;
    private Pair<LVBoundaryCondition, LVBoundaryCondition> boundaryConditions;

    public LVInitialData() {
    }

    public Pair<Integer, Integer> getGridDimension() {
        return gridDimension;
    }

    public void setGridDimension(Pair<Integer, Integer> gridDimension) {
        this.gridDimension = gridDimension;
    }

    public LVHabitatArea getHabitatArea() {
        return habitatArea;
    }

    public void setHabitatArea(LVHabitatArea habitatArea) {
        this.habitatArea = habitatArea;
    }

    public Pair<Double, Double> getD() {
        return d;
    }

    public void setD(Pair<Double, Double> d) {
        this.d = d;
    }

    public Pair<LVFunction, LVFunction> getF() {
        return f;
    }

    public void setF(Pair<LVFunction, LVFunction> f) {
        this.f = f;
    }

    public Pair<LVFunction, LVFunction> getC() {
        return c;
    }

    public void setC(Pair<LVFunction, LVFunction> c) {
        this.c = c;
    }

    public Pair<LVBoundaryCondition, LVBoundaryCondition> getBoundaryConditions() {
        return boundaryConditions;
    }

    public void setBoundaryConditions(Pair<LVBoundaryCondition, LVBoundaryCondition> boundaryConditions) {
        this.boundaryConditions = boundaryConditions;
    }
}
