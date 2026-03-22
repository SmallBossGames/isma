package ru.isma.next.app.models.simulation

import ru.nstu.isma.intg.api.models.IntgMetricData
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.isma.next.domain.models.IEquationIndexProvider

class CompletedSimulationModel(
    val id: Int,
    val modelName: String,
    val equationIndexProvider: IEquationIndexProvider,
    val metricData: IntgMetricData,
    val resultPointProvider: IntegrationResultPointProvider,
    val parameters: SimulationParametersModel,
)