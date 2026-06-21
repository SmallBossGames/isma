package ru.isma.next.app.models.simulation

import javafx.beans.property.*
import javafx.collections.FXCollections

enum class SimulationTaskStatus {
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}

class SimulationTask(
    val id: Long,
    val modelName: String,
    val parameters: SimulationParametersModel,
    initialStatus: SimulationTaskStatus = SimulationTaskStatus.RUNNING,
) {
    private val _status = SimpleObjectProperty(initialStatus)
    val status: ObjectProperty<SimulationTaskStatus> = _status
    val statusValue get() = _status.value
    fun setStatus(s: SimulationTaskStatus) { _status.value = s }

    private val _progress = SimpleDoubleProperty(0.0)
    val progress: DoubleProperty = _progress
    val progressValue get() = _progress.value
    fun setProgress(p: Double) { _progress.value = p.coerceIn(0.0, 1.0) }

    private val _error = SimpleObjectProperty<String?>(null)
    val error: ObjectProperty<String?> = _error
    val errorValue get() = _error.value
    fun setError(e: String?) { _error.value = e }

    var result: CompletedSimulationModel? = null

    companion object {
        val ALL = FXCollections.observableArrayList<SimulationTask>()
    }
}
