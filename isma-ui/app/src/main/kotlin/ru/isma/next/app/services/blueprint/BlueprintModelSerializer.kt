package ru.isma.next.app.services.blueprint

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel

@Serializable
private data class SerializableBlueprintModel(
    val main: SerializableBlueprintStateModel,
    val init: SerializableBlueprintStateModel,
    val states: Array<SerializableBlueprintStateModel>,
    val transactions: Array<SerializableBlueprintTransactionModel>,
    val loopTransactions: Array<SerializableBlueprintLoopTransactionModel>
)

@Serializable
private data class SerializableBlueprintStateModel(
    val canvasPositionX: Double,
    val canvasPositionY: Double,
    val name: String,
    val text: String
)

@Serializable
private data class SerializableBlueprintTransactionModel(
    val startStateName: String,
    val endStateName: String,
    val predicate: String,
    val alias: String = ""
)

@Serializable
private data class SerializableBlueprintLoopTransactionModel(
    val stateName: String,
    val predicate: String,
    val alias: String = "",
    val text: String
)

object BlueprintModelSerializer {

    fun toJson(model: BlueprintModel): String {
        val serializable = SerializableBlueprintModel(
            main = SerializableBlueprintStateModel(
                model.main.canvasPositionX,
                model.main.canvasPositionY,
                model.main.name,
                model.main.text
            ),
            init = SerializableBlueprintStateModel(
                model.init.canvasPositionX,
                model.init.canvasPositionY,
                model.init.name,
                model.init.text
            ),
            states = model.states.map {
                SerializableBlueprintStateModel(it.canvasPositionX, it.canvasPositionY, it.name, it.text)
            }.toTypedArray(),
            transactions = model.transactions.map {
                SerializableBlueprintTransactionModel(it.startStateName, it.endStateName, it.predicate, it.alias)
            }.toTypedArray(),
            loopTransactions = model.loopTransactions.map {
                SerializableBlueprintLoopTransactionModel(it.stateName, it.predicate, it.alias, it.text)
            }.toTypedArray()
        )
        return kotlinx.serialization.json.Json.encodeToString(SerializableBlueprintModel.serializer(), serializable)
    }

    fun fromJson(jsonInput: String): BlueprintModel {
        val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        }
        val deserialized = json.decodeFromString(
            SerializableBlueprintModel.serializer(),
            jsonInput
        )
        return BlueprintModel(
            main = BlueprintStateModel(
                deserialized.main.canvasPositionX,
                deserialized.main.canvasPositionY,
                deserialized.main.name,
                deserialized.main.text
            ),
            init = BlueprintStateModel(
                deserialized.init.canvasPositionX,
                deserialized.init.canvasPositionY,
                deserialized.init.name,
                deserialized.init.text
            ),
            states = deserialized.states.map {
                BlueprintStateModel(it.canvasPositionX, it.canvasPositionY, it.name, it.text)
            }.toTypedArray(),
            transactions = deserialized.transactions.map {
                BlueprintTransactionModel(it.startStateName, it.endStateName, it.predicate, it.alias)
            }.toTypedArray(),
            loopTransactions = deserialized.loopTransactions.map {
                BlueprintLoopTransactionModel(it.stateName, it.predicate, it.alias, it.text)
            }.toTypedArray()
        )
    }
}
