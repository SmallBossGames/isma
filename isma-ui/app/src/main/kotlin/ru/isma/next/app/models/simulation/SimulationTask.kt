package ru.isma.next.app.models.simulation

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
) {
    var status: SimulationTaskStatus = SimulationTaskStatus.RUNNING

    var progress: Double = 0.0
        set(value) { field = value.coerceIn(0.0, 1.0) }

    var error: String? = null

    var result: CompletedSimulationModel? = null
}
