package services

import enumerables.SaveTarget
import models.*
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import tornadofx.asObservable

class SimulationParametersService(library: IntegrationMethodsLibrary) {
    val integrationMethods = (library.getIntgMethodNames() ?: emptyList())
            .asObservable()

    val simplifyMethods = listOf("Radial-Distance", "Douglas-Peucker")
            .asObservable()

    val cauchyInitialsModel = CauchyInitialsModel()

    val integrationMethod = IntegrationMethodParametersModel()

    val resultSaving = ResultSavingParametersModel()

    val resultProcessing = ResultProcessingParametersModel()

    val eventDetection = EventDetectionParametersModel()

    init {
        integrationMethod.selectedMethod = integrationMethods.first()
        resultProcessing.selectedSimplifyMethod = simplifyMethods.first()

        cauchyInitialsModel.step = 0.1
        cauchyInitialsModel.startTime = 0.0
        cauchyInitialsModel.endTime = 10.0

        integrationMethod.accuracy = 0.1
        integrationMethod.server = "localhost"
        integrationMethod.port = 7890

        resultSaving.savingTarget = SaveTarget.MEMORY

        resultProcessing.tolerance = 20.0

        eventDetection.gamma = 0.8
        eventDetection.lowBorder = 0.001
    }

}