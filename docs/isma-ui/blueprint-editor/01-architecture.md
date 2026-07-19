# Blueprint Editor — Architecture

## Canvas Properties

| Property | Value |
|----------|-------|
| Root type | `Pane` (no layout constraints) |
| Scrolling | Via `ScrollPane` wrapper |
| Scroll constraints | No min/max viewport size — scrolls to content |
| Coordinate origin | (0, 0) at top-left of canvas pane |
| Position clamping | All state positions: `max(position, 0.0)` — negative coordinates forbidden |

## Rendering Order (z-index via `viewOrder`)

Higher `viewOrder` renders on top.

| Element | viewOrder | Layer |
|---------|-----------|-------|
| Rectangle (state body) | 3.0 | Background fill |
| HBox (name label + text area) | 2.0 (default) | Foreground content inside state |
| StateBox group | 3.0 (default) | Container for state body + content |
| TransactionArrow / LoopTransactionArrow | 4.0 | Transitions (above states) |
| Line (arrow shaft) | 6.0 | Arrow geometry (topmost) |
| Polygon (arrowhead) | 6.0 | Arrow geometry (topmost) |

## Coordinate System

### Absolute Positioning

All elements use absolute `layoutX` and `layoutY` on the canvas `Pane`. There is no layout manager, no grid snapping, and no snapping to other elements.

### Arrow Geometry Bindings

| Arrow Type | Layout X Binding | Layout Y Binding |
|------------|-----------------|-----------------|
| TransactionArrow | `(endX - startX) / 2 + startX` | `(endY - startY) / 2 + startY` |
| LoopTransactionArrow | `stateBox.centerXProperty()` | `stateBox.centerYProperty()` |

### State Center Calculation

`centerX = layoutX + squareWidth / 2` (equals `layoutX + 55`) and `centerY = layoutY + squareHeight / 2` (equals `layoutY + 32.5` for user states, `layoutY + 30` for Main/Init). These are exposed as `DoubleBinding` objects via `StateBox.centerXProperty()` and `StateBox.centerYProperty()` that update automatically when `layoutX`, `layoutY`, `squareWidth`, or `squareHeight` changes.

## ViewAdapter Pattern

The `BlueprintViewAdapter` interface abstracts JavaFX-specific canvas operations, decoupling the ViewModel from concrete JavaFX node manipulation. This enables testability and allows future view implementations (e.g., headless or different UI toolkit).

### Interface

The `BlueprintViewAdapter` interface declares the following methods:

- `createStateBox(model, x, y, canvas, factory)` — Create a state box and add to canvas
- `addTransactionArrow(startBox, endBox, model, canvas, factory)` — Create a transaction arrow and add to canvas
- `addLoopTransactionArrow(stateBox, model, canvas, factory)` — Create a loop arrow and add to canvas
- `createTab(title, content)` — Create a Tab for text editors
- `addNodeToCanvas(canvas, node)` — Canvas node manipulation
- `removeNodeFromCanvas(canvas, node)` — Canvas node manipulation
- `clearCanvas(canvas)` — Canvas node manipulation

See `BlueprintViewAdapter.kt` for the full interface definition.

### JavaFX Implementation

`JavaFxBlueprintViewAdapter` implements `BlueprintViewAdapter` where all methods delegate to `canvas.children.add/remove/clear`. The `createStateBox`, `addTransactionArrow`, and `addLoopTransactionArrow` methods call the factory with the model to create the Node, add it to `canvas.children`, and return the node. See `JavaFxBlueprintViewAdapter.kt` for the full implementation.

### Factory Pattern Usage

The `factory` parameter in each adapter method is a lambda that creates the actual control. The ViewModel provides the factory with the model data and receives back a configured JavaFX `Node`. This keeps the ViewModel free of JavaFX import dependencies in the factory closures.

Example from `IsmaBlueprintViewModel.addTransactionArrow()`:

The factory lambda creates a `TransactionArrow` with `onClick` and `onArrowClick` callbacks, binds `startXProperty` to `sb.centerXProperty()`, `startYProperty` to `sb.centerYProperty()`, `endXProperty` to `eb.centerXProperty()`, `endYProperty` to `eb.centerYProperty()`, and sets `text` and `alias` from the model. See `IsmaBlueprintViewModel.kt` for the full implementation.

## Data Flow: Save (Canvas → Model → JSON)

