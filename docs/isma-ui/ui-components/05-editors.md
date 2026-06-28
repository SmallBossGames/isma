# Editors

## Text Editor Module

### Architecture

The text editor module provides a rich text editing component with server-driven syntax highlighting and clipboard event propagation. It is a reusable JavaFX control injected into project models.

### IsmaTextEditor

**File:** [`IsmaTextEditor.kt`](../../text-editor/src/main/kotlin/.../IsmaTextEditor.kt)

`BorderPane` wrapping `fxmisc.richtext.CodeArea` with:
- Consolas 12pt font
- Line numbers via `LineNumberFactory`
- Remote syntax highlighting via `IHighlightingService` (delegates to server's `highlightSource()`)
- Cut/copy/paste event propagation from `IEditorPlatformService`

Constructor takes `textEditorService: IEditorPlatformService` and `highlightingService: IHighlightingService`. In `init`: creates `CodeArea` with CSS font style, subscribes to `cutEvent`/`copyEvent`/`pasteEvent` flows (propagates only if focused), adds `textProperty()` listener that calls `highlightingService.createHighlightingStyleSpans()` and applies via `setStyleSpans(0, highlighting)`.

### Syntax Highlighting Pipeline

```mermaid
flowchart LR
    User["User types in CodeArea"] --> TextProp["textProperty() change"]
    TextProp --> HighlightingSvc["IHighlightingService.createHighlightingStyleSpans()"]
    HighlightingSvc --> Facade["SimulationServerFacade.highlightSource()"]
    Facade --> Tokens["SyntaxTokenDto[]"]
    Tokens --> HighlightingSvc
    HighlightingSvc --> StyleSpans["StyleSpans<Collection<String>>"]
    StyleSpans --> CSS["CSS classes: syntax-keyword, syntax-comment,\nsyntax-decimal, syntax-default"]
    CodeArea["CodeArea.setStyleSpans(0, StyleSpans)"]
    StyleSpans --> CodeArea
```

CSS classes applied: `syntax-keyword`, `syntax-comment`, `syntax-decimal`, `syntax-default`.

### Clipboard Propagation

```mermaid
sequenceDiagram
    participant Menu as IsmaToolBar / IsmaMenuBar
    participant Service as EditorPlatformService
    participant Flow as MutableSharedFlow<Unit>
    participant Editor as IsmaTextEditor (CodeArea)

    Menu->>Service: cut() / copy() / paste()
    Service->>Flow: emit(Unit)
    Flow->>Editor: collect { if (isFocused) cut() }
    Editor->>Editor: Platform cut/copy/paste
```

**File:** [`EditorPlatformService.kt`](../../text-editor/src/main/kotlin/.../services/EditorPlatformService.kt)

Coroutine-based service that propagates cut/copy/paste events via `MutableSharedFlow<Unit>`. Constructor creates a `CoroutineScope(Dispatchers.Default)`. Three internal `MutableSharedFlow<Unit>` instances exposed as read-only `SharedFlow`. Methods `cut()`, `copy()`, `paste()` each launch a coroutine to emit to their respective flow.

### Contracts

**File:** [`IEditorPlatformService.kt`](../../text-editor/src/main/kotlin/.../services/contracts/IEditorPlatformService.kt)

Interface declares: `cutEvent: Flow<Unit>`, `copyEvent: Flow<Unit>`, `pasteEvent: Flow<Unit>`, `cut()`, `copy()`, `paste()`.

**File:** [`IHighlightingService.kt`](../../text-editor/src/main/kotlin/.../services/contracts/IHighlightingService.kt)

Interface declares: `createHighlightingStyleSpans(text: String): StyleSpans<Collection<String>>`.

**File:** [`RemoteLismaHighlightingService.kt`](../../text-editor/src/main/kotlin/.../services/RemoteLismaHighlightingService.kt)

Implementation of `IHighlightingService` that delegates to `SimulationServerFacade.highlightSource()`. Converts `SyntaxTokenDto[]` from the server into `StyleSpans<Collection<String>>` with CSS class mappings.

## Blueprint Editor Module

### MVVM Split

```mermaid
flowchart LR
    View["IsmaBlueprintEditor\n(UI only, 112 lines)"]
    VM["IsmaBlueprintViewModel\n(all logic, 461 lines)"]
    Model["BlueprintModel / CanvasViewModel"]

    View --> VM
    VM --> Model

    View --> Canvas["Pane (canvas)"]
    View --> Toolbar["ToolBar (bottom)"]
    View --> Tabs["TabPane (Diagram tab)"]

    VM --> StateBox["StateBox[]"]
    VM --> Arrow["TransactionArrow[] / LoopTransactionArrow[]"]
    VM --> PopOver["EditArrowPopOver (floating)"]
```

**File:** [`IsmaBlueprintEditor.kt`](../../blueprint-editor/src/main/kotlin/.../IsmaBlueprintEditor.kt) (112 lines)

Pure UI component — a `BorderPane` that creates the canvas, toolbar, and delegates all logic to `IsmaBlueprintViewModel`. 100% Kotlin, no FXML files. Constructor takes `editorFactory: ITextEditorFactory`, creates `Pane` canvas and `IsmaBlueprintViewModel` instance. Toolbar buttons and canvas events delegate to viewModel methods. Accessor methods: `getBlueprintModel()`, `setBlueprintModel(model)`.

**File:** [`IsmaBlueprintViewModel.kt`](../../blueprint-editor/src/main/kotlin/.../IsmaBlueprintViewModel.kt) (461 lines)

Contains all editor logic: state management, arrow creation/removal, canvas operations, serialization, and name monitoring.

| Method | Description |
|--------|-------------|
| `resetMode()` | Sets `editorMode = EditorMode.Idle` |
| `toggleAddTransition()` | Sets mode to `AddTransition`, resets counter |
| `toggleRemoveState()` | Sets mode to `RemoveState` |
| `toggleRemoveTransition()` | Sets mode to `RemoveTransition` |
| `addState(x, y, text)` | Creates new `StateBox`, registers name |
| `removeState(box)` | Removes state + associated arrows (protects Main/Init) |
| `recordTransitionSource(box)` | Records first/second click for transition creation |
| `addTransactionArrow(start, end, pred, alias)` | Creates `TransactionArrow` with geometry bindings |
| `addLoopArrow(box, text, pred, alias)` | Creates `LoopTransactionArrow` with geometry bindings |
| `toBlueprintModel()` | Serializes canvas to `BlueprintModel` |
| `fromBlueprintModel(model)` | Rebuilds canvas from serialized model |
| `openStateTextEditor(state)` | Creates text editor tab for state content |
| `openLoopTextEditor(arrow, state)` | Creates text editor tab for loop content |
| `onStatePress/release/drag` | Drag interaction handlers |

### Canvas Architecture

```mermaid
flowchart TD
    Canvas["Pane (scrollable via ScrollPane)"]
    Canvas --> MainState["Main state box (fixed, LightGreen)"]
    Canvas --> InitState["Init state box (fixed, LightBlue)"]
    Canvas --> UserStates["User states [] (Coral, draggable)"]
    Canvas --> Arrows["Transaction arrows [] (center-to-center)"]
    Canvas --> Loops["Loop arrows [] (circle + arrowhead)"]
    Canvas --> PopOver["EditArrowPopOver (transient)"]

    UserStates -.-> Arrows
    UserStates -.-> Loops
```

**File:** [`CanvasViewModel.kt`](../../blueprint-editor/src/main/kotlin/.../models/CanvasViewModel.kt) (69 lines)

Holds observable lists of canvas elements with editor-specific wrapper classes: `states: ObservableList<StateBox>`, `transactions: ObservableList<EditorTransaction>`, `loopTransactions: ObservableList<EditorLoopTransaction>`.

Inner data classes:
- `EditorTransaction(startBox, endBox, arrow: TransactionArrow)`
- `EditorLoopTransaction(stateBox, arrow: LoopTransactionArrow)`

### Editor Factory SPI

**File:** [`ITextEditorFactory.kt`](../../blueprint-editor/src/main/kotlin/.../services/ITextEditorFactory.kt)

Interface for creating/disposing text editor nodes. Injected via Koin to decouple the blueprint editor from the text-editor module. Declares: `createTextEditor(text: String, onTextChanged: (String) -> Unit): Node`, `disposeInstance(node: Node)`.

The implementation (in app module) wraps `IsmaTextEditor` instances and provides per-project Koin scopes. Each blueprint project gets its own factory and editor pool.

### Editor Mode State Machine

**File:** [`EditorMode.kt`](../../blueprint-editor/src/main/kotlin/.../EditorMode.kt)

Sealed class hierarchy defining the editor's interaction modes: `Idle`, `AddTransition(selectedStates: MutableList<StateBox>)`, `RemoveState`, `RemoveTransition`.

Modes are mutually exclusive. Every toolbar button action calls `resetMode()` before setting or toggling its own mode.

## Deep Specification

For detailed specification of the blueprint editor (state boxes, transitions, popover, toolbar modes, geometry algorithms, LISMA conversion, dimensions), see [`blueprint-editor/README.md`](../blueprint-editor/README.md).
