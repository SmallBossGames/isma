# UI Components

## Purpose

The `app` module is the application layer — it contains the JavaFX UI components, business logic services, project models, view models, and the application entry point. It wires together the domain models, external services, and editor modules into a cohesive desktop application.

## Structure

```
app/src/main/kotlin/ru/isma/next/app/
├── launcher/                     # IsmaApplication, Koin DI root, GrinProcessLauncher
├── models/
│   ├── ErrorViewModel.kt
│   ├── preferences/              # PreferencesModel, WindowPreferencesModel
│   ├── projects/                 # IProjectModel, LismaProjectModel, BlueprintProjectModel
│   │   └── LismaTextModel.kt     # CodeRegion for error line tracking
│   └── simulation/               # SimulationParametersModel, CompletedSimulationModel
│       ├── SaveTarget.kt         # MEMORY / FILE enum
│       ├── SimulationTask.kt     # Task model with status/progress/error/result
│       └── CompletedSimulationModel.kt
├── services/
│   ├── ModelErrorService.kt
│   ├── editors/                  # SyntaxHighlighterService, TextEditorFactory
│   ├── koin/                     # Koin DI module definitions (services)
│   ├── preferences/              # PreferencesProvider
│   ├── project/                  # ProjectService, ProjectFileService, LismaPdeService
│   │   └── LismaPdeTranslationResult.kt  # Success/Failed sealed interface
│   └── simualtion/               # SimulationService, SimulationResultService, SimulationParametersService
├── viewmodels/                   # Plain JavaFX property-based view models for settings
├── utilities/                    # BlueprintModelExtensions.kt (convertToLisma)
├── extentions/                   # ButtonExtensions.kt, FormsExtensions.kt
└── constants/                    # FileExtensions.kt (file type constants)
├── views/
│   ├── MainView.kt               # Main BorderPane layout
│   ├── dialogs/                  # ItemsPickerDialog
│   ├── koin/                     # Koin DI module definitions (views)
│   ├── layout/                   # Drawer
│   ├── settings/                 # Settings panel views
│   ├── tabpane/                  # IsmaEditorTabPane
│   └── toolbars/                 # MenuBar, ToolBar, ErrorList, ProcessBar
```

## Module Configuration

**File:** `app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
    application
}

application {
    mainModule.set("isma.ui.app.main")
    mainClass.set("ru.isma.next.app.launcher.IsmaApplication")
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics",
        "--enable-native-access=io.netty.common",
    )
}
```

JavaFX modules: `javafx.controls`, `javafx.fxml`. Key dependencies: Koin, ControlsFX, fxmisc.richtext, Ikonli (Material2 icons), gRPC-Netty with Linux Epoll, Kotlinx Coroutines (JavaFX + serialization).

## Application Entry Point

### IsmaApplication

**File:** `IsmaApplication.kt`

```kotlin
class IsmaApplication : Application(), KoinComponent {
    lateinit var stage: Stage

    private val projectFileService: ProjectFileService by inject()
    private val preferencesProvider: PreferencesProvider by inject()
    private val mainView: MainView by inject()
    private val serverFacade: SimulationServerFacade by inject()

    init {
        ismaKoinStart()
        serverFacade.warmup()
    }

    override fun start(stage: Stage) { ... }
    override fun stop() { ... }
}
```

**Lifecycle:**

1. `init` block — starts Koin DI, warms up the server (starts process + creates clients)
2. `start()` — creates `Scene(mainView)`, initializes window from saved preferences, opens last projects
3. `stop()` — saves window state + last opened file paths to preferences, shuts down server

### Launcher

**File:** `Launcher.kt`

Simple `main()` entry point that calls `Application.launch(IsmaApplication::class.java)`.

### GrinProcessLauncher

**File:** `GrinProcessLauncher.kt`

Launches the GRIN chart viewer as a child process. Resolves script path from `ISMA_GRIN_SCRIPT` env var or `isma.grin.script` system property. Arguments: `--result-file`, `--x-axis`, `--charts`.

## Project Models

### IProjectModel

**File:** `IProjectModel.kt`

