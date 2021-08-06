package ru.isma.next.app.services.simualtion

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.app.enumerables.SaveTarget
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.common.services.lisma.FailedTranslation
import ru.isma.next.common.services.lisma.SuccessTranslation
import ru.isma.next.common.services.lisma.services.LismaPdeService
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.next.core.sim.controller.FileStorage
import ru.nstu.isma.next.core.sim.controller.MemoryStorage
import ru.nstu.isma.next.core.sim.controller.SimulationCoreController
import ru.nstu.isma.next.core.sim.controller.SimulationResultStorageType
import ru.nstu.isma.next.core.sim.controller.parameters.EventDetectionParameters
import ru.nstu.isma.next.core.sim.controller.parameters.ParallelParameters
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import tornadofx.getValue
import tornadofx.setValue
import kotlin.coroutines.EmptyCoroutineContext
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
    private val progressProperty = SimpleDoubleProperty()
    fun progressProperty() = progressProperty
    var progress by progressProperty

    private val isSimulationInProgressProperty = SimpleBooleanProperty()
    fun isSimulationInProgressProperty() = isSimulationInProgressProperty
    var isSimulationInProgress by isSimulationInProgressProperty

    private var currentSimulationJob: Job? = null

    fun simulate() {
        currentSimulationJob = SimulationScope.launch {
            try {
                simulateAsyncInternal()
            }
            finally {
                resetState()
            }
        }
    }

    private suspend fun simulateAsyncInternal() = coroutineScope {
        val translationResult = lismaPdeService
            .translateLisma(projectService.activeProject?.lismaText ?: return@coroutineScope)

        modelService.setErrorList(emptyList())

        when (translationResult) {
            is FailedTranslation -> {
                modelService.setErrorList(translationResult.errors)
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

                initAsyncProgressTracking(simulationController, initials)

                startSimulation(simulationController)
            }
            else -> throw IllegalArgumentException()
        }
    }

    fun stopCurrentSimulation() {
        currentSimulationJob?.cancel()
    }

    private fun resetState(){
        currentSimulationJob = null
        isSimulationInProgress = false
        progress = 0.0
    }

    private fun createCauchyInitials(): CauchyInitials {
        return CauchyInitials().apply {
            start = simulationParametersService.cauchyInitials.startTime
            end = simulationParametersService.cauchyInitials.endTime
            stepSize = simulationParametersService.cauchyInitials.step
        }
    }

    private fun createIntegrationMethod(): IntgMethod {
        val selectedMethod = simulationParametersService.integrationMethod.selectedMethod
        return library.getIntgMethod(selectedMethod)!!
    }

    private fun createEventDetectionParameters(): EventDetectionParameters? {
        val eventDetectionParams = simulationParametersService.eventDetection

        return if (eventDetectionParams.isEventDetectionInUse) {
            val stepLowerBound = if (eventDetectionParams.isStepLimitInUse) eventDetectionParams.lowBorder else 0.0

            EventDetectionParameters(eventDetectionParams.gamma, stepLowerBound)
        } else {
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

    private fun createFileResultParameters(): SimulationResultStorageType {
        return when(simulationParametersService.resultSaving.savingTarget)
        {
            SaveTarget.MEMORY -> MemoryStorage
            SaveTarget.FILE -> FileStorage("temp.csv")
        }
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

    private fun initAsyncProgressTracking(
        simulationController: SimulationCoreController,
        initials: CauchyInitials
    ){
        simulationController.addStepChangeListener {
            runBlocking(Dispatchers.JavaFx) {
                progress = normalizeProgress(initials.start, initials.end, it)
            }
        }
    }

    private suspend fun startSimulation(controller: SimulationCoreController) = coroutineScope {
        withContext(Dispatchers.JavaFx) {
            isSimulationInProgress = true
        }

        val result = controller.simulateAsync()

        withContext(Dispatchers.JavaFx) {
            simulationResult.simulationResult = result
        }
    }

    companion object {
        private val SimulationSupervisor = SupervisorJob()

        val SimulationScope = CoroutineScope(EmptyCoroutineContext + SimulationSupervisor)

        private fun normalizeProgress(start: Double, end: Double, current: Double): Double{
            return max(0.0, min(1.0, (current - start) / (end-start)))
        }
    }
}