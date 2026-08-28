package ru.isma.next.app.services.simulation

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.isma.next.app.models.simulation.SimulationParametersModel

class SimulationParametersService(availableMethods: List<String>) {
    val methodNames: List<String> = availableMethods

    init {
        require(availableMethods.isNotEmpty()) { "availableMethods must not be empty" }
    }

    fun serialize(model: SimulationParametersModel): String = Json.encodeToString(model)

    fun deserialize(text: String): SimulationParametersModel = Json.decodeFromString(text)
}
