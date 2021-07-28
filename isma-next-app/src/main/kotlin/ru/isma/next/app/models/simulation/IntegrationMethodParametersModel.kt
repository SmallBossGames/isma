package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
data class IntegrationMethodParametersModel(
    var selectedMethod: String,
    var accuracy: Double,
    var isAccuracyInUse: Boolean,
    var isStableAllowedInUse: Boolean,
    var isStableInUse: Boolean,
    var isParallelInUse: Boolean,
    var server: String,
    var port: Int,
)
