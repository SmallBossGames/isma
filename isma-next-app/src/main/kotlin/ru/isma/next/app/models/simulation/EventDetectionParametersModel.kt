package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
data class EventDetectionParametersModel(
    var isEventDetectionInUse: Boolean,
    var isStepLimitInUse: Boolean,
    var gamma: Double,
    var lowBorder: Double
)
