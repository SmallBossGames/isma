package ru.isma.next.services.simulation.abstractions.models

import kotlinx.serialization.Serializable
import ru.isma.next.services.simulation.abstractions.enumerables.SaveTarget

@Serializable
data class ResultSavingParametersModel(
    val savingTarget: SaveTarget
)