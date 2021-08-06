package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.IntgResultPoint
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.calcmodel.HybridSystemChangeSet
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import kotlin.math.max
import kotlin.math.min

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class HybridSystemSimulator {
    private val stepChangeListeners = ArrayList<(value: Double) -> Unit>()

    fun addStepChangeListener(c: (value: Double) -> Unit) {
        stepChangeListeners.add(c)
    }

    suspend fun runAsync(
        hybridSystem: HybridSystem,
        stepSolver: DaeSystemStepSolver,
        simulationInitials: SimulationInitials,
        eventDetector: EventDetectionIntgController,
        eventDetectionStepBoundLow: Double,
        resultPointConsumer: suspend (point: IntgResultPoint) -> Unit
    ): IntgMetricData = coroutineScope {
        val metricData = IntgMetricData()
        metricData.setStartTime(System.currentTimeMillis())
        var x = simulationInitials.start
        val end = simulationInitials.end
        var step = simulationInitials.step
        var yForDe = simulationInitials.differentialEquationInitials
        var changeSet: HybridSystemChangeSet
        var rhs = stepSolver.calculateRhs(yForDe)
        var isLastStep = false
        while (x < end && isActive) {
            changeSet = hybridSystem.checkTransitions(yForDe, rhs)
            if (!changeSet.isEmpty) {
                changeInitials(yForDe, changeSet)
                stepSolver.apply(changeSet)
                rhs = stepSolver.calculateRhs(yForDe)
            }
            resultPointConsumer(IntgResultPoint(x, yForDe.copyOf(), rhs))
            val fromPoint = IntgPoint(step, yForDe, rhs)
            val toPoint = stepSolver.step(fromPoint)

            // TODO: сделать правильно, чтобы первый шаг тоже оценивался.
            if (eventDetector.isEnabled) {
                val guards = hybridSystem.currentState.guards
                val eventFunctionGroups = guards.map { it.eventFunctionGroup }
                val predictedStep = eventDetector.predictNextStep(toPoint, eventFunctionGroups)
                // TODO: если так, то шагов около 8500
                val nextStep = min(max(predictedStep, eventDetectionStepBoundLow), toPoint.nextStep)
                // TODO: а если так, то 170
                //val nextStep = min(max(predictedStep, eventDetectionStepBoundLow), simulationInitials.step)
                toPoint.nextStep = nextStep
            }
            x += toPoint.step
            step = toPoint.nextStep
            yForDe = toPoint.y
            rhs = toPoint.rhs
            if (isLastStep) {
                resultPointConsumer(IntgResultPoint(x, yForDe.copyOf(), rhs))
                break
            } else if (x + step > end) { // Если нам нужно сделать последний шаг, то меняем значение
                step = end - x
                isLastStep = true
            }
            val finalX = x
            stepChangeListeners.forEach { it(finalX) }
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