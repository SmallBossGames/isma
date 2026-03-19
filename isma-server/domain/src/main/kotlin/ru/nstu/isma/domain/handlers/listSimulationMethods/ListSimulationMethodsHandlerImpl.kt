package ru.nstu.isma.domain.handlers.listSimulationMethods

class ListSimulationMethodsHandlerImpl : IListSimulationMethodsHandler {
    override fun handle(): List<SimulationMethodItem> {
        return listOf(
            SimulationMethodItem("euler", "Euler"),
            SimulationMethodItem("rk2", "Runge-Kutta 2nd order"),
            SimulationMethodItem("rk3", "Runge-Kutta 3rd order"),
            SimulationMethodItem("rk31", "Runge-Kutta 3(1)"),
            SimulationMethodItem("rkmerson", "Merson's method"),
            SimulationMethodItem("rkfehlberg", "Runge-Kutta-Fehlberg"),
        )
    }
}
