package ru.isma.next.app.models.simulation

import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider

class CompletedSimulationModel(
    val id: Int,
    val modelName: String,
    val equationIndexProvider: EquationIndexProvider,
    val metricData: IntgMetricData,
    val resultPointProvider: IntegrationResultPointProvider,
    val parameters: SimulationParametersModel,
)