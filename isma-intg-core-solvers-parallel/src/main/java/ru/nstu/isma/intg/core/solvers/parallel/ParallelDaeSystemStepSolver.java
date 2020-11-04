package ru.nstu.isma.intg.core.solvers.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.core.solvers.parallel.utils.MPIUtils;
import ru.nstu.isma.intg.core.solvers.utils.DataBalanceUtils;

import java.util.List;

/**
 * @author Mariya Nasyrova
 * @since 01.09.14
 */
public class ParallelDaeSystemStepSolver extends DefaultDaeSystemStepSolver {

    private static final Logger logger = LoggerFactory.getLogger(ParallelDaeSystemStepSolver.class);

    public ParallelDaeSystemStepSolver(IntgMethod method, DaeSystem odes) {
        super(method, prepareOdeSystem(odes));
    }

    public static DaeSystem prepareOdeSystem(DaeSystem daeSystem) {
        int dataPortionCount = daeSystem.getDifferentialEquationCount();
        int rankCount = MPIUtils.getSize();
        List<DataBalanceUtils.Range> ranges = DataBalanceUtils.getRankDataPortionRanges(dataPortionCount, rankCount);
        int rank = MPIUtils.getRank();
        DataBalanceUtils.Range range = ranges.get(rank);
        return daeSystem.getSubsystem(range.getStart(), range.getEnd());
    }

    @Override
    protected double[] calculateRhsForDifferentialEquations(double[] yForDe, double[][] rhs) {
        SendBuffBuilder sbb = new SendBuffBuilder() {
            @Override
            public double getSendingValue(DifferentialEquation de) {
                return de.apply(yForDe, rhs);
            }
        };

        double[] sendBuff = sbb.build(getDaeSystem(), yForDe.length);
        if (logger.isTraceEnabled()) {
            logger.trace("#{} rank: allGather rhs", MPIUtils.getRank());
        }

        return MPIUtils.allGather(sendBuff);
    }

    @Override
    protected double[] nextY(IntgPoint fromPoint, double[][] stages) {
        SendBuffBuilder sbb = new SendBuffBuilder() {
            @Override
            public double getSendingValue(DifferentialEquation ode) {
                int odeIdx = ode.getIndex();
                double[] odeStages = stages.length == 0 ? new double[0] : stages[odeIdx];
                return getIntgMethod().nextY(
                        fromPoint.getStep(),
                        odeStages,
                        fromPoint.getY()[odeIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][odeIdx]);
            }
        };

        double[] sendBuff = sbb.build(getDaeSystem(), fromPoint.getY().length);
        if (logger.isTraceEnabled()) {
            logger.trace("#{} rank: allGather nextY", MPIUtils.getRank());
        }

        return MPIUtils.allGather(sendBuff);
    }

    @Override
    protected double[] yk(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages) {
        SendBuffBuilder sbb = new SendBuffBuilder() {
            @Override
            public double getSendingValue(DifferentialEquation ode) {
                int odeIdx = ode.getIndex();
                return stageCalc.yk(
                        fromPoint.getStep(),
                        fromPoint.getY()[odeIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][odeIdx],
                        stages[odeIdx]
                );
            }
        };

        double[] sendBuff = sbb.build(getDaeSystem(), fromPoint.getY().length);
        if (logger.isTraceEnabled()) {
            logger.trace("#{} rank: allGather yk", MPIUtils.getRank());
        }

        return MPIUtils.allGather(sendBuff);
    }

    @Override
    protected double[] k(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
        SendBuffBuilder sbb = new SendBuffBuilder() {
            @Override
            public double getSendingValue(DifferentialEquation ode) {
                int odeIdx = ode.getIndex();
                return stageCalc.k(
                        fromPoint.getStep(),
                        fromPoint.getY()[odeIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][odeIdx],
                        stages[odeIdx],
                        yk[odeIdx],
                        fk[odeIdx]
                );
            }
        };

        double[] sendBuff = sbb.build(getDaeSystem(), fromPoint.getY().length);
        if (logger.isTraceEnabled()) {
            logger.trace("#{} rank: allGather k", MPIUtils.getRank());
        }

        return MPIUtils.allGather(sendBuff);
    }

    private static abstract class SendBuffBuilder {

        public abstract double getSendingValue(DifferentialEquation de);

        public double[] build(DaeSystem daeSystem, int varCount) {
            /**
             *  Для ускорения процесса передачи данных все данные будут передаваться в одномерном массиве.
             *  В этом массиве каждое передаваемое значение будет занимать 2 элемента: индекс и само значение.
             *  Поскольку неизвестно, сколько на каком узле считается уравнений, то для каждого узла выделяется
             *  память для всей системы уравнений.
             */
            int sendingSize = varCount * 2;
            double[] sendBuff = new double[sendingSize];

            int i = 0;
            for (DifferentialEquation de : daeSystem.getDifferentialEquations()) { // Для каждого уравнения на текущем узле.
                sendBuff[i + 0] = de.getIndex(); // Сохраняем индекс уравнения.
                sendBuff[i + 1] = getSendingValue(de); // Сохраняем вычисленное значение.
                i += 2; // Перешагиваем на 2 элемента.
            }

            // Поскольку мы заполнили все данные, что у нас есть, устанавливаем метку,
            // которая при получении укажет другим узлам, что данных больше нет.
            if (i < sendingSize) {
                sendBuff[i] = -1.0;
            }

            return sendBuff;
        }

    }

}
