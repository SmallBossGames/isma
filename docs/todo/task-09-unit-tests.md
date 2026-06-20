# Task: Add Unit Tests for Blueprint Editor

## Problem

The entire `blueprint-editor` module has **zero tests**. This means:
- No safety net for refactoring
- No verification of core logic (name uniqueness, geometry math, mode transitions)
- Serialization contracts are untested
- Any change risks silent regressions

## Requirements

1. Add unit tests for all testable pure logic
2. Tests must not require a JavaFX headless environment — avoid UI controls
3. Use JUnit 5 (already available via the kotlin-jvm plugin)
4. Tests must be deterministic and fast (< 1 second total)
5. Add test dependencies to `build.gradle.kts`

## Test Targets

### 1. NameChangingMonitorTest

Tests the `NameChangingMonitor` class — the most testable piece of logic.

**Create:** `src/test/kotlin/ru/isma/next/editor/blueprint/NameChangingMonitorTest.kt`

```kotlin
package ru.isma.next.editor.blueprint

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NameChangingMonitorTest {
    private lateinit var monitor: NameChangingMonitor

    @BeforeEach
    fun setUp() {
        monitor = NameChangingMonitor("New state")
    }

    @Test
    fun `first registration succeeds`() {
        assertTrue(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unique name registration succeeds`() {
        assertTrue(monitor.tryRegister("MyState"))
    }

    @Test
    fun `duplicate registration fails`() {
        monitor.tryRegister("New state 1")
        assertFalse(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unregister allows re-registration`() {
        monitor.tryRegister("New state 1")
        assertTrue(monitor.tryUnregister("New state 1"))
        assertTrue(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unregister unknown name fails`() {
        assertFalse(monitor.tryUnregister("NonExistent"))
    }

    @Test
    fun `counter increments on high-numbered name`() {
        monitor.tryRegister("New state 5")
        assertEquals("New state 6", monitor.createNextDefaultName())
    }

    @Test
    fun `counter does not decrease on lower name`() {
        monitor.tryRegister("New state 7")
        monitor.tryRegister("New state 3")
        assertEquals("New state 8", monitor.createNextDefaultName())
    }

    @Test
    fun `non-default names do not affect counter`() {
        monitor.tryRegister("MyState")
        assertEquals("New state 1", monitor.createNextDefaultName())
    }

    @Test
    fun `counter persists across unregister of high number`() {
        monitor.tryRegister("New state 10")
        monitor.tryUnregister("New state 10")
        assertEquals("New state 11", monitor.createNextDefaultName())
    }

    @Test
    fun `default name regex matches only correct format`() {
        monitor.tryRegister("New state 42")
        assertEquals("New state 43", monitor.createNextDefaultName())

        // These should NOT match the regex and should NOT affect counter
        monitor.tryRegister("New state")
        monitor.tryRegister("New state abc")
        monitor.tryRegister("Newstate 5")
        assertEquals("New state 43", monitor.createNextDefaultName())
    }

    @Test
    fun `multiple states can coexist`() {
        assertTrue(monitor.tryRegister("State A"))
        assertTrue(monitor.tryRegister("State B"))
        assertTrue(monitor.tryRegister("State C"))
        assertEquals("New state 1", monitor.createNextDefaultName())
    }

    @Test
    fun `counter recovers from gap`() {
        monitor.tryRegister("New state 1")
        monitor.tryRegister("New state 3")
        monitor.tryUnregister("New state 1")
        // Counter should be 4 (max of registered + 1)
        assertEquals("New state 4", monitor.createNextDefaultName())
    }
}
```

### 2. ArrowGeometryTest

Tests the extracted geometry calculation function.

**Create:** `src/test/kotlin/ru/isma/next/editor/blueprint/utilities/ArrowGeometryTest.kt`

(See Task 4 implementation for full test code)

### 3. EditorModeTest

Tests the sealed class mode hierarchy and helper logic.

**Create:** `src/test/kotlin/ru/isma/next/editor/blueprint/EditorModeTest.kt`

```kotlin
package ru.isma.next.editor.blueprint

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EditorModeTest {

    @Test
    fun `Idle mode is not editing mode`() {
        val mode: EditorMode = EditorMode.Idle
        assertFalse(mode is EditorMode.RemoveState)
        assertFalse(mode is EditorMode.AddTransition)
    }

    @Test
    fun `RemoveState mode is correctly identified`() {
        val mode: EditorMode = EditorMode.RemoveState
        assertTrue(mode is EditorMode.RemoveState)
        assertFalse(mode is EditorMode.AddTransition)
    }

    @Test
    fun `AddTransition mode carries selected states`() {
        val mode = EditorMode.AddTransition(mutableListOf())
        assertTrue(mode is EditorMode.AddTransition)
        assertEquals(0, mode.selectedStates.size)
    }

    @Test
    fun `AddTransition mode can hold states`() {
        val mode = EditorMode.AddTransition(mutableListOf())
        mode.selectedStates.add("state1") // would be StateBox in real code
        mode.selectedStates.add("state2")
        assertEquals(2, mode.selectedStates.size)
    }

    @Test
    fun `RemoveTransition mode is correctly identified`() {
        val mode: EditorMode = EditorMode.RemoveTransition
        assertTrue(mode is EditorMode.RemoveTransition)
    }

    @Test
    fun `modes are mutually exclusive by type`() {
        val modes: List<EditorMode> = listOf(
            EditorMode.Idle,
            EditorMode.AddTransition(mutableListOf()),
            EditorMode.RemoveState,
            EditorMode.RemoveTransition
        )

        assertEquals(4, modes.distinct().size)
    }
}
```

### 4. BlueprintModelSerializationTest

Tests that `BlueprintModel` serializes and deserializes correctly via kotlinx.serialization.

**Create:** `src/test/kotlin/ru/isma/next/editor/blueprint/models/BlueprintModelSerializationTest.kt`

```kotlin
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
                "transactions": []
            }
        """.trimIndent()

        val deserialized = json.decodeFromString(BlueprintModel.serializer(), jsonStr)
        assertEquals(emptyArray<BlueprintLoopTransactionModel>(), deserialized.loopTransactions)
    }
}
```

### 5. CanvasViewModelTest

Tests the CanvasViewModel CRUD operations and cascade behavior.

**Create:** `src/test/kotlin/ru/isma/next/editor/blueprint/models/CanvasViewModelTest.kt`

This test requires JavaFX runtime for `ObservableList`. It can run in a headless environment.

```kotlin
package ru.isma.next.editor.blueprint.models

import javafx.application.Application
import javafx.embed.swing.JFXPanel
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow
import ru.isma.next.editor.blueprint.controls.LoopTransactionArrow
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CanvasViewModelTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun initJavaFX() {
            // Initialize JavaFX toolkit for tests
            Application.launch(CanvasViewModelTest.FakeFXApp::class.java) {}
        }

        class FakeFXApp : Application() {
            override fun start(stage: javafx.stage.Stage) {
                // No-op, just initializes the toolkit
                javafx.embed.swing.JFXPanel()
                Platform.exit()
            }
        }
    }

    private lateinit var vm: CanvasViewModel

    @BeforeEach
    fun setUp() {
        vm = CanvasViewModel()
    }

    @Test
    fun `add state increases count`() {
        val state = createState("A")
        vm.addState(state)
        assertEquals(1, vm.states.size)
    }

    @Test
    fun `remove state cascades to transactions`() {
        val start = createState("Start")
        val end = createState("End")
        val arrow = TransactionArrow({}, {})
        vm.addState(start)
        vm.addState(end)
        vm.addTransaction(CanvasViewModel.EditorTransaction(start, end, arrow))

        vm.removeState(start)

        assertEquals(0, vm.states.size)
        assertEquals(0, vm.transactions.size)
    }

    @Test
    fun `remove state cascades to loop transactions`() {
        val state = createState("LoopState")
        val loopArrow = LoopTransactionArrow({}, {}, {}, "", "")
        vm.addState(state)
        vm.addLoopTransaction(CanvasViewModel.EditorLoopTransaction(state, loopArrow))

        vm.removeState(state)

        assertEquals(0, vm.states.size)
        assertEquals(0, vm.loopTransactions.size)
    }

    @Test
    fun `remove transaction by arrow removes from collection`() {
        val start = createState("Start")
        val end = createState("End")
        val arrow = TransactionArrow({}, {})
        vm.addState(start)
        vm.addState(end)
        vm.addTransaction(CanvasViewModel.EditorTransaction(start, end, arrow))

        vm.removeTransaction(arrow)

        assertEquals(0, vm.transactions.size)
    }

    @Test
    fun `remove state removes transaction from both start and end`() {
        val start = createState("Start")
        val end = createState("End")
        val arrow = TransactionArrow({}, {})
        vm.addState(start)
        vm.addState(end)
        vm.addTransaction(CanvasViewModel.EditorTransaction(start, end, arrow))

        vm.removeState(end)

        assertEquals(0, vm.transactions.size)
    }

    @Test
    fun `clear all removes everything`() {
        val state = createState("A")
        val arrow = TransactionArrow({}, {})
        vm.addState(state)
        vm.addTransaction(CanvasViewModel.EditorTransaction(state, state, arrow))

        vm.clearAll()

        assertEquals(0, vm.states.size)
        assertEquals(0, vm.transactions.size)
        assertEquals(0, vm.loopTransactions.size)
    }

    private fun createState(name: String): StateBox {
        return StateBox().apply { this.name = name }
    }
}
```

**Note:** The CanvasViewModelTest requires JavaFX initialization. If JUnit 5 + JavaFX integration is complex, this test can be deferred or moved to an integration test module.

### Update `build.gradle.kts`

```kotlin
dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    testImplementation(libs.kotlin.test)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    // JavaFX needs headless mode
    jvmArgs("-Djava.awt.headless=true")
}
```

## Acceptance Criteria

- [ ] `NameChangingMonitorTest.kt` exists with at least 8 test methods
- [ ] `ArrowGeometryTest.kt` exists with at least 5 test methods (if Task 4 completed)
- [ ] `EditorModeTest.kt` exists with at least 5 test methods (if Task 2 completed)
- [ ] `BlueprintModelSerializationTest.kt` exists with at least 4 test methods
- [ ] `CanvasViewModelTest.kt` exists with at least 5 test methods (if Task 5 completed)
- [ ] `build.gradle.kts` includes test dependencies and `useJUnitPlatform()`
- [ ] `./gradlew :isma-ui:blueprint-editor:test` passes all tests
- [ ] Total test execution time is under 5 seconds
- [ ] No tests require a display (headless-compatible)
