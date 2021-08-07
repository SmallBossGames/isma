package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation
import ru.nstu.isma.intg.api.calcmodel.HybridSystemChangeSet
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.next.core.sim.controller.contracts.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import kotlin.math.max
import kotlin.math.min

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class HybridSystemSimulator : IHybridSystemSimulator {
    override suspend fun runAsync(parameters: HybridSystemSimulatorParameters): IntgMetricData = coroutineScope {
        val metricData = IntgMetricData()
        metricData.setStartTime(System.currentTimeMillis())
        var x = parameters.simulationInitials.start
        val end = parameters.simulationInitials.end
        var step = parameters.simulationInitials.step
        var yForDe = parameters.simulationInitials.differentialEquationInitials
        var changeSet: HybridSystemChangeSet
        var rhs = parameters.stepSolver.calculateRhs(yForDe)
        var isLastStep = false
        while (x < end && isActive) {
            changeSet = parameters.hybridSystem.checkTransitions(yForDe, rhs)
            if (!changeSet.isEmpty) {
                changeInitials(yForDe, changeSet)
                parameters.stepSolver.apply(changeSet)
                rhs = parameters.stepSolver.calculateRhs(yForDe)
            }

            parameters.resultPointHandlers.forEach {
                it(IntgResultPoint(x, yForDe.copyOf(), rhs))
            }

            val fromPoint = IntgPoint(step, yForDe, rhs)
            val toPoint = parameters.stepSolver.step(fromPoint)

            // TODO: сделать правильно, чтобы первый шаг тоже оценивался.
            if (parameters.eventDetector.isEnabled) {
                val guards = parameters.hybridSystem.currentState.guards
                val eventFunctionGroups = guards.map { it.eventFunctionGroup }
                val predictedStep = parameters.eventDetector.predictNextStep(toPoint, eventFunctionGroups)
                // TODO: если так, то шагов около 8500
                val nextStep = min(max(predictedStep, parameters.eventDetectionStepBoundLow), toPoint.nextStep)
                // TODO: а если так, то 170
                //val nextStep = min(max(predictedStep, eventDetectionStepBoundLow), simulationInitials.step)
                toPoint.nextStep = nextStep
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

