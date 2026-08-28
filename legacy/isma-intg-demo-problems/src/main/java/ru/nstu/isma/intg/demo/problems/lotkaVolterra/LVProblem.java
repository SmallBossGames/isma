package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.demo.problems.lotkaVolterra.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class LVProblem {

    public static final int FIRST_SPECIES = 1;
    public static final int SECOND_SPECIES = 2;

    private final LVInitialData initialData;

    private int N;
    private double[] y0;
    private DaeSystem daeSystem;

    public LVProblem(LVInitialData initialData) {
        this.initialData = initialData;
        init();
    }

    public LVInitialData getInitialData() {
        return initialData;
    }

    public int getN() {
        return N;
    }

    public double[] getY0() {
        return y0;
    }

    public DaeSystem getDaeSystem() {
        return daeSystem;
    }

    private void init() {
        int J = initialData.getGridDimension().getFirst();
        int K = initialData.getGridDimension().getSecond();
        N = getEquationCount(J, K);

        y0 = new double[N];

        Double topBoundaryOnX = initialData.getHabitatArea().getOnX().getSecond();
        double gridStepOnX = getGridStepOnCoordinate(topBoundaryOnX, J);

        Double topBoundaryOnZ = initialData.getHabitatArea().getOnZ().getSecond();
        double gridStepOnZ = getGridStepOnCoordinate(topBoundaryOnZ, K);

        Pair<LVOdeGenerator, LVOdeGenerator> odeGenerators = getOdeGenerators(J, gridStepOnX, gridStepOnZ);

        int odeSize = getEquationCount(J, K);
        List<DifferentialEquation> deList = new ArrayList<>(odeSize);

        int equationIndex = 0;
        for (int j = 0; j < J; j++) {
            for (int k = 0; k < K; k++) {

                double xJ = j * gridStepOnX;
                double zK = k * gridStepOnZ;

                double y01 = initialData.getC().getFirst().calculate(xJ, zK);
                y0[equationIndex] = y01;

                LVDifferentialEquation ode1 = odeGenerators.getFirst().getOde(equationIndex, j, k);
                deList.add(ode1);
                equationIndex++;

                double y02 = initialData.getC().getSecond().calculate(xJ, zK);
                y0[equationIndex] = y02;

                LVDifferentialEquation ode2 = odeGenerators.getSecond().getOde(equationIndex, j, k);
                deList.add(ode2);
                equationIndex++;
            }
        }

        DifferentialEquation[] des = new DifferentialEquation[odeSize];
        des = deList.toArray(des);
        daeSystem = new DaeSystem(des);
    }

    private int getEquationCount(int J, int K) {
        return 2 * J * K;
    }

    private double getGridStepOnCoordinate(double topBoundary, int gridDimension) {
        return topBoundary / (gridDimension - 1);
    }

    private Pair<LVOdeGenerator, LVOdeGenerator> getOdeGenerators(int J, double gridStepOnX, double gridStepOnZ) {
        LVBoundaryCondition bounder1 = initialData.getBoundaryConditions().getFirst();
        LVOdeIndexGenerator odeIndexGenerator1 = new LVOdeIndexGenerator(J, bounder1);
        double d1 = initialData.getD().getFirst();
        LVFunction f1 = initialData.getF().getFirst();
        LVOdeGenerator odeGenerator1 = new LVOdeGenerator(
                odeIndexGenerator1,
                FIRST_SPECIES,
                gridStepOnX,
                gridStepOnZ,
                d1,
                f1);

        LVBoundaryCondition bounder2 = initialData.getBoundaryConditions().getSecond();
        LVOdeIndexGenerator odeIndexGenerator2 = new LVOdeIndexGenerator(J, bounder2);
        double d2 = initialData.getD().getSecond();
        LVFunction f2 = initialData.getF().getSecond();
        LVOdeGenerator odeGenerator2 = new LVOdeGenerator(
                odeIndexGenerator2,
                SECOND_SPECIES,
                gridStepOnX,
                gridStepOnZ,
                d2,
                f2);

        return new Pair<>(odeGenerator1, odeGenerator2);
    }
}
