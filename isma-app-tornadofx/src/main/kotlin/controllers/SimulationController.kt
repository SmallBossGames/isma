package controllers

import kotlinx.coroutines.*
import enumerables.SaveTarget
import ru.nstu.isma.core.sim.controller.SimulationCoreController
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.lib.IntgMethodLibrary
import tornadofx.Controller
import kotlin.math.max
import kotlin.math.min

class SimulationController: Controller() {
    private val lismaPdeController: LismaPdeController by inject()
    private val simulationParametersController: SimulationParametersController by inject()
    private val simulationProgress by inject<SimulationProgressController>()

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

        GlobalScope.launch {
                simulationController.simulate()
        }
    }

    private fun normalizeProgress(start: Double, end: Double, current: Double): Double{
        return max(0.0, min(1.0, (current - start) / (end-start)))
    }
}