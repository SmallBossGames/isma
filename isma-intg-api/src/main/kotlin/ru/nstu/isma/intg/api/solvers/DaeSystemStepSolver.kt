package ru.nstu.isma.intg.api.solvers

import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet
import ru.nstu.isma.intg.api.methods.IntgPoint

/**
 * Решатель системы ОДУ на заданном шаге.
 *
 * @author Maria Nasyrova
 * @since 08.12.2014
 */
interface DaeSystemStepSolver {
    val intgMethod: IntgMethod
    fun apply(changeSet: DaeSystemChangeSet)
    fun calculateRhs(yForDe: DoubleArray): Array<DoubleArray>
    fun step(fromPoint: IntgPoint): IntgPoint
    fun stages(fromPoint: IntgPoint): Array<DoubleArray>
}