```kotlin
interface IProjectModel {
    var name: String
    var file: File?
    val editor: Node
    fun nameProperty(): SimpleStringProperty
    fun snapshot(): LismaTextModel
    fun dispose()
}
```

### LismaProjectModel

**File:** `LismaProjectModel.kt`

Text-based LISMA project. Implements `KoinScopeComponent` — creates its own Koin scope and injects `LismaProjectDataProvider` and `IsmaTextEditor` (scoped).

```kotlin
class LismaProjectModel: IProjectModel, KoinScopeComponent {
    override val scope: Scope by lazy { createScope() }
    private val dataProvider by inject<LismaProjectDataProvider>()

    var lismaText: String
        get() { fetchText(); return lismaTextValue }
        set(value) { lismaTextValue = value; pushText() }

    override val editor: Node by inject(named<IsmaEditorQualifier>())
    override fun dispose() { scope.close() }
}
```

Text changes are synchronized through `dataProvider` — `pushText()` writes to the data provider, `fetchText()` reads from it. The `IsmaTextEditor` reads from the same provider, creating a shared model.

### BlueprintProjectModel

**File:** `BlueprintProjectModel.kt`

Visual statechart project. Similar pattern to `LismaProjectModel` but backed by `BlueprintProjectDataProvider` and `BlueprintModel` (serialized JSON).

```kotlin
class BlueprintProjectModel : IProjectModel, KoinScopeComponent {
    override val scope: Scope by lazy { createScope() }
    private val dataProvider by inject<BlueprintProjectDataProvider>()

    var blueprint: BlueprintModel
        get() { fetchBlueprint(); return blueprintValue }
        set(value) { blueprintValue = value; pushBlueprint() }

    override fun snapshot() = blueprint.convertToLisma()
    override fun dispose() { scope.close() }
}
```

`snapshot()` converts the visual blueprint model to LISMA text via `BlueprintModelExtensions.kt`, which handles main text, state blocks, and loop transactions.

### Additional Project Models

**File:** `LismaProjectDataProvider.kt` / `BlueprintProjectDataProvider.kt`

Data providers that act as the shared model between the project model and the editor. `LismaProjectDataProvider` holds the LISMA text string and notifies observers on changes. `BlueprintProjectDataProvider` holds the `BlueprintModel` and similarly provides change notification.

## Services

### ProjectService

**File:** `ProjectService.kt`

```kotlin
class ProjectService {
    val projects = FXCollections.observableSet<IProjectModel>()
    var activeProject: IProjectModel? = null

    fun createNewBlueprint(name: String = "New statechart") { ... }
    fun createNew(name: String = "New project") { ... }
    fun addText(project: LismaProjectModel) { projects.add(project) }
    fun addBlueprint(project: BlueprintProjectModel) { projects.add(project) }
    fun close(project: IProjectModel) { projects.remove(project); project.dispose() }
    fun closeAll() { ... }
}
```

Manages the observable set of projects. `IsmaEditorTabPane` observes this set to create/delete tabs.

### ProjectFileService

**File:** `ProjectFileService.kt`

FileChooser-based open/save operations. Supports three file types:
- `*.im2` — legacy ISMA project (marked TODO for backward compatibility)
- `*.iscm2` — text-based LISMA project → `LismaProjectModel`
- State chart project → `BlueprintProjectModel` (JSON-encoded `BlueprintModel`)

Save operations write `project.lismaText` or `Json.encodeToString(project.blueprint)` to file.

### SimulationService (thin coordinator)

**File:** `SimulationService.kt`

A 36-line thin wrapper that delegates to `SimulationTaskService`. It snapshots parameters, resolves the active project, and calls `SimulationTaskService.submit()`.

```kotlin
class SimulationService(
    private val projectService: ProjectService,
    private val simulationTaskService: SimulationTaskService,
    private val simulationParametersService: SimulationParametersService,
) : KoinComponent {
    fun simulate() { ... }
    fun stopSimulation(task: SimulationTask) { simulationTaskService.cancelTask(task) }
    companion object { val SimulationScope = SimulationTaskService.SimulationScope }
}
```

### SimulationTaskService (full lifecycle)

**File:** `SimulationTaskService.kt`

The complete simulation pipeline (151 lines). Owns `val tasks: ObservableList<SimulationTask> = SimulationTask.ALL` and manages the 4-phase lifecycle:

