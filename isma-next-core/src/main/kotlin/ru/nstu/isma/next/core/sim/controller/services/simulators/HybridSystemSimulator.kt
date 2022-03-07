package ru.nstu.isma.next.core.sim.controller.services.simulators

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation
import ru.nstu.isma.intg.api.calcmodel.HybridSystemChangeSet
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.api.solvers.useAsync
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetector
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetectorFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class HybridSystemSimulator(
    private val daeSystemStepSolverFactory: IDaeSystemSolverFactory,
    private val eventDetectorFactory: IEventDetectorFactory
) : IHybridSystemSimulator {

    override suspend fun runAsync(parameters: HybridSystemSimulatorParameters) = coroutineScope {
        daeSystemStepSolverFactory.create(parameters.hsmCompilationResult).useAsync {
            return@useAsync runAsyncInternal(parameters, this, eventDetectorFactory.create())
        }
    }

    private suspend fun runAsyncInternal(
        parameters: HybridSystemSimulatorParameters,
        stepSolver: DaeSystemStepSolver,
        eventDetector: IEventDetector?,
    ): IntgMetricData = coroutineScope {
        val metricData = IntgMetricData()
        metricData.setStartTime(System.currentTimeMillis())
        var x = parameters.simulationInitials.start
        val end = parameters.simulationInitials.end
        var step = parameters.simulationInitials.step
        var yForDe = parameters.simulationInitials.differentialEquationInitials
        var changeSet: HybridSystemChangeSet
        var rhs = stepSolver.calculateRhs(yForDe)
        var isLastStep = false
        while (x < end && isActive) {
            changeSet = parameters.hsmCompilationResult.hybridSystem.checkTransitions(yForDe, rhs)
            if (!changeSet.isEmpty) {
                changeInitials(yForDe, changeSet)
                stepSolver.apply(changeSet)
                rhs = stepSolver.calculateRhs(yForDe)
            }

            parameters.resultPointHandlers.forEach {
                it(IntgResultPoint(x, yForDe.copyOf(), rhs))
            }

            val fromPoint = IntgPoint(step, yForDe, rhs)
            val toPoint = stepSolver.step(fromPoint)

            if(eventDetector != null){
                val guards = parameters.hsmCompilationResult.hybridSystem.currentState.guards
                toPoint.nextStep = eventDetector.predictNextStep(toPoint, guards)
            }

            x += toPoint.step
            step = toPoint.nextStep
            yForDe = toPoint.y
            rhs = toPoint.rhs
            if (isLastStep) {
                parameters.resultPointHandlers.forEach {
                    it(IntgResultPoint(x, yForDe.copyOf(), rhs))
                }
                break
            } else if (x + step > end) { // Если нам нужно сделать последний шаг, то меняем значение
                step = end - x
                isLastStep = true
            }
            val finalX = x
            parameters.stepChangeHandlers.forEach { it(finalX) }
        }
        metricData.setEndTime(System.currentTimeMillis())
        return@coroutineScope metricData
    }

    private fun changeInitials(yForDe: DoubleArray, changeSet: HybridSystemChangeSet) {
        val emptyRhs = emptyArray<DoubleArray>()
        changeSet.initialValueSetters.entries.forEach {
            val setter: DifferentialEquation = it.value.value
            yForDe[it.key] = setter.apply(yForDe, emptyRhs)
        }
    }
}

