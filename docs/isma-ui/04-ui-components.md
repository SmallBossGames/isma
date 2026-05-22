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
│       ├── InProgressSimulationModel.kt
│       └── CompletedSimulationModel.kt
├── services/
│   ├── ModelErrorService.kt
│   ├── editors/                  # SyntaxHighlighterService, TextEditorFactory
│   ├── koin/                     # Koin DI module definitions (services)
│   ├── preferences/              # PreferencesProvider
│   ├── project/                  # ProjectService, ProjectFileService, LismaPdeService
│   │   └── LismaPdeTranslationResult.kt  # Success/Failed sealed interface
│   └── simualtion/               # SimulationService, SimulationResultService, SimulationParametersService
├── viewmodels/                   # TornadoFX view models for settings
├── utilities/                    # BlueprintModelExenstions.kt (convertToLisma)
├── extention/                    # ButtonExtensions.kt, FormsExtentions.kt
├── constants/                    # FileExtentions.kt (file type constants)
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

JavaFX modules: `javafx.controls`, `javafx.fxml`. Key dependencies: Koin, TornadoFX, ControlsFX, fxmisc.richtext, Ikonli (Material2 icons), gRPC-Netty with Linux Epoll.

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

`snapshot()` converts the visual blueprint model to LISMA text via `BlueprintModelExenstions.kt`, which handles main text, state blocks, and loop transactions.

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

### SimulationService

**File:** `SimulationService.kt`

Orchestrates the full simulation lifecycle:

```kotlin
class SimulationService(
    private val projectService: ProjectService,
    private val simulationResult: SimulationResultService,
    private val simulationParametersService: SimulationParametersService,
    private val serverFacade: SimulationServerFacade,
    private val modelErrorService: ModelErrorService,
) : KoinComponent {
    val trackingTasks = FXCollections.observableArrayList<InProgressSimulationModel>()!!

    fun simulate() { ... }
    fun stopSimulation(trackingTask: InProgressSimulationModel) { ... }

    companion object {
        private val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor()
            .asCoroutineDispatcher()
        val SimulationScope = CoroutineScope(virtualThreadDispatcher + SupervisorJob())
    }
}
```

**Flow:**

1. Snapshot simulation parameters and project source
2. Compile model via `serverFacade.compileModel()`
3. Report compilation errors to `ModelErrorService` → `IsmaErrorListTable`
4. Run simulation via `serverFacade.runSimulation()`
5. Monitor progress via `serverFacade.monitorSimulation()` → flow → `trackingTask.commitProgress()`
6. Download result via `serverFacade.downloadResultToCache()`
7. Create `CompletedSimulationModel` and commit to `SimulationResultService`

Uses a virtual-thread-backed coroutine dispatcher with `SupervisorJob` for simulation concurrency. UI updates are marshalled via `Platform.runLater`.

### SimulationResultService

**File:** `SimulationResultService.kt`

Manages completed simulation results:

| Method | Description |
| --- | --- |
| `commitResult(result)` | Adds to `trackingTasksResults` observable list |
| `removeResult(result)` | Removes from list |
| `showChart(result)` | Opens Grin chart viewer with axis picker dialog |
| `exportToFile(result, file)` | Exports to CSV asynchronously |

CSV export uses `BinaryFilePointProvider` to stream points via coroutine flow, writing to a buffered `Writer` on `Dispatchers.IO`.

### LismaPdeService

**File:** `LismaPdeService.kt`

Validates LISMA source code via `serverFacade.validateModel()` and populates `ModelErrorService` with `ErrorViewModel` entries. Returns a sealed interface:

```kotlin
sealed interface LismaPdeTranslationResult
data object SuccessTranslation : LismaPdeTranslationResult
data object FailedTranslation : LismaPdeTranslationResult
```

### SimulationParametersService

**File:** `SimulationParametersService.kt`

Manages simulation parameter view models and provides store/load persistence as JSON files:

