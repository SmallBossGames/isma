package ru.nstu.isma.intg.core.solvers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nstu.isma.intg.api.calcmodel.*;
import ru.nstu.isma.intg.api.methods.*;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;

import java.util.Arrays;

/**
 * @author Mariya Nasyrova
 * @since 01.09.14
 */
@Deprecated
public class DefaultDaeSystemStepSolver implements DaeSystemStepSolver {

    private final IntegrationMethodRungeKutta intgMethod;
    private DaeSystem daeSystem;

    private long stepCalculationCount;
    private long rhsCalculationCount;

    private DifferentialEquationsCalculator deCalculator;
    private AlgebraicEquationCalculator aeCalculator;

    public DefaultDaeSystemStepSolver(IntegrationMethodRungeKutta intgMethod, DaeSystem daeSystem) {
        this.intgMethod = intgMethod;
        commitDaeSystem(daeSystem);
    }

    @NotNull
    @Override
    public IntegrationMethodRungeKutta getIntgMethod() {
        return intgMethod;
    }

    protected DaeSystem getDaeSystem() {
        return daeSystem;
    }

    @Override
    public double[][] calculateRhs(double[] yForDe) {
        rhsCalculationCount++;

        double[][] rhs = daeSystem.createEmptyRhs();
        rhs[DaeSystem.RHS_AE_PART_IDX] = calculateRhsForAlgebraicEquations(yForDe);
        rhs[DaeSystem.RHS_DE_PART_IDX] = calculateRhsForDifferentialEquations(yForDe, rhs);
        return rhs;
    }

    protected double[] calculateRhsForAlgebraicEquations(double[] yForDe) {
        return aeCalculator.apply(yForDe);
    }

    protected double[] calculateRhsForDifferentialEquations(double[] yForDe, double[][] rhs) {
        return deCalculator.apply(yForDe, rhs);
    }

    @Override
    public void apply(@Nullable DaeSystemChangeSet changeSet) {
        if (changeSet != null && !changeSet.isEmpty()) {
            commitDaeSystem(changeSet.apply(daeSystem));
        }
    }

    @NotNull
    @Override
    public IntgPoint step(IntgPoint fromPoint) {
        double initialStep = fromPoint.getStep();
        double[][] stages = stages(fromPoint);

        Double accuracyNextStep = null;
        if (isControllerEnabled(getIntgMethod().getAccuracyController())) {
            AccuracyIntgController.AccuracyResults accuracyResults =
                    getIntgMethod().getAccuracyController().tune(fromPoint, stages, this);
            stages = accuracyResults.getTunedStages();
            accuracyNextStep = accuracyResults.getTunedStep();
            fromPoint.setStep(accuracyResults.getTunedStep());
        }

        stepCalculationCount++;

        double[] nextY = nextY(fromPoint, stages);
        double[][] nextRhs = calculateRhs(nextY);
        IntgPoint toPoint = new IntgPoint(fromPoint.getStep(), nextY, nextRhs, stages, fromPoint.getStep());

        Double stabilityNextStep = null;
        if (isControllerEnabled(getIntgMethod().getStabilityController())) {
            stabilityNextStep = getIntgMethod().getStabilityController().predictNextStepSize(toPoint);
        }

        // Выбираем следующий шаг по формуле h_new = max(h, min(h_acc, h_st)), где
        // min(h_acc, h_st) - минимальный из шагов, прогнозируемых по точности и устойчивости;
        // h - текущий шаг.
        Double minPredictedNextStep = accuracyNextStep;
        if (minPredictedNextStep == null || (stabilityNextStep != null && stabilityNextStep < minPredictedNextStep)) {
            minPredictedNextStep = stabilityNextStep;
        }

        double nextStep = initialStep;
        if (minPredictedNextStep != null && minPredictedNextStep > initialStep) {
            nextStep = minPredictedNextStep;
        }

        toPoint.setNextStep(nextStep);
        return toPoint;
    }

    public long getStepCalculationCount() {
        return stepCalculationCount;
    }

    public long getRhsCalculationCount() {
        return rhsCalculationCount;
    }

    protected double[] nextY(IntgPoint fromPoint, double[][] stages) {
        double[] nextY = new double[fromPoint.getY().length];

        double[] odeStages = new double[0];
        for (int odeIdx = 0; odeIdx < getDaeSystem().getDifferentialEquationCount(); odeIdx++) {
            if (stages.length > 0)
                odeStages = stages[odeIdx];

            nextY[odeIdx] = getIntgMethod().getNextY().invoke(
                    fromPoint.getStep(),
                    odeStages,
                    fromPoint.getY()[odeIdx],
                    fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][odeIdx]
            );
        }

        return nextY;
    }

    @Override
    public double[][] stages(@NotNull IntgPoint fromPoint) {
        if (getIntgMethod().getStageCalculators().length == 0)
            return new double[0][];

        StageCalculator[] stageCalcs = getIntgMethod().getStageCalculators();
        int stageCount = stageCalcs.length;
        double[][] stages = new double[getDaeSystem().getDifferentialVariableCount()][stageCount];

        for (int stageIdx = 0; stageIdx < stageCount; stageIdx++) {
            StageCalculator stageCalc = stageCalcs[stageIdx];

            double[] yk = yk(stageCalc, fromPoint, stages);

            // Оптимизация для первой стадии.
            double[] fk;
            if (stageIdx == 0 && Arrays.equals(yk, fromPoint.getY())) {
                fk = fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX];
            } else {
                fk = calculateRhs(yk)[DaeSystem.RHS_DE_PART_IDX];
            }

            double[] k = k(stageCalc, fromPoint, stages, yk, fk);

            for (int i = 0; i < getDaeSystem().getDifferentialVariableCount(); i++) {
                stages[i][stageIdx] = k[i];
            }
        }

        return stages;
    }

    protected double[] yk(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages) {
        double[] yk = new double[fromPoint.getY().length];

        for (int odeIdx = 0; odeIdx < getDaeSystem().getDifferentialVariableCount(); odeIdx++) {
            yk[odeIdx] = stageCalc.yk(
                    fromPoint.getStep(),
                    fromPoint.getY()[odeIdx],
                    fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][odeIdx],
                    stages[odeIdx]
            );
        }

        return yk;
    }

    protected double[] k(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
        double[] k = new double[getDaeSystem().getDifferentialEquationCount()];

        for (int odeIdx = 0; odeIdx < getDaeSystem().getDifferentialEquationCount(); odeIdx++) {
            k[odeIdx] = stageCalc.k(
                    fromPoint.getStep(),
                    fromPoint.getY()[odeIdx],
                    fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][odeIdx],
                    stages[odeIdx],
                    yk[odeIdx],
                    fk[odeIdx]
            );
        }

        return k;
    }

    private void commitDaeSystem(DaeSystem daeSystem){
        this.daeSystem = daeSystem;
        this.aeCalculator = new AlgebraicEquationCalculator(daeSystem.getAlgebraicEquations());
        this.deCalculator = new DifferentialEquationsCalculator(daeSystem.getDifferentialEquations());
    }

    private boolean isControllerEnabled(IntgController intgController) {
        return intgController != null && intgController.getEnabled();
    }

    @Override
    public void dispose() {}
}
