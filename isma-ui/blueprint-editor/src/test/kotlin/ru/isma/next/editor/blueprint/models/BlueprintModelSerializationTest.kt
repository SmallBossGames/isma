package ru.isma.next.editor.blueprint.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.isma.next.editor.blueprint.constants.INIT_STATE
import ru.isma.next.editor.blueprint.constants.MAIN_STATE

class BlueprintModelSerializationTest {
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }

    @Test
    fun `empty model serializes and deserializes`() {
        val model = BlueprintModel.empty
        val serialized = json.encodeToString(BlueprintModel.serializer(), model)
        val deserialized = json.decodeFromString(BlueprintModel.serializer(), serialized)

        assertEquals(model.main.name, deserialized.main.name)
        assertEquals(model.init.name, deserialized.init.name)
        assertEquals(model.states.size, deserialized.states.size)
        assertEquals(model.transactions.size, deserialized.transactions.size)
        assertEquals(model.loopTransactions.size, deserialized.loopTransactions.size)
    }

    @Test
    fun `model with states serializes correctly`() {
        val model = BlueprintModel(
            main = BlueprintStateModel(10.0, 10.0, MAIN_STATE, "main body"),
            init = BlueprintStateModel(10.0, 100.0, INIT_STATE, "init body"),
            states = arrayOf(
                BlueprintStateModel(50.0, 200.0, "State A", "body A"),
                BlueprintStateModel(200.0, 150.0, "State B", "body B")
            ),
            transactions = arrayOf(
                BlueprintTransactionModel("State A", "State B", "condition X", "alias X")
            ),
            loopTransactions = arrayOf(
                BlueprintLoopTransactionModel("State A", "loop condition", "loop alias", "loop body")
            )
        )

        val serialized = json.encodeToString(BlueprintModel.serializer(), model)
        val deserialized = json.decodeFromString(BlueprintModel.serializer(), serialized)

        assertEquals(2, deserialized.states.size)
        assertEquals("State A", deserialized.states[0].name)
        assertEquals("body A", deserialized.states[0].text)
        assertEquals(50.0, deserialized.states[0].canvasPositionX, 0.001)

        assertEquals(1, deserialized.transactions.size)
        assertEquals("State A", deserialized.transactions[0].startStateName)
        assertEquals("State B", deserialized.transactions[0].endStateName)
        assertEquals("condition X", deserialized.transactions[0].predicate)
        assertEquals("alias X", deserialized.transactions[0].alias)

        assertEquals(1, deserialized.loopTransactions.size)
        assertEquals("State A", deserialized.loopTransactions[0].stateName)
        assertEquals("loop condition", deserialized.loopTransactions[0].predicate)
        assertEquals("loop body", deserialized.loopTransactions[0].text)
    }

    @Test
    fun `position coordinates are preserved with decimals`() {
        val model = BlueprintModel(
            main = BlueprintStateModel(10.5, 20.7, MAIN_STATE, ""),
            init = BlueprintStateModel(30.3, 40.1, INIT_STATE, ""),
            states = emptyArray(),
            transactions = emptyArray(),
            loopTransactions = emptyArray()
        )

        val serialized = json.encodeToString(BlueprintModel.serializer(), model)
        val deserialized = json.decodeFromString(BlueprintModel.serializer(), serialized)

        assertEquals(10.5, deserialized.main.canvasPositionX, 0.0001)
        assertEquals(20.7, deserialized.main.canvasPositionY, 0.0001)
        assertEquals(30.3, deserialized.init.canvasPositionX, 0.0001)
        assertEquals(40.1, deserialized.init.canvasPositionY, 0.0001)
    }

    @Test
    fun `empty arrays default correctly`() {
        val jsonStr = """
            {
                "main": {"canvasPositionX": 10.0, "canvasPositionY": 10.0, "name": "Main", "text": ""},
                "init": {"canvasPositionX": 10.0, "canvasPositionY": 100.0, "name": "init", "text": ""},
                "states": [],
                "transactions": [],
                "loopTransactions": []
            }
        """.trimIndent()

        val deserialized = json.decodeFromString(BlueprintModel.serializer(), jsonStr)
        assertTrue(deserialized.loopTransactions.isEmpty())
    }
}
