package ru.nstu.isma.domain.simulation

import ru.nstu.isma.compiler.hsm.core.IHSM
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters

interface ISimulationExecutor {
    fun execute(
        simulationId: Long,
        parameters: RunSimulationParameters,
        model: IHSM,
    )
}
