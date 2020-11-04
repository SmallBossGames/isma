package ru.nstu.isma.intg.core.solvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.solvers.CauchyProblemSolver;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Maria Nasyrova
 * @since 09.12.2014
 */
public class DefaultCauchyProblemSolver implements CauchyProblemSolver {

    private Logger logger = LoggerFactory.getLogger(DefaultCauchyProblemSolver.class);

    @Override
    public IntgMetricData solve(CauchyProblem cauchyProblem, DaeSystemStepSolver stepSolver, Consumer<IntgResultPoint> resultConsumer) {
        IntgMetricData metricData = new IntgMetricData();
        metricData.setStartTime(System.currentTimeMillis());

        double x = cauchyProblem.getCauchyInitials().getStart();
        double end = cauchyProblem.getCauchyInitials().getEnd();
        double step = cauchyProblem.getCauchyInitials().getStepSize();
        double[] yForDe = cauchyProblem.getCauchyInitials().getY0();

        double[][] rhs = stepSolver.calculateRhs(yForDe);

        long stepIndex = 0;
        boolean isLastStep = false;
        while (x < end) {
            resultConsumer.accept(new IntgResultPoint(x, Arrays.copyOf(yForDe, yForDe.length), rhs));

            IntgPoint toPoint = stepSolver.step(new IntgPoint(step, yForDe, rhs));
            if (logger.isDebugEnabled()) {
                logger.debug("Calculated step #{}. X: {}; Original step: {}, Used step: {}; Next step: {}",
                        stepIndex++, x, step, toPoint.getStep(), toPoint.getNextStep());
            }

            x += toPoint.getStep();

            step = toPoint.getNextStep();
            yForDe = toPoint.getY();
            rhs = toPoint.getRhs();

            if (isLastStep) { // Если мы прошли весь интервал, то запоминаем последние значения и выходим.
                resultConsumer.accept(new IntgResultPoint(x, Arrays.copyOf(yForDe, yForDe.length), rhs));
                break;
            } else if (x + step > end) { // Если нам нужно сделать последний шаг, то меняем значение
                step = end - x;
                isLastStep = true;
            }
        }

        //logger.debug(String.valueOf(((DefaultDaeSystemStepSolver) stepSolver).getRhsCount()));

        metricData.setEndTime(System.currentTimeMillis());
        return metricData;
    }

}
