package ru.nstu.isma.intg.api.solvers;

import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;

/**
 * Решатель системы ОДУ на заданном шаге.
 *
 * @author Maria Nasyrova
 * @since 08.12.2014
 */
public interface DaeSystemStepSolver {

    IntgMethod getIntgMethod();

    void apply(DaeSystemChangeSet changeSet);

    double[][] calculateRhs(double[] yForDe);

    IntgPoint step(IntgPoint fromPoint);

    double[][] stages(IntgPoint fromPoint);
}
