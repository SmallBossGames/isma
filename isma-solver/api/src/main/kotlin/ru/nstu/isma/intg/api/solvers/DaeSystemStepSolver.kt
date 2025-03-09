package ru.nstu.isma.intg.api.solvers

import kotlinx.coroutines.coroutineScope
import ru.nstu.isma.compiler.hsm.jvm.calcmodel.DaeSystemChangeSet
import ru.nstu.isma.intg.api.methods.IntgPoint

/**
 * Решатель системы ОДУ на заданном шаге.
 *
 * @author Maria Nasyrova
 * @since 08.12.2014
 */
interface DaeSystemStepSolver {
    fun apply(changeSet: DaeSystemChangeSet?)
    fun calculateRhs(yForDe: DoubleArray): Array<DoubleArray>
    fun step(fromPoint: IntgPoint): IntgPoint
    fun stages(fromPoint: IntgPoint): Array<DoubleArray>
}