```mermaid
sequenceDiagram
    participant App as App Module
    participant Editor as IsmaBlueprintEditor
    participant VM as IsmaBlueprintViewModel
    participant CVM as CanvasViewModel
    participant BM as BlueprintModel
    participant JSON as kotlinx.serialization
    participant File as .scisma file

    App->>Editor: getBlueprintModel()
    Editor->>VM: toBlueprintModel()
    VM->>VM: mainStateBox.toBlueprintState()
    VM->>VM: initStateBox.toBlueprintState()
    loop Each user state
        VM->>CVM: states list
        CVM-->>VM: StateBox → toBlueprintState()
    end
    loop Each transaction
        VM->>CVM: transactions list
        CVM-->>VM: EditorTransaction → toBlueprintTransaction()
    end
    loop Each loop transaction
        VM->>CVM: loopTransactions list
        CVM-->>VM: EditorLoopTransaction → toBlueprintLoopTransaction()
    end
    VM->>BM: Assemble BlueprintModel(main, init, states, transactions, loopTransactions)
    BM-->>Editor: BlueprintModel
    Editor-->>App: BlueprintModel
    App->>JSON: encodeToString(BM)
    JSON->>File: Write JSON
```

1. `getBlueprintModel()` is called on `IsmaBlueprintEditor`
2. `toBlueprintModel()` is called on `IsmaBlueprintViewModel`
3. `mainStateBox.toBlueprintState()` and `initStateBox.toBlueprintState()` convert the fixed states
4. Each `StateBox` in `CanvasViewModel.states` is converted via `toBlueprintState()`: extracts `layoutX`, `layoutY`, `name`, `text`
5. Each `EditorTransaction` in `CanvasViewModel.transactions` is converted via `toBlueprintTransaction()`: extracts state **names** (not references), predicate, alias
6. Each `EditorLoopTransaction` in `CanvasViewModel.loopTransactions` is converted via `toBlueprintLoopTransaction()`: extracts state name, predicate, alias, text
7. A `BlueprintModel` is assembled from main, init, states, transactions, and loopTransactions
8. The `BlueprintModel` is returned to the editor and then to the app module
9. `kotlinx.serialization` converts the model to JSON, which is written to a `.scisma` file

See `IsmaBlueprintViewModel.kt` for the full implementation.

## Data Flow: Load (JSON → Model → Canvas)

```mermaid
sequenceDiagram
    participant App as App Module
    participant File as .scisma file
    participant JSON as kotlinx.serialization
    participant Editor as IsmaBlueprintEditor
    participant VM as IsmaBlueprintViewModel
    participant CVM as CanvasViewModel
    participant Canvas as Canvas Pane

    App->>File: Read JSON
    File->>JSON: Parse JSON string
    JSON->>BM: BlueprintModel
    BM-->>App: BlueprintModel
    App->>Editor: setBlueprintModel(model)
    Editor->>VM: fromBlueprintModel(model)
    VM->>CVM: Remove all user states
    CVM->>Canvas: removeNodeFromCanvas()
    VM->>Canvas: clearCanvas()
    VM->>VM: applyBlueprintState(main)
    VM->>VM: applyBlueprintState(init)
    VM->>Canvas: addNodeToCanvas(mainBox)
    VM->>Canvas: addNodeToCanvas(initBox)
    loop Each model state
        VM->>VM: instantiateStateBoxFromBlueprintState()
        VM->>CVM: Add to states list
        VM->>Canvas: addNodeToCanvas(stateBox)
    end
    loop Each model transaction
        VM->>VM: Lookup start/end by name
        VM->>VM: Create TransactionArrow
        VM->>Canvas: addNodeToCanvas(arrow)
    end
    loop Each model loop transaction
        VM->>VM: Create LoopTransactionArrow
        VM->>Canvas: addNodeToCanvas(loopArrow)
    end
    VM->>VM: Bind all arrow geometry to state centers
```

1. `setBlueprintModel(model)` is called on `IsmaBlueprintEditor`
2. `fromBlueprintModel(model)` is called on `IsmaBlueprintViewModel`
3. All existing user state boxes are removed from `CanvasViewModel` (calling `removeState(it.model)` for each) and from the canvas pane (calling `removeNodeFromCanvas(canvas, it.node)`)
4. `clearCanvas()` removes all remaining children from the canvas `Pane`
5. Main and Init state data is applied onto the fixed state boxes via `applyBlueprintState()`
6. Main and Init boxes are added back to the canvas via `addNodeToCanvas()`
7. A name → StateBox map is built including Main, Init, and new states
8. New `StateBox` instances are created from `model.states` via `instantiateStateBoxFromBlueprintState`, added to `CanvasViewModel`, and added to the canvas
9. For each `BlueprintTransactionModel`, start/end states are looked up by name and a `TransactionArrow` is created
10. For each `BlueprintLoopTransactionModel`, a `LoopTransactionArrow` is created
11. All arrows bind their geometry to the state box centers

See `IsmaBlueprintViewModel.kt` for the full implementation.

## Editor-Only Data Classes (runtime, not serializable)

Source: `models/CanvasViewModel.kt`

