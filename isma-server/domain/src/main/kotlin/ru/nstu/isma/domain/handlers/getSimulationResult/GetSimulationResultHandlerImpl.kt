package ru.nstu.isma.domain.handlers.getSimulationResult

import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus
import java.io.FileInputStream
import java.io.InputStream

class GetSimulationResultHandlerImpl(
    private val sessionStore: ISimulationSessionStore,
) : IGetSimulationResultHandler {
    override fun handle(simulationId: Long): InputStream {
        val session = sessionStore.get(simulationId)
            ?: throw IllegalArgumentException("Simulation session not found: $simulationId")

        if (session.status != SimulationStatus.COMPLETED) {
            throw IllegalStateException("Simulation is not completed. Status: ${session.status}")
        }

        val resultFilePath = session.resultFilePath
            ?: throw IllegalStateException("Simulation completed but result file path is missing")

        return FileInputStream(resultFilePath)
    }
}
