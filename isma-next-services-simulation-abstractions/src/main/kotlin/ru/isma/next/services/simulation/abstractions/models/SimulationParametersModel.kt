package ru.isma.next.services.simulation.abstractions.models

import kotlinx.serialization.Serializable

@Serializable
data class SimulationParametersModel(
    val cauchyInitials: CauchyInitialsModel,
    val eventDetectionParameters: EventDetectionParametersModel,
    val integrationMethodParameters: IntegrationMethodParametersModel,
    val resultSavingParameters: ResultSavingParametersModel,
)