package services

import ru.nstu.isma.next.core.sim.controller.SimulationCoreController
import kotlinx.coroutines.*
import enumerables.SaveTarget
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject as koinInject
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.lib.IntgMethodLibrary
import ru.nstu.isma.next.core.sim.controller.parameters.EventDetectionParameters
import ru.nstu.isma.next.core.sim.controller.parameters.ParallelParameters
import services.LismaPdeService
import services.SimulationParametersService
import services.SimulationResultService
import tornadofx.Controller
import tornadofx.getValue
import tornadofx.setValue
import kotlin.math.max
import kotlin.math.min

class SimulationController(
    private val lismaPdeService: LismaPdeService,
    private val simulationParametersService: SimulationParametersService,
    private val simulationResult: SimulationResultService) {

    private val progressProperty = SimpleDoubleProperty();
    fun progressProperty() = progressProperty
    var progress by progressProperty

    private val isSimulationInProgressProperty = SimpleBooleanProperty();
    fun isSimulationInProgressProperty() = isSimulationInProgressProperty
    var isSimulationInProgress by isSimulationInProgressProperty

    private var currentSimulation: Job? = null

    fun simulate(){
        val hsm = lismaPdeService.translateLisma() ?: return
        val initials = createCauchyInitials()
        val integrationMethod = createIntegrationMethod()

        initAccuracyController(integrationMethod)
        initStabilityController(integrationMethod)

        hsm.initTimeEquation(initials.start)

        val simulationController = SimulationCoreController(
                hsm,
                initials,
                integrationMethod,
                createParallelParameters(),
                createFileResultParameters(),
                createEventDetectionParameters()
        )

        initProgressTracking(simulationController, initials)

        currentSimulation?.cancel()

        currentSimulation = startSimualtionJob(simulationController)
    }

    private fun createCauchyInitials(): CauchyInitials {
        val initials = CauchyInitials()
        initials.start = simulationParametersService.cauchyInitialsModel.startTime
        initials.end = simulationParametersService.cauchyInitialsModel.endTime
        initials.stepSize = simulationParametersService.cauchyInitialsModel.step

        return initials
    }

    private fun createIntegrationMethod(): IntgMethod {
        val selectedMethod = simulationParametersService.integrationMethod.selectedMethod
        return IntgMethodLibrary.getIntgMethod(selectedMethod)
    }

    private fun createEventDetectionParameters(): EventDetectionParameters? {
        return if (simulationParametersService.eventDetection.isEventDetectionInUse){
            val stepLowerBound = if (simulationParametersService.eventDetection.isStepLimitInUse)
                simulationParametersService.eventDetection.lowBorder
            else
                0.0

            EventDetectionParameters(simulationParametersService.eventDetection.gamma, stepLowerBound)
        }
        else {
            null
        }
    }

    private fun createParallelParameters(): ParallelParameters? {
        return if (simulationParametersService.integrationMethod.isParallelInUse){
            ParallelParameters(
                    simulationParametersService.integrationMethod.server,
                    simulationParametersService.integrationMethod.port)
        }
        else {
            null
        }
    }

    private fun createFileResultParameters(): String? {
        return if (simulationParametersService.resultSaving.savingTarget == SaveTarget.FILE)
            "temp.csv"
        else
            null
    }


    private fun normalizeProgress(start: Double, end: Double, current: Double): Double{
        return max(0.0, min(1.0, (current - start) / (end-start)))
    }

    private fun initAccuracyController(integrationMethod: IntgMethod){
        val accuracyController = integrationMethod.accuracyController
        if (accuracyController != null) {
            val accuracyInUse = simulationParametersService.integrationMethod.isAccuracyInUse
            accuracyController.isEnabled = accuracyInUse
            if (accuracyInUse){
                accuracyController.accuracy = simulationParametersService.integrationMethod.accuracy
            }
        }
    }

    private fun initStabilityController(integrationMethod: IntgMethod){
        val stabilityController = integrationMethod.stabilityController
        if (stabilityController != null) {
            stabilityController.isEnabled = simulationParametersService.integrationMethod.isStableInUse
        }
    }

    private fun initProgressTracking(
            simulationController: SimulationCoreController,
            initials: CauchyInitials){
        simulationController.addStepChangeListener {
            progress = normalizeProgress(initials.start, initials.end, it)
        }
    }

    private fun startSimualtionJob(controller: SimulationCoreController) = GlobalScope.launch {
        try {
            isSimulationInProgress = true
            val result = controller.simulate()
            simulationResult.simulationResult = result
        } finally {
            isSimulationInProgress = false
            progress = 0.0
            currentSimulation = null
        }
    }
}