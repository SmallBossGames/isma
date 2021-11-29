package ru.isma.next.app.models.simulation

import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.withContext

class InProgressSimulationModel(
    val id: Int,
    val parameters: SimulationParametersModel
) {
    val progressProperty = SimpleDoubleProperty()

    suspend fun commitProgress(value: Double) = withContext(Dispatchers.JavaFx) {
        progressProperty.value = value
    }
}