| Property | Type | Default |
| --- | --- | --- |
| `cauchyInitials` | `CauchyInitialsViewModel` | start=0.0, end=10.0, step=0.1 |
| `integrationMethod` | `IntegrationMethodParametersViewModel` | accuracy=0.1, server=localhost, port=7890 |
| `eventDetection` | `EventDetectionParametersViewModel` | gamma=0.8, lowBorder=0.001 |
| `resultSaving` | `ResultSavingParametersViewModel` | target=MEMORY |
| `resultProcessing` | `ResultProcessingParametersViewModel` | tolerance=20.0 |
| `integrationMethods` | `ObservableList<String>` | From server |
| `simplifyMethods` | `ObservableList<String>` | Radial-Distance, Douglas-Peucker |

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
│ │ Drawer:            │                              │
│ │ ErrorListTable     │                              │
│ ├────────────────────┤                              │
│ │ SimulationProcessBar│                             │
│ └────────────────────┘                              │
└──────────────────────────────────────────────────────┘
```

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
| `TasksPopOver` | `TasksPopOver.kt` | `PopOver` bound to `SimulationService.trackingTasks` + `SimulationResultService.trackingTasksResults` |

### Settings Panel

**File:** `SettingsPanelView.kt`

TornadoFX `drawer` with 4 sub-views:

| View | Model | Purpose |
| --- | --- | --- |
| `CauchyInitialsView` | `CauchyInitialsViewModel` | Start time, end time, initial step |
| `MethodSettingsView` | `IntegrationMethodParametersViewModel` | Integration method, accuracy, stability control |
| `EventDetectionView` | `EventDetectionParametersViewModel` | Event detection, step limit, gamma, low border |
| `ResultProcessingView` | `ResultProcessingParametersViewModel` | Simplification, tolerance |

## ViewModels

All view models use TornadoFX `bind` helpers for property binding to UI controls.

### CauchyInitialsViewModel

```kotlin
class CauchyInitialsViewModel : ViewModel() {
    val startTime = bindDouble(0.0)
    val endTime = bindDouble(1.0)
    val step = bindDouble(0.01)
}
```

### IntegrationMethodParametersViewModel

```kotlin
class IntegrationMethodParametersViewModel(val methods: List<String>) : ViewModel() {
    val selectedMethod = bind("method")
    val accuracy = bindDouble(0.001)
    val isAccuracyInUse = bind(true)
    val isStableInUse = bind(true)
    val isParallelInUse = bind(false)
    val server = bind("")
    val port = bind(0)
}
```

### EventDetectionParametersViewModel

```kotlin
class EventDetectionParametersViewModel : ViewModel() {
    val isEventDetectionInUse = bind(false)
    val isStepLimitInUse = bind(false)
    val gamma = bindDouble(0.001)
    val lowBorder = bindDouble(0.0)
}
```

### ResultSavingParametersViewModel

```kotlin
class ResultSavingParametersViewModel : ViewModel() {
    val savingTarget = bind(SaveTarget.FILE)
}
```

### ResultProcessingParametersViewModel

```kotlin
class ResultProcessingParametersViewModel : ViewModel() {
    val isSimplifyInUse = bind(false)
    val selectedSimplifyMethod = bind("")
    val tolerance = bindDouble(0.001)
}
```

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

### IsmaBlueprintEditor

**File:** `blueprint-editor/src/main/kotlin/.../IsmaBlueprintEditor.kt`

Visual statechart editor with a canvas `Pane` containing `StateBox` nodes and `TransactionArrow` / `LoopTransactionArrow` connections.

**Editor modes:**
- **Add state** — toolbar button creates new `StateBox`
- **Add transition** — click two states in sequence to create `TransactionArrow`; clicking same state twice creates `LoopTransactionArrow`
- **Remove state** — click a state to delete it (removes associated arrows)
- **Remove transition** — click an arrow to delete it

**State boxes:**
- `mainStateBox` — green, fixed position, non-editable
- `initStateBox` — blue, fixed position, non-editable
- Additional states — coral-colored, draggable, editable name via double-click

**Interaction:**
- Double-click a state or loop arrow → opens a text editor tab via `ITextEditorFactory`
- Single-click an arrow → opens `EditArrowPopOver` for editing predicate and alias
- Drag states → updates `layoutX`/`layoutY` bindings on connected arrows

**Serialization:**

```kotlin
fun getBlueprintModel(): BlueprintModel
fun setBlueprintModel(model: BlueprintModel)
```

`BlueprintModel` is `@Serializable` and stores `main`, `init`, `states`, `transactions`, `loopTransactions`.

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
    single<SimulationResultService> { SimulationResultService(get()) }
    single<SimulationService> { SimulationService(get(), get(), get(), get(), get()) }
    single { PreferencesProvider(APPLICATION_PREFERENCES_FILE) }
}
```

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
        scoped<ITextEditorFactory> { TextEditorFactory { get() } }
        factoryOf(::IsmaTextEditor) onClose { it?.dispose() }
        scopedOf(::BlueprintProjectDataProvider)
        scopedOf(::IsmaBlueprintEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaBlueprintEditor>() }
    }
}

val toolbarsModule = module {
    single { IsmaMenuBar(get(), get(), get(), get(), get()) }
    single { IsmaToolBar(get(), get(), get(), get(), get()) }
    single { SimulationProcessBar(get(), get()) }
    single { IsmaErrorListTable(get()) }
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
    single { SettingsPanelView(get(), get(), get(), get()) }
}

val mainViewModule = module {
    single { MainView(get(), get(), get(), get(), get(), get()) }
}
```

All registrations use `single()` (singleton) except `TasksPopOver` which uses `factory()` (created each time). Editor components within project scopes use `scopedOf(::)` for per-project lifecycle.

**Key distinction:** `lismaTextEditorModule` uses `scopedOf(::IsmaTextEditor)` (one instance per LISMA project scope), while `blueprintEditorModule` uses `factoryOf(::IsmaTextEditor)` (new instance each time) because blueprint projects need multiple text editor tabs (one per state/loop content editor).

## Error Handling

| Scenario | Behavior |
| --- | --- |
| Compilation errors | `CompilationErrorDto[]` → `ErrorViewModel[]` → `ModelErrorService.errors` → `IsmaErrorListTable` |
| Server process not found | `IllegalStateException` — "isma-server script not found at: $scriptPath" |
| Missing env/property | `IllegalStateException` — "Neither environment variable ... nor system property ... is set" |
| Simulation failure | Exception re-thrown from `SimulationService.simulate()`, caught by caller |
