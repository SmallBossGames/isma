package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

public class LVOdeIndexGenerator {

    private final int J;
    private final LVBoundaryCondition boundaryCondition;

    public LVOdeIndexGenerator(int J, LVBoundaryCondition boundaryCondition) {
        this.J = J;
        this.boundaryCondition = boundaryCondition;
    }

    public int getEquationIndex(int i, int j, int k) {
        int boundedJ = boundaryCondition.getBoundedValue(j);
        int boundedK = boundaryCondition.getBoundedValue(k);
        int equationIndex = (i + 2 * J * boundedJ + 2 * boundedK) - 1;
        return equationIndex;
    }
}
