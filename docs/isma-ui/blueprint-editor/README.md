# Blueprint Editor — Documentation Index

## Purpose

The Blueprint Editor is a visual finite-state machine editor within ISMA. Users create states as draggable boxes on an infinite canvas, draw transitions between them, and define transition predicates (conditions). The visual statechart is compiled into LISMA text at build time via `convertToLisma()`.

## Module Structure

```
blueprint-editor/src/main/kotlin/ru/isma/next/editor/blueprint/
├── IsmaBlueprintEditor.kt          # UI only (113 lines)
├── IsmaBlueprintViewModel.kt       # All logic (421 lines)
├── EditorMode.kt                   # Sealed class for editor modes (12 lines)
├── NameChangingMonitor.kt          # Unique name enforcement (33 lines)
├── utilities/
│   ├── ClickDisambiguator.kt       # 200ms single/double click disambiguation
│   └── JavaFxExtensions.kt         # JavaFX utility extensions
├── constants/
│   ├── BlueprintEditorConstants.kt # All magic numbers (35 lines)
│   └── StateNames.kt               # MAIN_STATE, INIT_STATE constants
├── services/
│   └── ITextEditorFactory.kt       # SPI for text editor creation
├── models/
│   ├── BlueprintModel.kt           # Serializable JSON model
│   ├── BlueprintStateModel.kt      # State data model
│   ├── BlueprintTransactionModel.kt # Inter-state transition model
│   ├── BlueprintLoopTransactionModel.kt # Loop transition model
│   └── CanvasViewModel.kt          # Observable lists + EditorTransaction/EditorLoopTransaction
├── controls/
│   ├── StateBox.kt                 # Draggable state box control
│   ├── TransactionArrow.kt         # Inter-state transition arrow
│   ├── LoopTransactionArrow.kt     # Self-loop arrow
│   ├── EditArrowPopOver.kt         # Floating edit popover
│   └── CoroutineScopeProvider.kt   # Shared CoroutineScope(Dispatchers.JavaFx)
├── utilities/
│   └── ArrowGeometry.kt            # Arrow geometry calculations
└── views/                          # (new, exported in module-info)
```

## MVVM Pattern

| Role | Class | Responsibility |
|------|-------|----------------|
| **View** | `IsmaBlueprintEditor` | UI only — BorderPane layout, no business logic |
| **ViewModel** | `IsmaBlueprintViewModel` | All logic — state management, canvas operations, mode handling |
| **Model** | `BlueprintModel` | Serializable JSON persistence model |
| **Model** | `CanvasViewModel` | Observable lists + editor-only transaction wrappers |

## Container Hierarchy

```
IsmaBlueprintEditor (BorderPane) — View
├── center: TabPane
│   └── Tab "Diagram" (non-closable)
│       └── ScrollPane
│           └── Pane (canvas)
│               ├── mainStateBox (fixed, from ViewModel)
│               ├── initStateBox (fixed, from ViewModel)
│               ├── userStateBox[] (from CanvasViewModel.states)
│               ├── transactionArrow[] (from CanvasViewModel.transactions)
│               ├── loopTransactionArrow[] (from CanvasViewModel.loopTransactions)
│               └── EditArrowPopOver (floating, transient)
└── bottom: ToolBar
    ├── "New state" button → viewModel.addState()
    ├── "New transition" / "Stop adding transaction" → viewModel.toggleAddTransition()
    ├── Separator
    ├── "Remove state" / "Stop remove state" → viewModel.toggleRemoveState()
    └── "Remove transition" / "Stop remove transition" → viewModel.toggleRemoveTransition()
```

The toolbar is bound to the Diagram tab's visibility: `visibleProperty().bind(visible)` and `managedProperty().bind(visible)`. When the Diagram tab is not active, the toolbar is invisible and unmanaged.

## Data Model Summary

### BlueprintModel

```
BlueprintModel
├── main: BlueprintStateModel
├── init: BlueprintStateModel
├── states: Array<BlueprintStateModel>
├── transactions: Array<BlueprintTransactionModel>
└── loopTransactions: Array<BlueprintLoopTransactionModel>
```

### BlueprintStateModel

```
BlueprintStateModel
├── canvasPositionX: Double
├── canvasPositionY: Double
├── name: String
└── text: String
```

### BlueprintTransactionModel

```
BlueprintTransactionModel
├── startStateName: String
├── endStateName: String
├── predicate: String
└── alias: String = ""
```

### BlueprintLoopTransactionModel

```
BlueprintLoopTransactionModel
├── stateName: String
├── predicate: String
├── alias: String = ""
└── text: String
```

## Constants

All magic numbers are centralized in `BlueprintEditorConstants.kt`. See `03-ux-spec.md` for the full dimensions reference table.

## Text Editor Factory SPI

`ITextEditorFactory` is an interface injected via Koin:

```kotlin
interface ITextEditorFactory {
    fun createTextEditor(text: String, onTextChanged: (String) -> Unit): Node
    fun disposeInstance(node: Node)
}
```

The implementation (in app module) wraps `IsmaTextEditor` instances and provides per-project Koin scopes. Each blueprint project gets its own factory and editor pool.

## Index

| File | Content |
|------|---------|
| `01-architecture.md` | Canvas properties, rendering order, coordinate system, data flow (save/load), project lifecycle, editor-only data classes, text editor integration |
| `02-algorithms.md` | NameChangingMonitor, ArrowGeometry, ClickDisambiguator, LISMA conversion, editability bindings |
| `03-ux-spec.md` | State boxes, transition arrows, loop arrows, PopOver, toolbar, interaction modes, limitations, color palette, dimensions |