| Phase | Method | Description |
|-------|--------|-------------|
| Compile | `serverFacade.compileModel(sourceCode)` | Populates `ModelErrorService` with errors; fails fast if errors exist |
| Run | `serverFacade.runSimulation(runParams)` | Starts simulation, adds task to `tasks` list |
| Monitor | `serverFacade.monitorSimulation(simulationId, 0.01)` | Collects `Flow<SimulationProgress>`, normalizes to 0.0–1.0, calls `task.setProgress()` |
| Download | `serverFacade.downloadResultToCache(simulationId)` | Creates `CompletedSimulationModel`, sets `task.result` and `task.setStatus(COMPLETED)` |

Uses `SimulationScope` — a global `CoroutineScope` backed by `Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()` with `SupervisorJob()`. Each job is tracked in `currentJobs: MutableMap<SimulationTask, Job>`.

```kotlin
class SimulationTaskService(
    private val serverFacade: SimulationServerFacade,
    private val modelErrorService: ModelErrorService,
    private val projectService: ProjectService,
) : KoinComponent {
    val tasks: ObservableList<SimulationTask> = SimulationTask.ALL
    fun submit(modelName: String, params: RunSimulationParams, simulationParameters: SimulationParametersModel): SimulationTask
    fun cancelTask(task: SimulationTask)
}
```

### SimulationResultService

**File:** `SimulationResultService.kt`

Manages completed simulation results. Constructor takes `SimulationTaskService` (for task removal from `SimulationTask.ALL`) and `GrinProcessLauncher`.

| Method | Description |
| --- | --- |
| `removeResult(task)` | Removes task from `SimulationTask.ALL`, clears `task.result` |
| `showChart(task)` | Opens Grin chart viewer with axis picker dialog |
| `exportToFile(task, file)` | Exports to CSV asynchronously |

CSV export uses `BinaryFilePointProvider` to stream points via coroutine flow, writing to a buffered `Writer` on `Dispatchers.IO`. Uses `ResultServiceScope` (`CoroutineScope(Dispatchers.Default)`).

### LismaPdeService

**File:** `LismaPdeService.kt`

Validates LISMA source code via `serverFacade.validateModel()` and populates `ModelErrorService` with `ErrorViewModel` entries. Returns a sealed interface:

```kotlin
sealed interface LismaPdeTranslationResult
data object SuccessTranslation : LismaPdeTranslationResult
data object FailedTranslation : LismaPdeTranslationResult
```

### SyntaxHighlighterService

**File:** `editors/SyntaxHighlighterService.kt`

Provides syntax highlighting for the text editor. Delegates to `RemoteLismaHighlightingService` which calls `serverFacade.highlightSource()` to get token positions and kinds from the server.

### TextEditorFactory

**File:** `editors/TextEditorFactory.kt`

Factory for creating and disposing `IsmaTextEditor` instances. Used by the blueprint editor to create text editor tabs for state/loop content editing. Implements `ITextEditorFactory` interface from the blueprint-editor module.

### ModelErrorService

**File:** `ModelErrorService.kt`

Tracks compilation and validation errors in an `ObservableList<ErrorViewModel>`. Populated by `LismaPdeService` (validation) and `SimulationService` (compilation errors). Consumed by `IsmaErrorListTable` for display.

### SimulationParametersService

**File:** `SimulationParametersService.kt`

Manages simulation parameter view models and provides store/load persistence as JSON files:

| Property | Type | Default |
| --- | --- | --- |
| `cauchyInitials` | `CauchyInitialsViewModel` | start=0.0, end=10.0, step=0.1 |
| `integrationMethod` | `IntegrationMethodParametersViewModel` | accuracy=0.1, server="localhost", port=7890, selectedMethod=first from list |
| `eventDetection` | `EventDetectionParametersViewModel` | gamma=0.8, lowBorder=0.001 |
| `resultSaving` | `ResultSavingParametersViewModel` | target=MEMORY |
| `resultProcessing` | `ResultProcessingParametersViewModel` | tolerance=20.0, selectedSimplifyMethod="Radial-Distance" |
| `integrationMethods` | `ObservableList<String>` | From server |
| `simplifyMethods` | `ObservableList<String>` | "Radial-Distance", "Douglas-Peucker" |

