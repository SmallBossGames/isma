package ru.isma.next.app.models.simulation

import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.withContext
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider

class InProgressSimulationModel(val id: Int) {
    val progressProperty = SimpleDoubleProperty()

    suspend fun commitProgress(value: Double) = withContext(Dispatchers.JavaFx) {
        progressProperty.value = value
    }
}

class CompletedSimulationModel(
    val id: Int,
    val equationIndexProvider: EquationIndexProvider,
    val metricData: IntgMetricData,
    val resultPointProvider: IntegrationResultPointProvider
)