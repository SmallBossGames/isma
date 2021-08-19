package ru.nstu.isma.next.core.sim.controller.services

import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.providers.MemoryPointProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.contracts.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters

class InMemorySimulationRunner(
    private val hybridSystemSimulator: IHybridSystemSimulator
) : ISimulationRunner {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun run(context: SimulationParameters): HybridSystemIntegrationResult = coroutineScope {
        val resultMemoryStore = MemoryPointProvider()

        val simulatorParameters = HybridSystemSimulatorParameters(
            context.hybridSystem,
            context.stepSolver,
            context.simulationInitials,
            context.eventDetector,
            context.eventDetectionStepBoundLow,
            stepChangeHandlers = context.stepChangeHandlers,
            resultPointHandlers = arrayListOf(
                {
                    resultMemoryStore.accept(it)
                }
            )
        )

        val metricData: IntgMetricData = hybridSystemSimulator.runAsync(simulatorParameters)

        logCalculationStatistic(metricData, context.stepSolver)

        return@coroutineScope HybridSystemIntegrationResult(context.indexProvider, metricData, resultMemoryStore)
    }

    private fun logCalculationStatistic(
        metricData: IntgMetricData?,
        stepSolver: DaeSystemStepSolver
    ) {
        if (logger.isInfoEnabled) {
            val stepCalculationCount = if (stepSolver is DefaultDaeSystemStepSolver) stepSolver.stepCalculationCount else -1
            val rhsCalculationCount: Long = if (stepSolver is DefaultDaeSystemStepSolver) stepSolver.rhsCalculationCount else -1
            logger.info(
                "Simulation time: {} ms; Step calculation count: {}; RHS calculation count: {}",
                metricData?.simulationTime,
                stepCalculationCount,
                rhsCalculationCount)
        }
    }
}