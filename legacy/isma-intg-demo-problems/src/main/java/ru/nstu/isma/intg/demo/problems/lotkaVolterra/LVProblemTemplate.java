package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

import java.io.Serializable;

import ru.nstu.isma.intg.demo.problems.lotkaVolterra.utils.Pair;

public abstract class LVProblemTemplate implements Serializable {

    private int J;
    private int K;

    public abstract LVHabitatArea getHabitatArea();

    public abstract double getD1();

    public abstract double getD2();

    public abstract LVFunction getF1();

    public abstract LVFunction getF2();

    public abstract LVFunction getC1();

    public abstract LVFunction getC2();

    public abstract LVBoundaryCondition getBoundaryCondition1();

    public abstract LVBoundaryCondition getBoundaryCondition2();

    public int getJ() {
        return J;
    }

    public int getK() {
        return K;
    }

    public LVProblem generateLVProblem(int J, int K) {
        this.J = J;
        this.K = K;

        LVInitialData initialData = new LVInitialData();
        initialData.setGridDimension(new Pair<>(J, K));

        LVHabitatArea habitatArea = getHabitatArea();
        initialData.setHabitatArea(habitatArea);

        Pair<Double, Double> d = new Pair<>(getD1(), getD2());
        initialData.setD(d);

        Pair<LVFunction, LVFunction> f = new Pair<>(getF1(), getF2());
        initialData.setF(f);

        Pair<LVFunction, LVFunction> c = new Pair<>(getC1(), getC2());
        initialData.setC(c);

        Pair<LVBoundaryCondition, LVBoundaryCondition> boundaryConditions = new Pair<>(getBoundaryCondition1(),
                getBoundaryCondition2());
        initialData.setBoundaryConditions(boundaryConditions);

        LVProblem lvProblem = new LVProblem(initialData);
        return lvProblem;
    }
}
