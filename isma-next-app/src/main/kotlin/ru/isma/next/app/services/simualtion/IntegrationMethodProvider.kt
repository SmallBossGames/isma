package ru.isma.next.app.services.simualtion

import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.next.core.sim.controller.services.IIntegrationMethodProvider
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary

class IntegrationMethodProvider(
    private val library: IntegrationMethodsLibrary,
    private val simulationParametersService: SimulationParametersService,
) : IIntegrationMethodProvider {
    override val method: IntgMethod
        get() {
            val method = createIntegrationMethod()
            initAccuracyController(method)
            initStabilityController(method)
            return method
        }

    private fun createIntegrationMethod(): IntgMethod {
        val selectedMethod = simulationParametersService.integrationMethod.selectedMethod
        return library.getIntgMethod(selectedMethod)!!
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
}