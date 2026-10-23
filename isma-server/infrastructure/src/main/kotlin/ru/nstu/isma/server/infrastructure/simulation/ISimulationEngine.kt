package ru.nstu.isma.server.infrastructure.simulation

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters

interface ISimulationEngine {
    fun prepare(model: HSM, parameters: RunSimulationParameters): PreparedSimulation
    fun run(
        prepared: PreparedSimulation,
        onProgress: (currentTime: Double) -> Unit,
        isCancelled: () -> Boolean,
        onPoint: (point: DoubleArray) -> Unit,
    )
}

interface PreparedSimulation {
    val columnNames: List<String>
}
