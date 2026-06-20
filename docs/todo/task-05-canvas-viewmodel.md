# Task: Decouple Editor Models from JavaFX Nodes

## Problem

`BlueprintEditorTransactionModel` and `BlueprintEditorLoopTransactionModel` embed direct references to JavaFX nodes:

```kotlin
data class BlueprintEditorTransactionModel(
    val startStateBox: StateBox,          // JavaFX node
    val endStateBox: StateBox,            // JavaFX node
    val transactionArrow: TransactionArrow // JavaFX node
)
```

This couples the editor's data layer to the UI toolkit, making:
- Testing impossible without JavaFX runtime
- The data classes impossible to serialize or version
- Separation of concerns unclear

Similar coupling exists in `IsmaBlueprintEditor.kt` where `ArrayList<BlueprintEditorTransactionModel>` and `ArrayList<StateBox>` are managed imperatively with scattered `canvas.children.add/remove` calls.

## Requirements

1. Create a `CanvasViewModel` that owns all canvas state (states, transactions, loop transactions)
2. The ViewModel must provide CRUD operations that cascade (e.g., removing a state removes its transactions)
3. `BlueprintEditorTransactionModel` and `BlueprintEditorLoopTransactionModel` must be removed or converted to thin data classes without node references
4. `IsmaBlueprintEditor.kt` must use `CanvasViewModel` instead of managing lists and canvas children directly
5. No behavioral changes — all existing interactions must work identically

## Implementation

### Create `models/CanvasViewModel.kt`

```kotlin
package ru.isma.next.editor.blueprint.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.isma.next.editor.blueprint.controls.*

class CanvasViewModel {
    data class EditorTransaction(
        val startBox: StateBox,
        val endBox: StateBox,
        val arrow: TransactionArrow
    )

    data class EditorLoopTransaction(
        val stateBox: StateBox,
        val arrow: LoopTransactionArrow
    )

    private val _states = FXCollections.observableArrayList<StateBox>()
    val states: ObservableList<StateBox> = _states

    private val _transactions = FXCollections.observableArrayList<EditorTransaction>()
    val transactions: ObservableList<EditorTransaction> = _transactions

    private val _loopTransactions = FXCollections.observableArrayList<EditorLoopTransaction>()
    val loopTransactions: ObservableList<EditorLoopTransaction> = _loopTransactions

    fun addState(box: StateBox) {
        _states.add(box)
    }

    fun removeState(box: StateBox) {
        _states.remove(box)
        _transactions.removeAll { it.startBox == box || it.endBox == box }
        _loopTransactions.removeAll { it.stateBox == box }
    }

    fun addTransaction(tx: EditorTransaction) {
        _transactions.add(tx)
    }

    fun removeTransaction(arrow: TransactionArrow) {
        _transactions.removeAll { it.arrow == arrow }
    }

    fun addLoopTransaction(loop: EditorLoopTransaction) {
        _loopTransactions.add(loop)
    }

    fun removeLoopTransaction(arrow: LoopTransactionArrow) {
        _loopTransactions.removeAll { it.arrow == arrow }
    }

    fun clearAll() {
        _states.clear()
        _transactions.clear()
        _loopTransactions.clear()
    }
}
```

### Refactor `IsmaBlueprintEditor.kt`

**Add field:**
```kotlin
private val canvasViewModel = CanvasViewModel()
```

**Replace `transactions` list:**
```kotlin
// Delete: private val transactions = ArrayList<BlueprintEditorTransactionModel>()
// Delete: private val loopTransactions = ArrayList<BlueprintEditorLoopTransactionModel>()
// Delete: private val stateBoxes = ArrayList<StateBox>()
```

**Replace all `stateBoxes.add(box)` with:**
```kotlin
canvasViewModel.states.add(box)
```

**Replace all `stateBoxes.remove(box)` with:**
```kotlin
canvasViewModel.removeState(box)
```

**Replace all `transactions.add(...)` with:**
```kotlin
canvasViewModel.addTransaction(CanvasViewModel.EditorTransaction(...))
```

**Replace all `loopTransactions.add(...)` with:**
```kotlin
canvasViewModel.addLoopTransaction(CanvasViewModel.EditorLoopTransaction(...))
```

**Update `removeFromEditor` extension functions:**

Delete the three separate extensions. Replace with calls to `canvasViewModel`:

