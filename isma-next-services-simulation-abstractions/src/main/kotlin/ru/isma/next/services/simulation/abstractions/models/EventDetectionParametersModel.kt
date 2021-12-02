package ru.isma.next.services.simulation.abstractions.models

import kotlinx.serialization.Serializable

@Serializable
data class EventDetectionParametersModel(
    val isEventDetectionInUse: Boolean,
    val isStepLimitInUse: Boolean,
    val gamma: Double,
    val lowBorder: Double
)