`CanvasViewModel.EditorState` pairs `model` (BlueprintStateModel, serializable state data) with `node` (StateBox JavaFX node). `CanvasViewModel.EditorTransaction` pairs `startBox` (StateBox, direct reference to canvas node), `endBox` (StateBox), `arrow` (TransactionArrow), and `node` (Node). `CanvasViewModel.EditorLoopTransaction` pairs `stateBox` (StateBox), `arrow` (LoopTransactionArrow), and `node` (Node).

These classes pair serializable model data with live JavaFX nodes. They exist only at runtime and are never written to disk.

## CanvasViewModel Operations

### Cascade Removal

Removing a state automatically removes all associated transitions. The `removeState(model)` function removes from `_states` where `it.model == model`, removes from `_transactions` where `it.startBox.name == model.name || it.endBox.name == model.name`, and removes from `_loopTransactions` where `it.stateBox.name == model.name`. Matching is done by **state name** (not object reference), which handles the case where state boxes are recreated during load. See `CanvasViewModel.kt` for the full implementation.

### Observable Lists

All three lists (`states`, `transactions`, `loopTransactions`) are exposed as immutable `ObservableList` wrappers around private `FXCollections.observableArrayList`. External code can observe changes but cannot directly modify the lists — all mutations go through the provided CRUD methods.

## Text Editor Integration

### Opening State Text Editor Tabs

Double-clicking a state box triggers `openStateTextEditorTab(state)`:

1. `editorFactory.createTextEditor(state.text, onTextChanged = { state.text = it })` creates a text editor
2. `viewAdapter.createTab(state.name, editor)` creates a closable Tab
3. `Tab.textProperty()` binds to `state.nameProperty` — tab name updates when state is renamed
4. `Tab.setOnCloseRequest { editorFactory.disposeInstance(editor) }` disposes the editor on tab close

See `IsmaBlueprintViewModel.kt` for the full implementation.

### Opening Loop Content Editor Tabs

Double-clicking a loop arrow's arrowhead triggers `openLoopTextEditor(arrow, stateBox)`:

1. `editorFactory.createTextEditor(arrow.text, onTextChanged = { arrow.text = it })`
2. `viewAdapter.createTab("${stateBox.name} (loop)", editor)`
3. `Tab.textProperty()` binds to `stateBox.nameProperty.concat(" (loop)")`
4. Tab close disposes the editor instance

### Editor Lifecycle

The editor lifecycle is: Create → Tab opened → onTextChanged fires on edits → Tab closed → disposeInstance(). Each open tab owns one editor instance. Closing the tab releases the editor back to the factory's pool.

## Project Lifecycle

### Blueprint Project Creation

1. User clicks "New statechart" toolbar button or uses File → New Statechart (Ctrl+B)
2. `ProjectService.createNewBlueprint("New statechart")` is called
3. A new `BlueprintProjectModel` is created with `BlueprintModel.empty`
4. A Koin scope is created for this project
5. The `IsmaBlueprintEditor` is instantiated within the scope
6. The project is added to `ProjectService.projects`
7. A new tab opens in the main TabPane showing the editor

### Blueprint Project Save

1. User clicks Save (Ctrl+S) or Save All
2. `project.blueprint` triggers `fetchBlueprint()` → `dataProvider.blueprint` → `blueprintEditor.getBlueprintModel()`
3. The `BlueprintModel` is serialized to JSON via `kotlinx.serialization`
4. JSON is written to the `.scisma` file

### Blueprint Project Load

1. User opens a `.scisma` file (File → Open, filter `*.scisma`)
2. JSON is parsed into `BlueprintModel`
3. `project.blueprint = model` triggers `pushBlueprint()` → `dataProvider.blueprint = model` → `blueprintEditor.setBlueprintModel(model)`
4. `setBlueprintModel` rebuilds the canvas from the model

### Snapshot (Compile)

1. At compile/verification time, `project.snapshot()` is called
2. For blueprint projects, this calls `blueprint.toLismaText()`
3. The result is a `LismaTextModel` containing the generated LISMA text and `CodeRegion` mappings
4. `CodeRegion` objects provide line number ranges for error highlighting in the text editor

## Integration with Main App Shell

### Tab Display

The `IsmaBlueprintEditor` is injected as `Node` with qualifier `IsmaEditorQualifier` into the project model. The main app's `ProjectTab` wraps this node in a `Tab` with `text = project.nameProperty`, `content = project.editor` (IsmaBlueprintEditor instance), and `closable = true`.

### Tab Rename

When a state name changes, the tab text updates automatically because:
- The tab's text is bound to the project's name (set at project creation time)
- State name changes do NOT affect the tab title — they only affect the state box label
- The text editor tab (opened by double-clicking a state) DOES update its title via `textProperty().bind(state.nameProperty)`

### Multiple Project Types Coexistence

The main TabPane can contain tabs from both LISMA text projects and Blueprint projects. The TabPane has no type restriction.
