package ru.isma.next.app.services.simulation

import ru.isma.next.app.models.simulation.SimulationTask

interface ISimulationService {

    fun simulate()

    fun stopSimulation(task: SimulationTask)
}
