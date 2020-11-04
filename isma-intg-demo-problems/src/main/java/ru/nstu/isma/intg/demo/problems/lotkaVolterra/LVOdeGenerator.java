package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

public class LVOdeGenerator {

    private LVOdeIndexGenerator odeIndexGenerator;
    private int speciesType;
    private double gridStepOnX;
    private double gridStepOnZ;
    private double d;
    private LVFunction f;

    public LVOdeGenerator(LVOdeIndexGenerator odeIndexGenerator, int speciesType, double gridStepOnX, double gridStepOnZ, double d, LVFunction f) {
        this.odeIndexGenerator = odeIndexGenerator;
        this.speciesType = speciesType;
        this.gridStepOnX = gridStepOnX;
        this.gridStepOnZ = gridStepOnZ;
        this.d = d;
        this.f = f;
    }

    public LVDifferentialEquation getOde(int equationIndex, int j, int k) {
        LVDifferentialEquation ode = new LVDifferentialEquation(equationIndex);
        ode.setD(d);
        ode.setF(f);
        ode.setStepOnX(gridStepOnX);
        ode.setStepOnZ(gridStepOnZ);
        ode.setIndexes(
                odeIndexGenerator.getEquationIndex(speciesType, j + 1, k),
                odeIndexGenerator.getEquationIndex(speciesType, j - 1, k),
                odeIndexGenerator.getEquationIndex(speciesType, j, k),
                odeIndexGenerator.getEquationIndex(speciesType, j, k + 1),
                odeIndexGenerator.getEquationIndex(speciesType, j, k - 1),
                odeIndexGenerator.getEquationIndex(LVProblem.FIRST_SPECIES, j, k),
                odeIndexGenerator.getEquationIndex(LVProblem.SECOND_SPECIES, j, k)
        );
        return ode;
    }
}
