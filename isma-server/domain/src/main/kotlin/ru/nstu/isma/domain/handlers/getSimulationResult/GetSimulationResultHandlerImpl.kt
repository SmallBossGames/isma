package ru.nstu.isma.domain.handlers.getSimulationResult

import java.io.InputStream

class GetSimulationResultHandlerImpl : IGetSimulationResultHandler {
    override fun handle(simulationId: Long): InputStream {
        throw NotImplementedError("Simulation result loading not implemented")
    }
}