**Methods:**
- `store()` — opens FileChooser, serializes snapshot to JSON
- `load()` — opens FileChooser, deserializes JSON into view models
- `snapshot()` — captures current view model state into `SimulationParametersModel`
- `commit(model)` — applies `SimulationParametersModel` to all view models

File extension filter: `*.params.json` (constant from `FileExtentions.kt`).

### PreferencesProvider

**File:** `PreferencesProvider.kt`

JSON file persistence using `kotlinx.serialization`. Stores `WindowPreferencesModel` (geometry) and `DefaultFilesPreferencesModel` (last opened paths).

```kotlin
class PreferencesProvider(private val settingsFilePath: String) {
    var preferences: PreferencesModel = load() private set

    fun commit(windowPreferences: WindowPreferencesModel) { ... }
    fun commit(defaultFilesPreferences: DefaultFilesPreferencesModel) { ... }
}
```

## Views

### MainView

**File:** `MainView.kt`

`BorderPane` layout:

```
┌──────────────────────────────────────────────────────┐
│ MenuBar                                              │
│ ToolBar                                              │
│                                                      │
│ ┌────────────────────┐  ┌─────────────────────────┐ │
│ │                    │  │                         │ │
│ │  EditorTabPane     │  │ SettingsPanelView       │ │
│ │  (center)          │  │ (right)                 │ │
│ │                    │  │                         │ │
│ ├────────────────────┤  └─────────────────────────┘ │
│ │ ErrorListDrawer    │                              │
│ │ (collapsible)      │                              │
│ ├────────────────────┤                              │
│ │ SimulationProcessBar│                             │
│ └────────────────────┘                              │
└──────────────────────────────────────────────────────┘
```

The left drawer (`Drawer`) is commented out. The error list is now a dedicated `ErrorListDrawer` in the bottom area (above the simulation process bar).

### ErrorListDrawer

**File:** `ErrorListDrawer.kt`

A `TitledPane("Error list", ismaErrorListTable)` that wraps `IsmaErrorListTable` in a collapsible panel. `isCollapsible = true`, `isExpanded = false` by default. Used in the bottom area of `MainView`.

### IsmaEditorTabPane

**File:** `IsmaEditorTabPane.kt`

Observes `ProjectService.projects` via coroutine flow. Creates a `Tab` for each project with `Tab(it.name, it.editor)`. On tab close, calls `projectController.close(project)` which removes from the set and disposes the project scope.

```kotlin
init {
    coroutinesScope.launch {
        merge(
            projectController.projects.asFlow(),
            projectController.projects.addedAsFlow()
        ).cancellable().collect {
            addTabAndSelect(Tab(it.name, it.editor).apply { initProjectTab(it) })
        }
    }
}
```

### Toolbars

| Component | File | Description |
| --- | --- | --- |
| `IsmaMenuBar` | `IsmaMenuBar.kt` | File (New/Open/Save/Close/Exit), Edit (Cut/Copy/Paste), Simulation (Verify/Run/Store/Load Settings) |
| `IsmaToolBar` | `IsmaToolBar.kt` | Same commands as buttons: New model, New statechart, Open, Save, Save all, Cut, Copy, Paste, Verify, Store/Load Settings |
| `SimulationProcessBar` | `SimulationProcessBar.kt` | Play button (triggers `simulate()`) + Tasks button (opens `TasksPopOver`) |
| `IsmaErrorListTable` | `IsmaErrorListTable.kt` | `TableView<ErrorViewModel>` with Row/Position/Fragment/Message columns |
| `TasksPopOver` | `TasksPopOver.kt` | `PopOver` with 3 sections (In progress, Completed, Failed) bound to `SimulationTaskService.tasks` via `changeAsFlow()` |

### Settings Panel

**File:** `SettingsPanelView.kt`

Extends `PropertiesAccordion` (from toolkit) with 4 sub-views wrapped in `VBox` with `styleClass = "settings-box"` and `prefWidth = 240.0`:

