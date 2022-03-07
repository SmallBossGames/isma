package ru.isma.next.services.simulation.abstractions.models

import kotlinx.serialization.Serializable

@Serializable
data class CauchyInitialsModel(
    val startTime: Double,
    val endTime: Double,
    val initialStep: Double,
)
