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

| Element | viewOrder | Layer |
|---------|-----------|-------|
| Rectangle (state body) | 3.0 | Background |
| HBox (name label + text area) | 2.0 (default) | Foreground content |
| StateBox group | 3.0 (default) | Above rectangle |
| TransactionArrow / LoopTransactionArrow | 4.0 | Transitions |
| Line (arrow shaft) | 6.0 | Arrow geometry |
| Polygon (arrowhead) | 6.0 | Arrow geometry |

Higher `viewOrder` renders on top.

## Coordinate System

### Absolute Positioning

All elements use absolute `layoutX` and `layoutY` on the canvas `Pane`. There is no layout manager, no grid snapping, and no snapping to other elements.

### Arrow Geometry Bindings

| Arrow Type | Layout X Binding | Layout Y Binding |
|------------|-----------------|-----------------|
| TransactionArrow | `(endX - startX) / 2 + startX` | `(endY - startY) / 2 + startY` |
| LoopTransactionArrow | `stateBox.centerXProperty()` | `stateBox.centerYProperty()` |

### State Center Calculation

```
centerX = layoutX + squareWidth / 2   (= layoutX + 55)
centerY = layoutY + squareHeight / 2  (= layoutY + 32.5 for user, 30 for Main/Init)
```

These are exposed as `DoubleBinding` objects that update automatically when `layoutX`, `layoutY`, `squareWidth`, or `squareHeight` changes.

## Data Flow: Save (Canvas → Model → JSON)

1. `getBlueprintModel()` is called on `IsmaBlueprintEditor`
2. Each `StateBox` is converted via `toBlueprintState()`: extracts `layoutX`, `layoutY`, `name`, `text`
3. Each `BlueprintEditorTransactionModel` is converted via `toBlueprintTransaction()`: extracts state **names** (not references), predicate, alias
4. Each `BlueprintEditorLoopTransactionModel` is converted via `toBlueprintLoopTransaction()`: extracts state name, predicate, alias, text
5. A `BlueprintModel` is assembled and returned
6. `BlueprintProjectModel.snapshot()` calls `convertToLisma()` on the model at compile time

## Data Flow: Load (JSON → Model → Canvas)

1. `setBlueprintModel(model)` is called
2. All existing user state boxes are removed from the canvas
3. Main and Init state data is applied onto the fixed state boxes
4. New `StateBox` instances are created from `model.states` via `instantiateStateBoxFromBlueprintState`
5. A name→StateBox map is built including Main, Init, and new states
6. For each `BlueprintTransactionModel`, the start/end states are looked up by name and a `TransactionArrow` is created with the stored predicate and alias
7. For each `BlueprintLoopTransactionModel`, a `LoopTransactionArrow` is created
8. All arrows bind their geometry to the state box centers

## Editor-Only Data Classes (runtime, not serializable)

Source: `models/CanvasViewModel.kt`

```
CanvasViewModel.EditorTransaction
├── startBox: StateBox          // Direct reference to canvas node
├── endBox: StateBox
└── arrow: TransactionArrow

CanvasViewModel.EditorLoopTransaction
├── stateBox: StateBox
└── arrow: LoopTransactionArrow
```

The old separate files `BlueprintEditorTransactionModel.kt` and `BlueprintEditorLoopTransactionModel.kt` no longer exist.

## Text Editor Integration

### Opening State Text Editor Tabs

Double-clicking a state box triggers `openStateTextEditorTab(state)`:

1. `editorFactory.createTextEditor(state.text, onTextChanged = { state.text = it })`
2. `tabs.tabs.add(Tab(state.name, editor))`
3. `Tab.textProperty()` binds to `state.nameProperty`
4. `Tab.setOnCloseRequest { editorFactory.disposeInstance(editor) }`

### Opening Loop Content Editor Tabs

Double-clicking a loop arrow's arrowhead triggers `openStateTextEditorTab(arrow, stateBox)`:

1. `editorFactory.createTextEditor(arrow.text, onTextChanged = { arrow.text = it })`
2. `tabs.tabs.add(Tab("${stateBox.name} (loop)", editor))`
3. `Tab.textProperty()` binds to `stateBox.nameProperty.concat(" (loop)")`
4. `Tab.setOnCloseRequest { editorFactory.disposeInstance(editor) }`

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
2. For blueprint projects, this calls `blueprint.convertToLisma()`
3. The result is a `LismaTextModel` containing the generated LISMA text and `CodeRegion` mappings

## Integration with Main App Shell

### Tab Display

The `IsmaBlueprintEditor` is injected as `Node` with qualifier `IsmaEditorQualifier` into the project model. The main app's `ProjectTab` wraps this node in a `Tab`:

```
Tab {
    text = project.nameProperty
    content = project.editor (IsmaBlueprintEditor instance)
    closable = true
}
```

### Tab Rename

When a state name changes, the tab text updates automatically because:
- The tab's text is bound to the project's name (set at project creation time)
- State name changes do NOT affect the tab title — they only affect the state box label

### Multiple Project Types Coexistence

The main TabPane can contain tabs from both LISMA text projects and Blueprint projects. The TabPane has no type restriction.