| View | Model | Purpose |
| --- | --- | --- |
| `CauchyInitialsView` | `CauchyInitialsViewModel` | Start time, end time, initial step |
| `MethodSettingsView` | `IntegrationMethodParametersViewModel` | Method, accuracy, stability, parallel, server/port |
| `EventDetectionView` | `EventDetectionParametersViewModel` | Event detection, gamma, step limit, low border |
| `ResultProcessingView` | `ResultSavingParametersViewModel` | Save target (MEMORY/FILE only — Simplify/Tolerance not yet in UI) |

`MethodSettingsView` includes `Accurate` checkbox (with `Accuracy` field disabled unless enabled), `Stable` checkbox, `Parallel` checkbox (with `Server` and `Port` fields disabled unless enabled).

## ViewModels

All view models use plain JavaFX `Simple*Property` classes with Kotlin property delegation (`getValue`/`setValue` from toolkit). No TornadoFX `bind` helpers.

### CauchyInitialsViewModel

```kotlin
class CauchyInitialsViewModel {
    private val startTimeProperty = SimpleDoubleProperty()
    private val endTimeProperty = SimpleDoubleProperty()
    private val stepProperty = SimpleDoubleProperty()

    var startTime by startTimeProperty
    var endTime by endTimeProperty
    var step by stepProperty

    fun commit(model: CauchyInitialsModel) { ... }
    fun snapshot() = CauchyInitialsModel(startTime, endTime, step)
}
```

Defaults set at `SimulationParametersService` init: `startTime=0.0`, `endTime=10.0`, `step=0.1`.

### IntegrationMethodParametersViewModel

```kotlin
class IntegrationMethodParametersViewModel {
    val selectedMethodProperty = SimpleStringProperty()
    var selectedMethod: String by selectedMethodProperty

    val accuracyProperty = SimpleDoubleProperty()
    var accuracy by accuracyProperty

    val isAccuracyInUseProperty = SimpleBooleanProperty()
    var isAccuracyInUse by isAccuracyInUseProperty

    val isStableAllowedProperty = SimpleBooleanProperty()
    var isStableAllowedInUse by isStableAllowedProperty

    val isStableInUseProperty = SimpleBooleanProperty()
    var isStableInUse by isStableInUseProperty

    val isParallelInUseProperty = SimpleBooleanProperty()
    var isParallelInUse by isParallelInUseProperty

    val serverProperty = SimpleStringProperty()
    var server: String by serverProperty

    val portProperty = SimpleIntegerProperty()
    var port by portProperty

    fun commit(model: IntegrationMethodParametersModel) { ... }
    fun snapshot() = IntegrationMethodParametersModel(...)
}
```

Defaults set at `SimulationParametersService` init: `accuracy=0.1`, `server="localhost"`, `port=7890`. `selectedMethod` is set to the first item from `integrationMethods` list.

### EventDetectionParametersViewModel

```kotlin
class EventDetectionParametersViewModel {
    val isEventDetectionInUseProperty = SimpleBooleanProperty()
    var isEventDetectionInUse by isEventDetectionInUseProperty

    val isStepLimitInUseProperty = SimpleBooleanProperty()
    var isStepLimitInUse by isStepLimitInUseProperty

    val gammaProperty = SimpleDoubleProperty()
    var gamma by gammaProperty

    val lowBorderProperty = SimpleDoubleProperty()
    var lowBorder by lowBorderProperty

    fun commit(model: EventDetectionParametersModel) { ... }
    fun snapshot() = EventDetectionParametersModel(...)
}
```

Defaults set at `SimulationParametersService` init: `gamma=0.8`, `lowBorder=0.001`.

### ResultSavingParametersViewModel

```kotlin
class ResultSavingParametersViewModel {
    val savingTargetProperty = SimpleObjectProperty(SaveTarget.MEMORY)
    var savingTarget: SaveTarget by savingTargetProperty

    fun commit(model: ResultSavingParametersModel) { ... }
    fun snapshot() = ResultSavingParametersModel(savingTarget)
}
```

### ResultProcessingParametersViewModel

