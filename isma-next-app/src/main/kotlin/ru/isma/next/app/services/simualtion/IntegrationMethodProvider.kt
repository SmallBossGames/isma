package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary

class IntegrationMethodProvider(
    library: IntegrationMethodsLibrary,
    simulationParameters: SimulationParametersModel,
) : IIntegrationMethodProvider {

    private val parameters = simulationParameters.integrationMethodParameters

    override val method = library
        .getIntegrationMethod(parameters.selectedMethod)
        .createNg()
        .apply {
            initAccuracyController()
            initStabilityController()
        }

    private fun IntegrationMethodRungeKutta.initAccuracyController(){
        val accuracyController = accuracyController
        if (accuracyController != null) {
            val accuracyInUse = parameters.isAccuracyInUse
            accuracyController.enabled = accuracyInUse
            if (accuracyInUse){
                accuracyController.accuracy = parameters.accuracy
            }
        }
    }

    private fun IntegrationMethodRungeKutta.initStabilityController(){
        val stabilityController = stabilityController
        if (stabilityController != null) {
            stabilityController.enabled = parameters.isStableInUse
        }
    }
}