```kotlin
// StateBox.removeFromEditor() body becomes:
canvasViewModel.removeState(this)
canvas.children.remove(this)

// TransactionArrow.removeFromEditor() body becomes:
canvasViewModel.removeTransaction(this)
canvas.children.remove(this)

// LoopTransactionArrow.removeFromEditor() body becomes:
canvasViewModel.removeLoopTransaction(this)
canvas.children.remove(this)
```

**Update `removeTransaction(transaction: BlueprintEditorTransactionModel)`:**

Delete this function. Replace all callers with:
```kotlin
canvasViewModel.removeTransaction(transaction.transactionArrow)
canvas.children.remove(transaction.transactionArrow)
```

**Update `removeTransaction(transaction: BlueprintEditorLoopTransactionModel)`:**

Delete this function. Replace all callers with:
```kotlin
canvasViewModel.removeLoopTransaction(transaction.loopTransactionArrow)
canvas.children.remove(transaction.loopTransactionArrow)
```

**Update `getBlueprintModel()`:**

```kotlin
fun getBlueprintModel(): BlueprintModel {
    val main = mainStateBox.toBlueprintState()
    val init = initStateBox.toBlueprintState()
    val states = canvasViewModel.states.map { it.toBlueprintState() }.toTypedArray()
    val blueprintTransactions = canvasViewModel.transactions.map { it.toBlueprintTransaction() }.toTypedArray()
    val blueprintLoopTransactions = canvasViewModel.loopTransactions.map { it.toBlueprintLoopTransaction() }.toTypedArray()

    return BlueprintModel(main, init, states, blueprintTransactions, blueprintLoopTransactions)
}
```

**Update `setBlueprintModel()`:**

```kotlin
fun setBlueprintModel(model: BlueprintModel) {
    canvasViewModel.states.toList().forEach { it.removeFromEditor() }

    mainStateBox.applyBlueprintState(model.main)
    initStateBox.applyBlueprintState(model.init)

    val stateMap = model.states.associateByTo(
        mutableMapOf(
            initStateBox.name to initStateBox,
            mainStateBox.name to mainStateBox
        ),
        { it.name },
        { instantiateStateBoxFromBlueprintState(it) }
    )

    // Update canvasViewModel states
    stateMap.values.forEach { canvasViewModel.addState(it) }

    model.transactions.forEach {
        addTransactionArrow(
            stateMap[it.startStateName]!!,
            stateMap[it.endStateName]!!,
            it.predicate,
            it.alias,
        )
    }

    model.loopTransactions.forEach { loopTransaction ->
        addLoopTransactionArrow(
            stateMap[loopTransaction.stateName]!!,
            loopTransaction.text,
            loopTransaction.predicate,
            loopTransaction.alias
        )
    }
}
```

**Update collection references in event handlers:**

All `transactions.toList().filter(...)` become `canvasViewModel.transactions.toList().filter(...)`.
All `loopTransactions.toList().filter(...)` become `canvasViewModel.loopTransactions.toList().filter(...)`.
All `stateBoxes.remove(this)` become `canvasViewModel.states.remove(this)`.

### Handle `BlueprintEditorTransactionModel` and `BlueprintEditorLoopTransactionModel`

These files can be **deleted** if nothing else references them. If `BlueprintProjectDataProvider` or other code references them, convert them to thin aliases:

```kotlin
// BlueprintEditorTransactionModel.kt — delete entirely
// BlueprintEditorLoopTransactionModel.kt — delete entirely
```

Check for references before deleting:
```bash
grep -r "BlueprintEditorTransactionModel" --include="*.kt"
grep -r "BlueprintEditorLoopTransactionModel" --include="*.kt"
```

Only the blueprint-editor module files reference these, so they can be safely removed.

## Acceptance Criteria

- [ ] `CanvasViewModel.kt` exists with `states`, `transactions`, `loopTransactions` collections and CRUD methods
- [ ] `CanvasViewModel.removeState()` cascades to remove associated transactions and loop transactions
- [ ] `BlueprintEditorTransactionModel.kt` is deleted (or confirmed as unused)
- [ ] `BlueprintEditorLoopTransactionModel.kt` is deleted (or confirmed as unused)
- [ ] `IsmaBlueprintEditor.kt` has no `ArrayList<BlueprintEditorTransactionModel>` or `ArrayList<BlueprintEditorLoopTransactionModel>`
- [ ] `IsmaBlueprintEditor.kt` has no `ArrayList<StateBox>` — uses `canvasViewModel.states` instead
- [ ] All `canvas.children.add/remove` calls still work correctly
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] All CRUD operations (add state, remove state, add transition, remove transition, add loop, remove loop) produce identical results
