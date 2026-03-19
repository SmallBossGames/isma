package ru.nstu.isma.domain.handlers.getSimulationResult

import java.io.InputStream

class GetSimulationResultHandlerImpl : IGetSimulationResultHandler {
    override fun handle(simulationId: String): InputStream {
        // TODO: implement simulation result loading
        throw NotImplementedError("Simulation result loading not implemented")
    }
}
