package ru.nstu.isma.domain.handlers.listSimulationMethods

import ru.nstu.isma.domain.integration.IIntegrationMethodsStore

class ListSimulationMethodsHandlerImpl(
    private val integrationMethodsStore: IIntegrationMethodsStore
) : IListSimulationMethodsHandler {
    override fun handle(): List<SimulationMethodItem> {
        return integrationMethodsStore.getMethodNames().map { name ->
            SimulationMethodItem(name, formatTitle(name))
        }
    }

    private fun formatTitle(name: String): String = when (name) {
        "euler" -> "Euler"
        "rk2" -> "Runge-Kutta 2nd order"
        "rk3" -> "Runge-Kutta 3rd order"
        "rk31" -> "Runge-Kutta 3(1)"
        "rkmerson" -> "Merson's method"
        "rkfehlberg" -> "Runge-Kutta-Fehlberg"
        else -> name.replaceFirstChar { it.uppercase() }
    }
}
