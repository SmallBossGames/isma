package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
data class IntegrationMethodParametersModel(
    val selectedMethod: String,
    val accuracy: Double,
    val isAccuracyInUse: Boolean,
    val isStableAllowedInUse: Boolean,
    val isStableInUse: Boolean,
    val isParallelInUse: Boolean,
    val server: String,
    val port: Int,
)
