package ru.nstu.isma.next.core.sim.controller

import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.IntgResultPoint
import ru.nstu.isma.intg.api.calcmodel.*
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.math.max
import kotlin.math.min

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class HybridSystemSimulator {
    private val stepChangeListeners: MutableList<Consumer<Double>> = ArrayList()
    fun addStepChangeListener(c: Consumer<Double>) {
        stepChangeListeners.add(c)
    }

    fun run(hybridSystem: HybridSystem,
            stepSolver: DaeSystemStepSolver,
            simulationInitials: SimulationInitials,
            eventDetector: EventDetectionIntgController,
            eventDetectionStepBoundLow: Double,
            resultPointConsumer: Consumer<IntgResultPoint?>
    ): IntgMetricData {
        val metricData = IntgMetricData()
        metricData.setStartTime(System.currentTimeMillis())
        var x = simulationInitials.start
        val end = simulationInitials.end
        var step = simulationInitials.step
        var yForDe = simulationInitials.differentialEquationInitials
        var changeSet: HybridSystemChangeSet
        var rhs = stepSolver.calculateRhs(yForDe)
        var isLastStep = false
        while (x < end && !Thread.currentThread().isInterrupted) {
            changeSet = hybridSystem.checkTransitions(yForDe, rhs)
            if (!changeSet.isEmpty) {
                changeInitials(yForDe, changeSet)
                stepSolver.apply(changeSet)
                rhs = stepSolver.calculateRhs(yForDe)
            }
            resultPointConsumer.accept(IntgResultPoint(x, yForDe.copyOf(yForDe.size), rhs))
            val fromPoint = IntgPoint(step, yForDe, rhs)
            val toPoint = stepSolver.step(fromPoint)

            // TODO: сделать правильно, чтобы первый шаг тоже оценивался.
            if (eventDetector.isEnabled) {
                val guards = hybridSystem.currentState.guards
                val eventFunctionGroups = guards.map { it.eventFunctionGroup }
                val predictedStep = eventDetector.predictNextStep(toPoint, eventFunctionGroups)
                // TODO: если так, то шагов около 8500
                val nextStep = min(max(predictedStep, eventDetectionStepBoundLow), toPoint.nextStep);
                // TODO: а если так, то 170
                //val nextStep = min(max(predictedStep, eventDetectionStepBoundLow), simulationInitials.step)
                toPoint.nextStep = nextStep
            }
            x += toPoint.step
            step = toPoint.nextStep
            yForDe = toPoint.y
            rhs = toPoint.rhs
            if (isLastStep) {
                resultPointConsumer.accept(IntgResultPoint(x, yForDe.copyOf(yForDe.size), rhs))
                break
            } else if (x + step > end) { // Если нам нужно сделать последний шаг, то меняем значение
                step = end - x
                isLastStep = true
            }
            val finalX = x
            stepChangeListeners.forEach { it.accept(finalX) }
        }
        metricData.setEndTime(System.currentTimeMillis())
        return metricData
    }

    private fun changeInitials(yForDe: DoubleArray, changeSet: HybridSystemChangeSet) {
        val emptyRhs = emptyArray<DoubleArray>()
        changeSet.initialValueSetters.entries.forEach {
            val setter: DifferentialEquation = it.value.value
            yForDe[it.key] = setter.apply(yForDe, emptyRhs)
        }
    }
}