package ru.isma.next.app.models.simulation

import ru.nstu.isma.intg.api.models.IntgMetricData
import ru.isma.next.domain.models.IEquationIndexProvider
import java.io.File

class CompletedSimulationModel(
    val id: Int,
    val modelName: String,
    val equationIndexProvider: IEquationIndexProvider,
    val metricData: IntgMetricData,
    val parameters: SimulationParametersModel,
    val cachedFile: File,
    val cachedColumnNames: List<String>,
)