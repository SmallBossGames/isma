package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
data class EventDetectionParametersModel(
    val isEventDetectionInUse: Boolean,
    val isStepLimitInUse: Boolean,
    val gamma: Double,
    val lowBorder: Double
)
