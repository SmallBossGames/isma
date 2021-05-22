package services.simualtion

import ru.nstu.isma.next.core.sim.controller.SimulationCoreController
import kotlinx.coroutines.*
import enumerables.SaveTarget
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import models.FailedTranslation
import models.SuccessTranslation
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.next.core.sim.controller.parameters.EventDetectionParameters
import ru.nstu.isma.next.core.sim.controller.parameters.ParallelParameters
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import services.ModelErrorService
import services.lisma.LismaPdeService
import services.project.ProjectService
import tornadofx.getValue
import tornadofx.setValue
import kotlin.math.max
import kotlin.math.min

class SimulationService(
    private val projectService: ProjectService,
    private val lismaPdeService: LismaPdeService,
    private val simulationParametersService: SimulationParametersService,
    private val simulationResult: SimulationResultService,
    private val library: IntegrationMethodsLibrary,
    private val modelService: ModelErrorService,
) {

    private val progressProperty = SimpleDoubleProperty();
    fun progressProperty() = progressProperty
    var progress by progressProperty

    private val isSimulationInProgressProperty = SimpleBooleanProperty();
    fun isSimulationInProgressProperty() = isSimulationInProgressProperty
    var isSimulationInProgress by isSimulationInProgressProperty

    private var currentSimulation: Job? = null

    fun simulate(){
        val translationResult = lismaPdeService.translateLisma(projectService.activeProject?.lismaText ?: return)

        modelService.setErrorList(emptyList())

        when (translationResult) {
            is FailedTranslation -> {
                modelService.setErrorList(translationResult.errors)
                return
            }
            is SuccessTranslation -> {
                val initials = createCauchyInitials()
                val integrationMethod = createIntegrationMethod()

                initAccuracyController(integrationMethod)
                initStabilityController(integrationMethod)

                translationResult.hsm.initTimeEquation(initials.start)

                val simulationController = SimulationCoreController(
                    translationResult.hsm,
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
        }


    }

    private fun createCauchyInitials(): CauchyInitials {
        return CauchyInitials().apply {
            start = simulationParametersService.cauchyInitialsModel.startTime
            end = simulationParametersService.cauchyInitialsModel.endTime
            stepSize = simulationParametersService.cauchyInitialsModel.step
        }
    }

    private fun createIntegrationMethod(): IntgMethod {
        val selectedMethod = simulationParametersService.integrationMethod.selectedMethod
        return library.getIntgMethod(selectedMethod)!!
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