package ru.isma.next.app.models.simulation

import kotlinx.serialization.Serializable
import ru.isma.next.app.enumerables.SaveTarget

@Serializable
data class ResultSavingParametersModel(
    var savingTarget: SaveTarget
)