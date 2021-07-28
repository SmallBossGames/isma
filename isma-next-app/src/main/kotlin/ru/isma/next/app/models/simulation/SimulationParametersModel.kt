package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
class SimulationParametersModel(
    val cauchyInitials: CauchyInitialsModel,
    val eventDetectionParameters: EventDetectionParametersModel,
    val integrationMethodParameters: IntegrationMethodParametersModel,
    val resultSavingParameters: ResultSavingParametersModel,
)