package ru.nstu.isma.domain.handlers.getSimulationResult

import java.io.InputStream

interface IGetSimulationResultHandler {
    fun handle(simulationId: String): InputStream
}
