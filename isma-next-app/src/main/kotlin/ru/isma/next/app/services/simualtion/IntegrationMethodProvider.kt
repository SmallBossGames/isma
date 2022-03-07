package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary

class IntegrationMethodProvider(
    library: IntegrationMethodsLibrary,
    simulationParameters: SimulationParametersModel,
) : IIntegrationMethodProvider {

    private val parameters = simulationParameters.integrationMethodParameters

    private val method = library
        .getIntegrationMethod(parameters.selectedMethod)!!
        .apply {
            initAccuracyController()
            initStabilityController()
        }

    override fun createMethod() = method

    private fun IntgMethod.initAccuracyController(){
        val accuracyController = accuracyController
        if (accuracyController != null) {
            val accuracyInUse = parameters.isAccuracyInUse
            accuracyController.isEnabled = accuracyInUse
            if (accuracyInUse){
                accuracyController.accuracy = parameters.accuracy
            }
        }
    }

    private fun IntgMethod.initStabilityController(){
        val stabilityController = stabilityController
        if (stabilityController != null) {
            stabilityController.isEnabled = parameters.isStableInUse
        }
    }
}