```kotlin
class ResultProcessingParametersViewModel {
    val isSimplifyInUseProperty = SimpleBooleanProperty()
    var isSimplifyInUse by isSimplifyInUseProperty

    val selectedSimplifyMethodProperty = SimpleStringProperty()
    var selectedSimplifyMethod: String by selectedSimplifyMethodProperty

    val toleranceProperty = SimpleDoubleProperty()
    var tolerance by toleranceProperty
}
```

Defaults set at `SimulationParametersService` init: `tolerance=20.0`, `selectedSimplifyMethod` set to first item from `simplifyMethods` list ("Radial-Distance").

### SimulationParametersService defaults

| Property | Default Value |
| --- | --- |
| `cauchyInitials.startTime` | `0.0` |
| `cauchyInitials.endTime` | `10.0` |
| `cauchyInitials.step` | `0.1` |
| `integrationMethod.accuracy` | `0.1` |
| `integrationMethod.server` | `"localhost"` |
| `integrationMethod.port` | `7890` |
| `resultSaving.savingTarget` | `SaveTarget.MEMORY` |
| `resultProcessing.tolerance` | `20.0` |
| `eventDetection.gamma` | `0.8` |
| `eventDetection.lowBorder` | `0.001` |
| `integrationMethods` | From server (first selected) |
| `simplifyMethods` | `"Radial-Distance"`, `"Douglas-Peucker"` |

## Text Editor Module

### EditorPlatformService

**File:** `text-editor/src/main/kotlin/.../services/EditorPlatformService.kt`

Coroutine-based service that propagates cut/copy/paste events via `MutableSharedFlow`. Used by `IsmaTextEditor` to respond to platform clipboard commands:

```kotlin
class EditorPlatformService : IEditorPlatformService {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val cutEventInternal = MutableSharedFlow<Unit>()
    override val cutEvent = cutEventInternal.asSharedFlow()

    private val copyEventInternal = MutableSharedFlow<Unit>()
    override val copyEvent = copyEventInternal.asSharedFlow()

    private val pasteEventInternal = MutableSharedFlow<Unit>()
    override val pasteEvent = pasteEventInternal.asSharedFlow()

    override fun cut() {
        coroutineScope.launch { cutEventInternal.emit(Unit) }
    }

    override fun copy() {
        coroutineScope.launch { copyEventInternal.emit(Unit) }
    }

    override fun paste() {
        coroutineScope.launch { pasteEventInternal.emit(Unit) }
    }
}
```

### IEditorPlatformService (Contract)

**File:** `text-editor/src/main/kotlin/.../services/contracts/IEditorPlatformService.kt`

Interface defining the contract for editor platform services:

```kotlin
interface IEditorPlatformService {
    val cutEvent: Flow<Unit>
    val copyEvent: Flow<Unit>
    val pasteEvent: Flow<Unit>
    fun cut()
    fun copy()
    fun paste()
}
```

### IHighlightingService (Contract)

**File:** `text-editor/src/main/kotlin/.../services/contracts/IHighlightingService.kt`

Interface defining the contract for syntax highlighting services:

```kotlin
interface IHighlightingService {
    fun createHighlightingStyleSpans(text: String): StyleSpans<Collection<String>>
}
```

### RemoteLismaHighlightingService

**File:** `text-editor/src/main/kotlin/.../services/RemoteLismaHighlightingService.kt`

Implementation of `IHighlightingService` that delegates to `SimulationServerFacade.highlightSource()`. Converts `SyntaxTokenDto[]` from the server into `StyleSpans<Collection<String>>` with CSS class mappings:
- `SyntaxTokenKind.KEYWORD` → `syntax-keyword`
- `SyntaxTokenKind.COMMENT` → `syntax-comment`
- `SyntaxTokenKind.NUMBER` → `syntax-decimal`
- `SyntaxTokenKind.UNSPECIFIED` → `syntax-default`

### IsmaTextEditor

**File:** `text-editor/src/main/kotlin/.../IsmaTextEditor.kt`

