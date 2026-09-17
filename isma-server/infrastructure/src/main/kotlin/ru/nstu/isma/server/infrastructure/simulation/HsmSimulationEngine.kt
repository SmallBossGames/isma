package ru.nstu.isma.server.infrastructure.simulation

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters
import ru.nstu.isma.intg.api.methods.IIntegrationMethod
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationInitials
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.DefaultEventDetector
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetector
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetectorFactory
import ru.nstu.isma.next.core.sim.controller.services.hsm.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.services.simulators.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary

class HsmSimulationEngine(
    private val integrationMethodsLibrary: IntegrationMethodsLibrary,
    private val hsmCompiler: IHsmCompiler,
) : ISimulationEngine {

    override fun prepare(model: HSM, parameters: RunSimulationParameters): PreparedSimulation {
        model.initTimeEquation(parameters.startTime)

        val method = integrationMethodsLibrary
            .getIntegrationMethod(parameters.methodName)
            .create()

        val accuracyController = method.accuracyController
        if (accuracyController != null) {
            accuracyController.enabled = parameters.isAccuracyInUse
            if (parameters.isAccuracyInUse) {
                accuracyController.accuracy = parameters.accuracy
            }
        }

        val stabilityController = method.stabilityController
        if (stabilityController != null) {
            stabilityController.enabled = parameters.isStabilityControlInUse
        }

        val compilationResult = hsmCompiler.compile(model)

        val simulationInitials = SimulationInitials(
            differentialEquationInitials = createOdeInitials(compilationResult.indexProvider, model),
            start = parameters.startTime,
            end = parameters.endTime,
            step = parameters.initialStep,
        )

        val columnNames = buildColumnNames(compilationResult.indexProvider)

        return HsmPreparedSimulation(columnNames, parameters, method, compilationResult, simulationInitials)
    }

    override fun run(
        prepared: PreparedSimulation,
        onProgress: (currentTime: Double) -> Unit,
        isCancelled: () -> Boolean,
        onPoint: (point: DoubleArray) -> Unit,
    ) {
        val hsmPrepared = prepared as HsmPreparedSimulation

        val integrationMethodProvider = object : IIntegrationMethodProvider {
            override val method = hsmPrepared.method
        }

        val daeSystemSolverFactory = object : IDaeSystemSolverFactory {
            override fun create(result: HsmCompilationResult): DaeSystemStepSolver {
                return DefaultDaeSystemStepSolver(
                    integrationMethodProvider.method,
                    result.hybridSystem.daeSystem
                )
            }
        }

        val eventDetectorFactory = object : IEventDetectorFactory {
            override fun create(): IEventDetector? {
                val gamma = hsmPrepared.parameters.eventDetectionGamma
                val lowBorder = hsmPrepared.parameters.eventDetectionLowBorder
                return if (gamma != null && lowBorder != null) {
                    DefaultEventDetector(gamma = gamma, stepLowBound = lowBorder)
                } else {
                    null
                }
            }
        }

        val simulator = HybridSystemSimulator(daeSystemSolverFactory, eventDetectorFactory)

        simulator.runAsync(
            HybridSystemSimulatorParameters(
                hsmPrepared.compilationResult,
                hsmPrepared.simulationInitials,
                stepChangeHandlers = { currentTime ->
                    if (isCancelled()) throw InterruptedException("Simulation was cancelled")
                    onProgress(currentTime)
                },
                resultPointHandlers = { point -> onPoint(convertToDoubleArray(point)) }
            )
        )
    }

    private fun buildColumnNames(indexProvider: EquationIndexProvider): List<String> {
        val deCount = indexProvider.getDifferentialEquationCount()
        val aeCount = indexProvider.getAlgebraicEquationCount()

        val variableNames = mutableListOf<String>()
        variableNames.add("TIME")
        for (i in 0 until deCount) {
            val code = indexProvider.getDifferentialEquationCode(i)
            variableNames.add(if (code != null) "DE_$i-$code" else "DE_$i")
        }
        for (i in 0 until aeCount) {
            val code = indexProvider.getAlgebraicEquationCode(i)
            variableNames.add(if (code != null) "AE_$i-$code" else "AE_$i")
        }
        for (i in 0 until deCount) {
            variableNames.add("f$i")
        }
        return variableNames
    }

    private fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray {
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)

        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }

        return odeInitials
    }

    private fun convertToDoubleArray(point: IntgResultPoint): DoubleArray {
        val deCount = point.yForDe.size
        val aeCount = point.rhs[1].size
        val fCount = point.rhs[0].size

        val result = DoubleArray(1 + deCount + aeCount + fCount)
        var idx = 0
        result[idx++] = point.x
        for (v in point.yForDe) {
            result[idx++] = v
        }
        for (v in point.rhs[1]) {
            result[idx++] = v
        }
        for (v in point.rhs[0]) {
            result[idx++] = v
        }
        return result
    }

    private class HsmPreparedSimulation(
        override val columnNames: List<String>,
        val parameters: RunSimulationParameters,
        val method: IIntegrationMethod,
        val compilationResult: HsmCompilationResult,
        val simulationInitials: SimulationInitials,
    ) : PreparedSimulation
}
