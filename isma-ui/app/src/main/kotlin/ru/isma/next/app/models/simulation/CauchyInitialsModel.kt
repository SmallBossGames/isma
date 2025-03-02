package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
data class CauchyInitialsModel(
    val startTime: Double,
    val endTime: Double,
    val initialStep: Double,
)
