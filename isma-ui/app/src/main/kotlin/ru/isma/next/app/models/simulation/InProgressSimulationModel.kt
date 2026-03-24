package ru.isma.next.app.models.simulation

import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import java.util.concurrent.atomic.AtomicReference

class InProgressSimulationModel(
    val id: Int,
    val model: String,
    val parameters: SimulationParametersModel
) {
    private var actualProgress = 0.0

    val progressProperty = SimpleDoubleProperty(actualProgress)

    fun commitProgress(value: Double) {
        Platform.runLater {
            progressProperty.set(value)
        }
    }
}