`BorderPane` wrapping `fxmisc.richtext.CodeArea` with:
- Consolas 12pt font
- Line numbers via `LineNumberFactory`
- Remote syntax highlighting via `IHighlightingService` (delegates to server's `highlightSource()`)
- Cut/copy/paste event propagation from `IEditorPlatformService`

```kotlin
class IsmaTextEditor(
    private val textEditorService: IEditorPlatformService,
    private val highlightingService: IHighlightingService,
) : BorderPane() {

    init {
        area = CodeArea().apply {
            style = "-fx-font-family: consolas; -fx-font-size: 12pt;"

            // Collect cut/copy/paste events and propagate if focused
            textEditorService.cutEvent.collect { if (isFocused) cut() }
            textEditorService.copyEvent.collect { if (isFocused) copy() }
            textEditorService.pasteEvent.collect { if (isFocused) paste() }

            // Apply syntax highlighting on text change
            textProperty().addListener { _, _, newValue ->
                val highlighting = highlightingService.createHighlightingStyleSpans(newValue ?: "")
                setStyleSpans(0, highlighting)
            }
        }
        center = area
    }
}
```

### Syntax Highlighting Pipeline

```
Server (highlightSource) → SyntaxTokenDto[] → RemoteLismaHighlightingService → StyleSpans<Collection<String>> → CodeArea.setStyleSpans()
```

CSS classes applied: `syntax-keyword`, `syntax-comment`, `syntax-decimal`, `syntax-default`.

## Blueprint Editor Module

### IsmaBlueprintEditor (UI only)

**File:** `blueprint-editor/src/main/kotlin/.../IsmaBlueprintEditor.kt` (113 lines)

Pure UI component — a `BorderPane` that creates the canvas, toolbar, and delegates all logic to `IsmaBlueprintViewModel`. 100% Kotlin, no FXML files.

```kotlin
class IsmaBlueprintEditor(editorFactory: ITextEditorFactory) : BorderPane() {
    private val canvas = Pane()
    private val viewModel = IsmaBlueprintViewModel(editorFactory, canvas)
    // toolbar buttons → viewModel methods
    // canvas events → viewModel methods
    fun getBlueprintModel() = viewModel.toBlueprintModel()
    fun setBlueprintModel(model: BlueprintModel) { viewModel.fromBlueprintModel(model) }
}
```

### IsmaBlueprintViewModel (all logic)

**File:** `blueprint-editor/src/main/kotlin/.../IsmaBlueprintViewModel.kt` (421 lines)

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

### CanvasViewModel

**File:** `blueprint-editor/src/main/kotlin/.../models/CanvasViewModel.kt` (61 lines)

Holds observable lists of canvas elements:

```kotlin
class CanvasViewModel {
    val states: ObservableList<StateBox>
    val transactions: ObservableList<EditorTransaction>
    val loopTransactions: ObservableList<EditorLoopTransaction>
}
```

Inner data classes replace the old `BlueprintEditorTransactionModel` / `BlueprintEditorLoopTransactionModel`:
- `EditorTransaction(startBox, endBox, arrow: TransactionArrow)`
- `EditorLoopTransaction(stateBox, arrow: LoopTransactionArrow)`

## DI Configuration

### Service Module (`KoinExtentions.kt`)

**File:** `app/src/main/kotlin/.../services/koin/KoinExtentions.kt`

```kotlin
val simulationServerModule = module {
    single { SimulationServerManager() }
    single { SimulationServerFacade(get()) }
}

val appServicesModule = module {
    single<IEditorPlatformService> { EditorPlatformService() }
    single<ProjectService> { ProjectService() }
    single<ProjectFileService> { ProjectFileService(get()) }
    single<ModelErrorService> { ModelErrorService() }
    single<LismaPdeService> { LismaPdeService(get(), get()) }
    single<SimulationParametersService> { SimulationParametersService(get<SimulationServerFacade>().getSimulationMethods()) }
    single<SimulationTaskService> { SimulationTaskService(get(), get(), get()) }
    single<SimulationResultService> { SimulationResultService(get(), get()) }
    single<SimulationService> { SimulationService(get(), get(), get()) }
    single { PreferencesProvider(APPLICATION_PREFERENCES_FILE) }
}
```

**Changes from previous version:**
- `SimulationTaskService` added (takes `serverFacade`, `modelErrorService`, `projectService`)
- `SimulationResultService` now takes 2 params (`grinProcessLauncher`, `simulationTaskService`)
- `SimulationService` now takes 3 params (`projectService`, `simulationTaskService`, `simulationParametersService`) — no longer depends on `SimulationResultService`, `serverFacade`, or `ModelErrorService` directly

### Launcher Module (`DependecyInjectionRootModule.kt`)

**File:** `app/src/main/kotlin/.../launcher/DependecyInjectionRootModule.kt`

```kotlin
fun ismaKoinStart() = startKoin {
    modules(
        simulationServerModule,
        appServicesModule,
    )

    modules(
        grinProcessLauncherModule,
    )

    modules(
        toolbarsModule,
        mainViewModule,
        settingsPanelModule,
        editorTabPaneModule,
        lismaTextEditorModule,
        blueprintEditorModule,
    )
}

val grinProcessLauncherModule = module {
    single { GrinProcessLauncher() }
}
```

DI initialization order: service modules → Grin launcher → view modules. All registrations use `single()` (singleton) lifecycle.

### View Module (`KoinExtensions.kt`)

**File:** `app/src/main/kotlin/.../views/koin/KoinExtensions.kt`

```kotlin
class IsmaEditorQualifier

val editorModule = module {
    single<ISyntaxHighlighter> { SyntaxHighlighterService(get()) }
    single<IHighlightingService> { RemoteLismaHighlightingService(get()) }
}

val lismaTextEditorModule = module {
    includes(editorModule)
    scope<LismaProjectModel> {
        scopedOf(::LismaProjectDataProvider)
        scopedOf(::IsmaTextEditor) onClose { it?.dispose() }
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaTextEditor>() }
    }
}

val blueprintEditorModule = module {
    includes(editorModule)
    scope<BlueprintProjectModel> {
        scoped<ITextEditorFactory>{ TextEditorFactory { get() } }
        factoryOf(::IsmaTextEditor) onClose { it?.dispose() }
        scopedOf(::BlueprintProjectDataProvider)
        scopedOf(::IsmaBlueprintEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaBlueprintEditor>() }
    }
}

val toolbarsModule = module {
    single { IsmaMenuBar(get(),get(),get(),get(),get()) }
    single { IsmaToolBar(get(),get(),get(),get(),get()) }
    single { SimulationProcessBar(get(), get()) }
    single { IsmaErrorListTable(get()) }
    single { ErrorListDrawer(get()) }
    factory { TasksPopOver(get(), get()) }
}

val editorTabPaneModule = module {
    single { IsmaEditorTabPane(get()) }
}

val settingsPanelModule = module {
    single { CauchyInitialsView(get()) }
    single { EventDetectionView(get()) }
    single { MethodSettingsView(get()) }
    single { ResultProcessingView(get()) }
    single { SettingsPanelView(get(),get(),get(),get()) }
}

val mainViewModule = module {
    single { MainView(get(), get(), get(), get(), get(), get()) }
}
```

All registrations use `single()` (singleton) except `TasksPopOver` which uses `factory()` (created each time). Editor components within project scopes use `scopedOf(::)` for per-project lifecycle.

**Key distinction:** `lismaTextEditorModule` uses `scopedOf(::IsmaTextEditor)` (one instance per LISMA project scope), while `blueprintEditorModule` uses `factoryOf(::IsmaTextEditor)` (new instance each time) because blueprint projects need multiple text editor tabs (one per state/loop content editor).

**New in `toolbarsModule`:** `ErrorListDrawer` is now registered as a singleton, injected into `MainView`.

## Error Handling

| Scenario | Behavior |
| --- | --- |
| Compilation errors | `CompilationErrorDto[]` → `ErrorViewModel[]` → `ModelErrorService.errors` → `IsmaErrorListTable` |
| Simulation compile failure | `task.setStatus(FAILED)`, `task.setError("Compilation failed: ...")` |
| Simulation run failure | `task.setStatus(FAILED)`, `task.setError("Monitor error: ...")` |
| Simulation download failure | `task.setStatus(FAILED)`, `task.setError("Download error: ...")` |
| No active project | `task.setStatus(FAILED)`, `task.setError("No active project")` |
| Server process not found | `IllegalStateException` — "isma-server script not found at: $scriptPath" |
| Missing env/property | `IllegalStateException` — "Neither environment variable ... nor system property ... is set" |
