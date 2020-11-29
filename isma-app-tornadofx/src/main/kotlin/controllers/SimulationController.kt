package controllers

import ru.nstu.isma.next.core.sim.controller.SimulationCoreController
import kotlinx.coroutines.*
import enumerables.SaveTarget
import ru.nstu.isma.intg.api.IntgResultPoint
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.lib.IntgMethodLibrary
import tornadofx.Controller
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min

class SimulationController: Controller() {
    private val lismaPdeController: LismaPdeController by inject()
    private val simulationParametersController: SimulationParametersController by inject()
    private val simulationProgress by inject<SimulationProgressController>()

    private var currentSimulation: Job? = null

    fun simulate(){
        val hsm = lismaPdeController.translateLisma() ?: return

        val initials = CauchyInitials()
        initials.start = simulationParametersController.cauchyInitialsModel.startTime
        initials.end = simulationParametersController.cauchyInitialsModel.endTime
        initials.stepSize = simulationParametersController.cauchyInitialsModel.step

        val integrationMethod = IntgMethodLibrary
                .getIntgMethod(simulationParametersController.integrationMethod.selectedMethod)

        val accuracyController = integrationMethod.accuracyController
        if (accuracyController != null) {
            val accuracyInUse = simulationParametersController.integrationMethod.isAccuracyInUse
            accuracyController.isEnabled = accuracyInUse
            if (accuracyInUse){
                accuracyController.accuracy = simulationParametersController.integrationMethod.accuracy
            }
        }

        val stabilityController = integrationMethod.stabilityController
        if (stabilityController != null) {
            stabilityController.isEnabled = simulationParametersController.integrationMethod.isStableInUse
        }


        hsm.initTimeEquation(initials.start)

        val resultFileName: String? =
                if (simulationParametersController.resultSaving.savingTarget == SaveTarget.FILE)
                    "temp.csv"
                else null

        val eventDetectionEnabled: Boolean = simulationParametersController.eventDetection.isEventDetectionInUse
        val eventDetectionGamma = if (eventDetectionEnabled) simulationParametersController.eventDetection.gamma else 0.0

        val eventDetectionStepBoundLow = if (simulationParametersController.eventDetection.isStepLimitInUse) {
            simulationParametersController.eventDetection.lowBorder
        }
        else {
            Double.MIN_VALUE
        }

        val simulationController = SimulationCoreController(
                hsm,
                initials,
                integrationMethod,
                simulationParametersController.integrationMethod.isParallelInUse,
                simulationParametersController.integrationMethod.server,
                simulationParametersController.integrationMethod.port,
                resultFileName,
                eventDetectionEnabled,
                eventDetectionGamma,
                eventDetectionStepBoundLow
        )

        simulationController.addStepChangeListener {
            simulationProgress.progress =
                    normalizeProgress(initials.start, initials.end, it)
        }

        currentSimulation?.cancel()
        currentSimulation = null

        currentSimulation = startSimualtionJob(simulationController)
    }

    fun simulateAsync() = GlobalScope.launch {
        val hsm = lismaPdeController.translateLisma() ?: return@launch
        val initials = createCauchyInitials()
        val integrationMethod = createIntegrationMethod()

        initAccuracyController(integrationMethod)
        initStabilityController(integrationMethod)

        hsm.initTimeEquation(initials.start)

        val resultFileName: String? =
                if (simulationParametersController.resultSaving.savingTarget == SaveTarget.FILE)
                    "temp.csv"
                else null

        val eventDetectionEnabled: Boolean = simulationParametersController.eventDetection.isEventDetectionInUse
        val eventDetectionGamma = if (eventDetectionEnabled)
            simulationParametersController.eventDetection.gamma
        else
            0.0

        val eventDetectionStepBoundLow = if (simulationParametersController.eventDetection.isStepLimitInUse) {
            simulationParametersController.eventDetection.lowBorder
        }
        else {
            Double.MIN_VALUE
        }

        val simulationController = SimulationCoreController(
                hsm,
                initials,
                integrationMethod,
                simulationParametersController.integrationMethod.isParallelInUse,
                simulationParametersController.integrationMethod.server,
                simulationParametersController.integrationMethod.port,
                resultFileName,
                eventDetectionEnabled,
                eventDetectionGamma,
                eventDetectionStepBoundLow
        )

        initProgressTracking(simulationController, initials)

        currentSimulation?.cancelAndJoin()
        currentSimulation = null

        currentSimulation = startSimualtionJob(simulationController)
    }

    private fun createCauchyInitials(): CauchyInitials {
        val initials = CauchyInitials()
        initials.start = simulationParametersController.cauchyInitialsModel.startTime
        initials.end = simulationParametersController.cauchyInitialsModel.endTime
        initials.stepSize = simulationParametersController.cauchyInitialsModel.step

        return initials
    }

    private fun createIntegrationMethod(): IntgMethod {
        val selectedMethod = simulationParametersController.integrationMethod.selectedMethod
        return IntgMethodLibrary.getIntgMethod(selectedMethod)
    }


    private fun normalizeProgress(start: Double, end: Double, current: Double): Double{
        return max(0.0, min(1.0, (current - start) / (end-start)))
    }

    private fun initAccuracyController(integrationMethod: IntgMethod){
        val accuracyController = integrationMethod.accuracyController
        if (accuracyController != null) {
            val accuracyInUse = simulationParametersController.integrationMethod.isAccuracyInUse
            accuracyController.isEnabled = accuracyInUse
            if (accuracyInUse){
                accuracyController.accuracy = simulationParametersController.integrationMethod.accuracy
            }
        }
    }

    private fun initStabilityController(integrationMethod: IntgMethod){
        val stabilityController = integrationMethod.stabilityController
        if (stabilityController != null) {
            stabilityController.isEnabled = simulationParametersController.integrationMethod.isStableInUse
        }
    }

    private fun initProgressTracking(
            simulationController: SimulationCoreController,
            initials: CauchyInitials){
        simulationController.addStepChangeListener {
            simulationProgress.progress = normalizeProgress(initials.start, initials.end, it)
        }
    }

    private fun startSimualtionJob(controller: SimulationCoreController) = GlobalScope.launch {
        try {
            val result = controller.simulate()
            var resultList: List<IntgResultPoint>
            val consumer = Consumer<List<IntgResultPoint>> {
                resultList = it
            }
            result.resultPointProvider!!.read(consumer);
        } finally {
            simulationProgress.progress = 0.0
        }
    }
}