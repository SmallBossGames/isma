package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable

@Serializable
data class ResultSavingParametersModel(
    val savingTarget: SaveTarget
)