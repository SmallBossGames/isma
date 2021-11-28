package ru.isma.next.app.models.simulation

import javafx.beans.property.SimpleDoubleProperty
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider

class InProgressSimulationModel(val id: Int) {
    val progressProperty = SimpleDoubleProperty()
}

class CompletedSimulationModel(
    val id: Int,
    val equationIndexProvider: EquationIndexProvider,
    val metricData: IntgMetricData,
    val resultPointProvider: IntegrationResultPointProvider
)