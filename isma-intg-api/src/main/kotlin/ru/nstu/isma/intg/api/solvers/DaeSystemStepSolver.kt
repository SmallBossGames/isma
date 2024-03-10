package ru.nstu.isma.intg.api.solvers

import kotlinx.coroutines.coroutineScope
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgPoint

/**
 * Решатель системы ОДУ на заданном шаге.
 *
 * @author Maria Nasyrova
 * @since 08.12.2014
 */
interface DaeSystemStepSolver {
    val intgMethod: IntegrationMethodRungeKutta
    fun apply(changeSet: DaeSystemChangeSet?)
    fun calculateRhs(yForDe: DoubleArray): Array<DoubleArray>
    fun step(fromPoint: IntgPoint): IntgPoint
    fun stages(fromPoint: IntgPoint): Array<DoubleArray>
    fun dispose()
}

fun DaeSystemStepSolver.use(op: DaeSystemStepSolver.() -> Unit) {
    try {
        op()
    } finally {
        dispose()
    }
}

suspend inline fun <T> DaeSystemStepSolver.useAsync(crossinline op: suspend DaeSystemStepSolver.() -> T) = coroutineScope {
    try {
        return@coroutineScope op()
    } finally {
        dispose()
    }
}