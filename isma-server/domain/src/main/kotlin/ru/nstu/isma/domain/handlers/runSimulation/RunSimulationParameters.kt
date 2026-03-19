package ru.nstu.isma.domain.handlers.runSimulation

data class RunSimulationParameters(
    val startTime: Double,
    val endTime: Double,
    val initialStep: Double,
    val methodName: String,
    val accuracy: Double,
    val isAccuracyInUse: Boolean,
)
