package ru.isma.next.external.dtos

data class RunSimulationParams(
    val startTime: Double,
    val endTime: Double,
    val initialStep: Double,
    val methodName: String,
    val accuracy: Double,
    val isAccuracyInUse: Boolean,
    val isStabilityControlInUse: Boolean,
    val compiledModelId: String,
    val eventDetectionGamma: Double? = null,
    val eventDetectionLowBorder: Double? = null,
)
