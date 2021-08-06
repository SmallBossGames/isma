package ru.nstu.isma.intg.api.solvers;

import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.models.IntgResultPoint;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;

import java.util.function.Consumer;

/**
 * Решатель задачи Коши.
 *
 * @author Mariya Nasyrova
 * @since 01.09.14
 */
public interface CauchyProblemSolver {

    /**
     * Решает задачу Коши указанным методом интегрирования.
     *
     * @param cauchyProblem  задача Коши, которую необходимо решить.
     * @param stepSolver     решатель систем ДАУ.
     * @param resultConsumer
	 * @return IntgResult   результат решения задачи Коши.
	 */
	IntgMetricData solve(CauchyProblem cauchyProblem, DaeSystemStepSolver stepSolver, Consumer<IntgResultPoint> resultConsumer);
}
