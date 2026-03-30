package ru.isma.next.app.models.simulation

import ru.isma.next.domain.models.IEquationIndexProvider
import ru.isma.next.domain.models.MetricData
import java.io.File

class CompletedSimulationModel(
    val id: Int,
    val modelName: String,
    val equationIndexProvider: IEquationIndexProvider,
    val metricData: MetricData,
    val parameters: SimulationParametersModel,
    val cachedFile: File,
    val cachedColumnNames: List<String>,
)