# ISMA UI → .NET 10 + Avalonia 12 Migration Plan

## Overview

This document provides a comprehensive, multi-step migration plan for porting the ISMA desktop UI from Java/Kotlin/JavaFX to .NET 10 / C# / Avalonia 12. The plan prioritizes a clean architecture with clear separation of concerns, testable business logic, and feature parity with the original application.

## Target Architecture

```
┌─────────────────────────────────────────────────────┐
│  ISMA.App (Avalonia UI)                             │
│  ├── Views/          (AXAML + ViewModel code-behind)│
│  └── ViewModels/     (CommunityToolkit.Mvvm)        │
├─────────────────────────────────────────────────────┤
│  ISMA.ViewModels (Presentation Layer)               │
│  └── ViewModels/     (UI-framework agnostic)        │
├─────────────────────────────────────────────────────┤
│  ISMA.Domain (Domain Layer)                          │
│  ├── Models/         (Pure POCOs, no deps)          │
│  └── Services/       (Business logic interfaces)    │
├─────────────────────────────────────────────────────┤
│  ISMA.Infrastructure (Infrastructure Layer)          │
│  ├── Server/         (gRPC/HTTP client, server mgmt)│
│  ├── FileStorage/    (File I/O, preferences)        │
│  └── ChartViewer/    (Grin process launcher)        │
└─────────────────────────────────────────────────────┘
```

**Key architectural decisions:**

- **MVVM** with `CommunityToolkit.Mvvm` — no reactive UI frameworks
- **Microsoft.Extensions.DependencyInjection** — standard .NET DI
- **System.Text.Json** — JSON serialization
- **Grpc.Net.Client** — cross-platform gRPC client (no platform-specific epoll needed)
- **Avalonia.Controls.DataGrid** — for error list table
- **ICSharpCode.AvalonEdit** — for LISMA text editor (syntax highlighting, line numbers)
- **Custom Panel** — for blueprint editor canvas rendering
- **Tests** — xUnit + FluentAssertions for Domain and ViewModels

## Project Structure

```
isma-ui-dotnet/
├── ISMA.Domain/                     # Pure domain models + interfaces
│   ├── Models/
│   │   ├── SimulationResult.cs
│   │   ├── SimulationProgress.cs
│   │   ├── SimulationPoint.cs
│   │   ├── SimulationMetadata.cs
│   │   ├── MetricData.cs
│   │   ├── SimulationParameters.cs
│   │   ├── ErrorViewModel.cs
│   │   ├── BlueprintModel.cs
│   │   ├── BlueprintStateModel.cs
│   │   ├── BlueprintTransactionModel.cs
│   │   ├── BlueprintLoopTransactionModel.cs
│   │   ├── PreferencesModel.cs
│   │   ├── WindowPreferencesModel.cs
│   │   ├── DefaultFilesPreferencesModel.cs
│   │   └── InProgressSimulation.cs
│   ├── Services/
│   │   ├── ISimulationServerFacade.cs
│   │   ├── IProjectService.cs
│   │   ├── ISimulationService.cs
│   │   ├── ISimulationResultService.cs
│   │   ├── ISimulationParametersService.cs
│   │   ├── IModelErrorService.cs
│   │   ├── ILismaPdeService.cs
│   │   ├── IProjectFileService.cs
│   │   ├── IPreferencesProvider.cs
│   │   ├── ITextEditorFactory.cs
│   │   └── IEquationIndexProvider.cs
│   └── ISMA.Domain.csproj
├── ISMA.Infrastructure/             # gRPC, file I/O, external processes
│   ├── Server/
│   │   ├── SimulationServerManager.cs
│   │   ├── SimulationServerFacade.cs
│   │   ├── GrpcSimulationClient.cs
│   │   ├── GrpcLismaCompilerClient.cs
│   │   ├── HttpSimulationClient.cs
│   │   ├── BinaryFilePointProvider.cs
│   │   └── BinaryEquationIndexProvider.cs
│   ├── FileStorage/
│   │   ├── ProjectFileService.cs
│   │   └── PreferencesProvider.cs
│   ├── ChartViewer/
│   │   └── GrinProcessLauncher.cs
│   └── ISMA.Infrastructure.csproj
├── ISMA.ViewModels/                  # Presentation layer
│   ├── ProjectViewModel.cs
│   ├── LismaProjectViewModel.cs
│   ├── BlueprintProjectViewModel.cs
│   ├── SimulationParametersViewModel.cs
│   ├── SimulationServiceViewModel.cs
│   ├── SimulationResultViewModel.cs
│   ├── ErrorListViewModel.cs
│   ├── SettingsViewModels.cs
│   ├── MainWindowViewModel.cs
│   └── ISMA.ViewModels.csproj
├── ISMA.App/                        # Avalonia UI
│   ├── Views/
│   │   ├── MainWindow.axaml
│   │   ├── SettingsPanelView.axaml
│   │   ├── EditorTabPaneView.axaml
│   │   ├── IsmaTextEditorView.axaml
│   │   ├── BlueprintEditorView.axaml
│   │   ├── TasksPopOverView.axaml
│   │   ├── IsmaErrorListTableView.axaml
│   │   ├── SimulationProcessBarView.axaml
│   │   ├── SelectVariablesDialog.axaml
│   │   ├── EditArrowPopOverView.axaml
│   │   └── Settings/
│   │       ├── CauchyInitialsView.axaml
│   │       ├── MethodSettingsView.axaml
│   │       ├── EventDetectionView.axaml
│   │       └── ResultProcessingView.axaml
│   ├── ViewModels/                  # Thin view-specific viewmodels
│   ├── Converters/
│   ├── Controls/
│   │   ├── BlueprintCanvasPanel.cs      # Custom Panel for canvas
│   │   ├── StateBoxControl.axaml          # State box visual
│   │   ├── ArrowShape.cs                  # Arrow geometry helpers
│   │   └── PropertiesGrid.axaml           # Reusable property grid
│   ├── Services/
│   │   ├── TextEditorFactory.cs
│   │   └── EditorPlatformService.cs
│   ├── App.axaml
│   ├── App.xaml.cs
│   ├── Program.cs
│   └── ISMA.App.csproj
├── ISMA.Tests/                      # Test project
│   ├── Domain/
│   │   ├── SimulationParametersTests.cs
│   │   ├── BlueprintModelTests.cs
│   │   ├── BlueprintToLismaConversionTests.cs
│   │   └── PreferencesTests.cs
│   ├── ViewModels/
│   │   ├── SimulationServiceViewModelTests.cs
│   │   ├── ProjectViewModelTests.cs
│   │   └── SimulationParametersViewModelTests.cs
│   └── ISMA.Tests.csproj
└── isma-ui-dotnet.sln
```

---

## Migration Phases

### Phase 0: Foundation & Project Setup

**Goal:** Create the solution structure, configure build, DI, and verify the project compiles.

#### Steps

1. Create .NET 10 solution with 5 projects (Domain, Infrastructure, ViewModels, App, Tests)
2. Configure `Directory.Build.props` for common properties (nullable, analyzers, LangVersion)
3. Add NuGet packages to each project:
   - **Domain:** None (pure POCOs)
   - **Infrastructure:** `Grpc.Net.Client`, `Google.Protobuf`, `Grpc.Tools`, `System.IO.Pipelines`, `Microsoft.Extensions.Logging.Abstractions`
   - **ViewModels:** `CommunityToolkit.Mvvm`, `Microsoft.Extensions.DependencyInjection.Abstractions`
   - **App:** `Avalonia.Themes.Fluent`, `Avalonia.Controls.DataGrid`, `Avalonia.Desktop`, `CommunityToolkit.Mvvm`, `Microsoft.Extensions.DependencyInjection`, `ICSharpCode.AvalonEdit`, `System.Text.Json`
   - **Tests:** `xunit`, `FluentAssertions`, `Moq`, `Microsoft.NET.Test.Sdk`, `coverlet.collector`
4. Configure protobuf generation from `protobuf-contracts/simulation/`
5. Set up DI registration in `App.axaml.cs`
6. Create minimal `MainWindow` with empty content to verify the app runs

#### Acceptance Checklist

- [ ] Solution builds with `dotnet build` (no warnings as errors yet)
- [ ] `dotnet run` launches an empty Avalonia window
- [ ] All 5 projects compile
- [ ] gRPC stubs generate correctly from protobuf contracts
- [ ] DI container resolves `MainWindowViewModel`
- [ ] Tests project compiles (no tests yet)
- [ ] `.axaml` files compile (XAML validation passes)

---

### Phase 1: Domain Layer — Models & Interfaces

**Goal:** Implement all domain models and service interfaces. No UI dependencies.

#### Steps

1. **Simulation Models**
   - `SimulationPoint` — x, yForDe (double[]), rhs (double[][])
   - `SimulationProgress` — startTime, endTime, currentTime
   - `SimulationResult` — raw data container
   - `SimulationMetadata` — columnNames list
   - `MetricData` — startTime, endTime, derived simulationTime

2. **Simulation Parameters Models**
   - `CauchyInitials` — startTime, endTime, initialStep
   - `IntegrationMethodParameters` — selectedMethod, accuracy, isAccuracyInUse, isStableInUse, isParallelInUse, server, port
   - `EventDetectionParameters` — isEventDetectionInUse, isStepLimitInUse, gamma, lowBorder
   - `ResultSavingParameters` — savingTarget (enum: Memory, File)
   - `ResultProcessingParameters` — isSimplifyInUse, selectedSimplifyMethod, tolerance
   - `SimulationParameters` — composite of all above

3. **Project Models**
   - `LismaTextModel` — fullText, CodeRegion list (line ranges for error mapping)
   - `BlueprintModel` — main, init, states[], transactions[], loopTransactions[]
   - `BlueprintStateModel` — canvasPositionX, canvasPositionY, name, text
   - `BlueprintTransactionModel` — startStateName, endStateName, predicate, alias
   - `BlueprintLoopTransactionModel` — stateName, predicate, alias, text

4. **UI Models**
   - `ErrorViewModel` — row, position, fragmentName, message
   - `InProgressSimulation` — id, modelName, parameters, progress (0.0–1.0)
   - `CompletedSimulation` — id, modelName, cachedFile, columnNames, parameters, equationIndexProvider, metricData

5. **Preferences Models**
   - `WindowPreferences` — x, y, width, height, isMaximized
   - `DefaultFilesPreferences` — lastOpenedProjectPath (string[])
   - `PreferencesModel` — windowPreferences, defaultFilesPreferences

6. **Service Interfaces** (in `ISMA.Domain.Services`)
   - `ISimulationServerFacade` — compile, validate, highlight, runSimulation, monitorSimulation, downloadResult, cancelSimulation, getSimulationMethods, shutdown
   - `IProjectService` — createNew, createNewBlueprint, close, closeAll, projects collection, activeProject
   - `ISimulationService` — simulate, stopSimulation, trackingTasks collection
   - `ISimulationResultService` — commitResult, removeResult, showChart, exportToFile, trackingTasksResults collection
   - `ISimulationParametersService` — store, load, snapshot, commit, integrationMethods, simplifyMethods
   - `IModelErrorService` — errors collection, putErrorList
   - `ILismaPdeService` — validate (returns Success/Failure)
   - `IProjectFileService` — open, save, saveAs
   - `IPreferencesProvider` — load, save, preferences property
   - `ITextEditorFactory` — createTextEditor, disposeInstance
   - `IEquationIndexProvider` — getDifferentialEquationCount, getAlgebraicEquationCount, getDifferentialEquationCode, getAlgebraicEquationCode
   - `ISimulationResultReader` — results (IEnumerable<SimulationPoint>)

#### Acceptance Checklist

- [ ] All domain model classes compile with no dependencies on external libraries
- [ ] All interfaces are defined and consistent with original Kotlin interfaces
- [ ] `SimulationPoint` uses `double[]` and `double[][]` with proper `Equals`/`GetHashCode`
- [ ] `BlueprintModel` has an `Empty` static property matching original defaults
- [ ] `SaveTarget` enum has Memory and File values
- [ ] `SimulationParameters` is serializable with `System.Text.Json`
- [ ] CodeRegion class tracks startLine and endLine
- [ ] `dotnet build ISMA.Domain` produces zero errors
- [ ] Domain models pass JSON serialization round-trip test

---

### Phase 2: Infrastructure Layer — Server Communication

**Goal:** Implement gRPC client, HTTP client, server lifecycle management, and binary result reading.

#### Steps

1. **SimulationServerManager**
   - Resolve script path from `ISMA_SERVER_SCRIPT` env var or `isma.server.script` config key
   - Launch server as child process
   - Parse stdout for gRPC socket path and HTTP socket path
   - Register shutdown hook to stop server
   - Return `SocketPaths` tuple (grpc, http)

2. **GrpcSimulationClient**
   - Use `Grpc.Net.Client` with custom `HttpHandler` for Unix Domain Socket
   - Implement `RunSimulation`, `MonitorSimulation` (server streaming), `DownloadResult`, `CancelSimulation`, `GetSimulationMethods`
   - Cross-platform Unix socket support (no Linux-only epoll dependency)

3. **GrpcLismaCompilerClient**
   - `CompileModel` — returns modelId + errors + warnings
   - `ValidateModel` — returns errors + warnings
   - `HighlightSource` — returns list of SyntaxToken (start, length, kind)
   - `DeleteCompiledModel`

4. **HttpSimulationClient**
   - Download result file from server-provided URL to temp directory
   - Create cache directory `<temp>/isma-simulation-cache/`

5. **SimulationServerFacade**
   - Orchestrate all three clients
   - `warmup()` — start server, create clients
   - `shutdown()` — dispose all
   - `compileModel(source)` → `CompileResult`
   - `validateModel(source)` → `ValidationResult`
   - `runSimulation(params)` → `long simulationId`
   - `monitorSimulation(id)` → `IAsyncEnumerable<SimulationProgress>`
   - `downloadResultToCache(id)` → `CachedSimulationResult`
   - `cancelSimulation(id)`
   - `getSimulationMethods()` → `string[]`

6. **BinaryFilePointProvider**
   - Read binary result files using exchange-format library (port from Java)
   - Stream points as `IEnumerable<SimulationPoint>`
   - Parse metadata (column names, equation counts)

7. **BinaryEquationIndexProvider**
   - Parse column name prefixes (DE_, AE_, f) to derive equation info
   - Implement `IEquationIndexProvider`

8. **GrinProcessLauncher**
   - Resolve script from `ISMA_GRIN_SCRIPT` or config
   - Launch with args: `--result-file`, `--x-axis`, `--charts`

#### Acceptance Checklist

- [ ] `SimulationServerManager` starts and stops the server process correctly
- [ ] gRPC client connects via Unix Domain Socket
- [ ] `warmup()` successfully initializes all clients
- [ ] `compileModel()` returns CompileResult with modelId and errors
- [ ] `validateModel()` returns ValidationResult
- [ ] `highlightSource()` returns token list with correct positions and kinds
- [ ] `runSimulation()` returns a simulation ID
- [ ] `monitorSimulation()` streams progress updates
- [ ] `downloadResultToCache()` saves binary file to cache directory
- [ ] `cancelSimulation()` stops a running simulation
- [ ] `getSimulationMethods()` returns available method names
- [ ] `BinaryFilePointProvider` reads binary files and yields SimulationPoints
- [ ] `BinaryEquationIndexProvider` correctly parses column prefixes
- [ ] `GrinProcessLauncher` launches the chart viewer process
- [ ] Shutdown hook terminates server on process exit

---

### Phase 3: Infrastructure Layer — File Storage & Preferences

**Goal:** Implement file I/O for projects and preferences persistence.

#### Steps

1. **ProjectFileService**
   - Open: File picker → read file → determine type by extension (`.iscm2` → LISMA, `.scisma` → Blueprint, `.im` → legacy text)
   - Save: Write project content to file (LISMA text or JSON for blueprint)
   - Save As: Same as Save but with new file picker
   - Save All: Iterate all projects, save each

2. **PreferencesProvider**
   - Load `preferences.json` from app data directory
   - Save `WindowPreferences` and `DefaultFilesPreferences`
   - Restore last opened file paths on startup

3. **SimulationParametersService**
   - Manage default values (Cauchy: 0.0/10.0/0.1, Integration: accuracy 0.1, etc.)
   - `store()` → File picker → serialize to JSON
   - `load()` → File picker → deserialize from JSON
   - `snapshot()` → create `SimulationParameters` model
   - `commit(model)` → apply model to internal state
   - `integrationMethods` and `simplifyMethods` from server

#### Acceptance Checklist

- [ ] Opening `.iscm2` file creates a text project
- [ ] Opening `.scisma` file creates a blueprint project
- [ ] Opening `.im` file creates a legacy text project
- [ ] Saving a project writes correct format
- [ ] Preferences persist across app restarts
- [ ] Last opened files are restored on startup
- [ ] Window geometry is saved and restored
- [ ] Store Settings saves parameters to JSON
- [ ] Load Settings restores parameters from JSON
- [ ] Default values match original application

---

### Phase 4: ViewModels — Core Presentation Layer

**Goal:** Implement all ViewModels using CommunityToolkit.Mvvm. These are UI-framework agnostic and fully testable.

#### Steps

1. **MainWindowViewModel**
   - `Projects` observable collection of `IProjectViewModel`
   - `ActiveProject` property
   - `ShowSettings` property (bind to settings panel visibility)
   - Commands: `NewTextCommand`, `NewBlueprintCommand`, `OpenCommand`, `SaveCommand`, `SaveAllCommand`, `CloseCommand`, `CloseAllCommand`, `ExitCommand`, `CutCommand`, `CopyCommand`, `PasteCommand`, `VerifyCommand`, `RunCommand`, `StoreSettingsCommand`, `LoadSettingsCommand`

2. **ProjectViewModel** (base interface + implementations)
   - `IProjectViewModel` — Name, File, EditorContent (object), NameChanged event, Dispose
   - `LismaProjectViewModel` — LISMA text content, data provider integration
   - `BlueprintProjectViewModel` — Blueprint model, convertToLisma integration

3. **SimulationServiceViewModel**
   - `TrackingTasks` observable collection of `InProgressSimulationViewModel`
   - `Simulate()` — orchestrates full simulation flow:
     1. Snapshot parameters from `SimulationParametersViewModel`
     2. Get active project source
     3. Call `serverFacade.compileModel()`
     4. Report errors to `ModelErrorService`
     5. Call `serverFacade.runSimulation()`
     6. Monitor progress → update `InProgressSimulationViewModel.progress`
     7. Download result → create `CompletedSimulationViewModel`
     8. Commit to `SimulationResultService`
   - `StopSimulation()` — cancel running simulation

4. **SimulationResultViewModel**
   - `TrackingTasksResults` observable collection
   - `CommitResult()` — add completed simulation
   - `RemoveResult()` — remove from collection
   - `ShowChart()` — open axis picker, launch Grin
   - `ExportToFile()` — async CSV export

5. **SimulationParametersViewModel**
   - `CauchyInitials` — StartTime, EndTime, Step (double, INPC)
   - `IntegrationMethod` — SelectedMethod, Accuracy, IsAccuracyInUse, IsStableInUse, IsParallelInUse, Server, Port
   - `EventDetection` — IsEventDetectionInUse, IsStepLimitInUse, Gamma, LowBorder
   - `ResultSaving` — SavingTarget (enum)
   - `ResultProcessing` — IsSimplifyInUse, SelectedSimplifyMethod, Tolerance
   - `IntegrationMethods` — observable collection of strings
   - Snapshot/commit pattern

6. **ErrorListViewModel**
   - `Errors` observable collection of `ErrorViewModel`
   - `PutErrorList()` — replace collection

7. **Settings ViewModels** (derived from SimulationParametersViewModel)
   - `CauchyInitialsViewModel` — bound to CauchyInitials properties
   - `MethodSettingsViewModel` — bound to IntegrationMethod properties + method list
   - `EventDetectionViewModel` — bound to EventDetection properties
   - `ResultProcessingViewModel` — bound to ResultProcessing properties

8. **InProgressSimulationViewModel**
   - `Id`, `ModelName`, `Parameters`
   - `Progress` (0.0–1.0, INPC)
   - `CanAbort` property

9. **CompletedSimulationViewModel**
   - `Id`, `ModelName`, `Parameters`, `CachedFile`, `ColumnNames`, `MetricData`
   - `EquationIndexProvider` for column name access

#### Acceptance Checklist

- [ ] All ViewModels inherit from `ObservableObject` (CommunityToolkit.Mvvm)
- [ ] All collections use `ObservableCollection<T>` or `ObservableCollection`
- [ ] All commands use `[RelayCommand]` or `ICommand`
- [ ] `MainWindowViewModel` has all 14 menu/toolbar commands
- [ ] `SimulationServiceViewModel.Simulate()` implements full flow
- [ ] Progress updates propagate via INPC from background task
- [ ] `SimulationParametersViewModel` has all 5 parameter sections
- [ ] `ErrorListViewModel` supports clearing and repopulating
- [ ] `CompletedSimulationViewModel` exposes column names for axis picker
- [ ] ViewModels have no Avalonia dependencies
- [ ] `dotnet build ISMA.ViewModels` produces zero errors

---

### Phase 5: Tests — Domain & ViewModels

**Goal:** Write unit tests for all business logic. UI layer is not tested directly (it's a thin presentation layer).

#### Steps

1. **Domain Tests** (`ISMA.Tests.Domain`)
   - `SimulationParametersTests` — serialization round-trip, default values, snapshot/commit
   - `BlueprintModelTests` — Empty model defaults, state/transaction creation
   - `BlueprintToLismaConversionTests` — convertToLisma produces correct LISMA text for:
     - Empty blueprint (Main + init only)
     - Single state with content
     - Multiple states with regular transitions
     - Loop transitions (pseudo-state pattern)
     - Multiple transitions to same target (merge behavior)
   - `PreferencesTests` — load/save preferences, window geometry persistence
   - `SimulationPointTests` — Equals/GetHashCode for arrays
   - `CodeRegionTests` — line range tracking

2. **ViewModel Tests** (`ISMA.Tests.ViewModels`)
   - `SimulationServiceViewModelTests` — simulate flow (mock server facade):
     - Compilation errors prevent simulation
     - Successful simulation creates completed result
     - Progress updates during simulation
     - Cancellation stops simulation
   - `ProjectViewModelTests` — create/close projects, name changes, file save/load
   - `SimulationParametersViewModelTests` — snapshot captures current values, commit applies values
   - `ErrorListViewModelTests` — errors are cleared and repopulated
   - `MainWindowViewModelTests` — commands exist and can be invoked

#### Acceptance Checklist

- [ ] All domain tests pass (`dotnet test ISMA.Tests`)
- [ ] Blueprint-to-LISMA conversion tests cover all scenarios from UX spec
- [ ] Simulation flow tests mock the server facade
- [ ] ViewModel tests verify command execution
- [ ] Test coverage for simulation parameters serialization
- [ ] Tests run in CI without external dependencies (all mocking)
- [ ] `dotnet test` produces 100% green

---

### Phase 6: UI — Application Shell, Menu, Toolbar, and Simple Views

**Goal:** Build the main window layout, menu bar, toolbar, settings panel, and error list. These are the simplest UI components.

#### Steps

1. **App.axaml** — Global styling, Fluent theme, resource dictionaries
2. **MainWindow.axaml** — BorderPane layout:
   - Top: MenuBar + ToolBar
   - Center: ContentControl for EditorTabPane
   - Right: ContentControl for SettingsPanel (bind IsVisible)
   - Bottom: ErrorList + SimulationProcessBar

3. **Menu Bar** (`IsmaMenuBarView`)
   - File menu: New Text (Ctrl+N), New Statechart (Ctrl+B), Open (Ctrl+O), Save (Ctrl+S), Save As, Save All, Close, Close All, Exit (Ctrl+W)
   - Edit menu: Cut (Ctrl+X), Copy (Ctrl+C), Paste (Ctrl+V)
   - Simulation menu: Verify (Ctrl+F4), Run (Ctrl+F5), Store Settings, Load Settings
   - All commands bound to `MainWindowViewModel`

4. **Tool Bar** (`IsmaToolBarView`)
   - Icon buttons with Material Design icons (use Avalonia icon pack or inline SVG)
   - Same commands as menu bar
   - Separators between logical groups

5. **Settings Panel** (`SettingsPanelView` + sub-views)
   - TabControl with 4 tabs: Initials, Integration, Event Detection, Result Processing
   - Each tab uses `PropertiesGrid` for label+control rows
   - Bind to `SimulationParametersViewModel`

6. **PropertiesGrid** (custom reusable control)
   - Grid layout: label on left, control on right
   - Support for Double, Integer, String, Boolean, Enum, ComboBox
   - Two-way binding support

7. **Error List** (`IsmaErrorListTableView`)
   - `DataGrid` bound to `ErrorListViewModel.Errors`
   - Columns: Row (5%), Position (5%), Fragment (10%), Message (80%)
   - Auto-resize columns

8. **Simulation Process Bar** (`SimulationProcessBarView`)
   - Play button (RunCommand) + Tasks button (toggle TasksPopOver)

#### Acceptance Checklist

- [ ] MainWindow displays with all regions (top, center, right, bottom)
- [ ] Menu bar shows all items with correct shortcuts
- [ ] Toolbar shows all buttons with icons
- [ ] Commands execute ViewModel methods (no code-behind logic)
- [ ] Settings panel shows all 4 sections with correct controls
- [ ] PropertiesGrid renders label+control rows correctly
- [ ] Error list DataGrid displays errors with correct column widths
- [ ] Process bar shows play button and tasks button
- [ ] Settings panel visibility toggles correctly
- [ ] Window minimum size is 500×600
- [ ] Window title is "ISMA"
- [ ] No business logic in any code-behind file

---

### Phase 7: UI — Project Tabs, Text Editor, and File Operations

**Goal:** Implement tab-based project management, LISMA text editor with syntax highlighting, and file open/save.

#### Steps

1. **Editor Tab Pane** (`EditorTabPaneView`)
   - `TabControl` bound to `MainWindowViewModel.Projects`
   - Each tab shows project name and editor content
   - Tab close → `CloseCommand` for that project
   - Tab selection → sets `ActiveProject`

2. **LISMA Text Editor** (`IsmaTextEditorView`)
   - Use `ICSharpCode.AvalonEdit` TextEditor control
   - Monospace font (Consolas/Courier New)
   - Line numbers enabled
   - Syntax highlighting via `TextEditor.SyntaxHighlighting`
   - Syntax highlighting definition for LISMA:
     - Keywords → orange, bold
     - Comments → gray, italic
     - Numbers → blue
   - Syntax computed server-side via `highlightSource()` gRPC call
   - Apply highlighting spans on text change with debouncing
   - Cut/Copy/Paste via `EditorPlatformService`

3. **EditorPlatformService**
   - Expose events for Cut/Copy/Paste
   - Propagate clipboard commands from menu/toolbar to focused editor

4. **TextEditorFactory**
   - Create `TextEditor` instances for blueprint state text editing
   - Dispose instances on tab close
   - Wire up text change callbacks

5. **File Operations**
   - Open dialog with filters: `.iscm2`, `.scisma`, `.im`, all files
   - Save dialog with type-specific filter
   - Save All iterates all projects
   - Unsaved project indicator in tab title

6. **Window State Persistence**
   - Save window geometry on closing
   - Restore on startup
   - Save last opened file paths

#### Acceptance Checklist

- [ ] Multiple tabs can be opened simultaneously
- [ ] Tab titles show project names (filename if saved, "New project" otherwise)
- [ ] Closing a tab disposes the project
- [ ] AvalonEdit renders LISMA source code correctly
- [ ] Line numbers display on left margin
- [ ] Syntax highlighting applies server-computed spans
- [ ] Keywords appear orange and bold
- [ ] Comments appear gray and italic
- [ ] Numbers appear blue
- [ ] Cut/Copy/Paste work via menu, toolbar, and keyboard
- [ ] Open dialog filters files correctly by extension
- [ ] Save writes correct format for each project type
- [ ] Unsaved changes are indicated (e.g., asterisk in tab title)
- [ ] Window geometry persists across restarts
- [ ] Last opened files restore on startup

---

### Phase 8: UI — Blueprint Editor (Complex Canvas)

**Goal:** Implement the visual statechart editor with canvas rendering, state boxes, transition arrows, and all interaction modes. This is the most complex UI component.

#### Steps

1. **BlueprintCanvasPanel** (custom `Panel`)
   - Override `MeasureOverride` and `ArrangeOverride` for absolute positioning
   - Render states and arrows via `OnRender` (direct drawing for performance)
   - Handle pointer events for drag, click, and double-click
   - Scroll support via `ScrollViewer` wrapper

2. **State Box Rendering**
   - Rounded rectangles (CornerRadius = 20)
   - Main state: LightGreen fill, fixed position (20, 10)
   - Init state: LightBlue fill, fixed position (10, 100)
   - User states: Coral fill, draggable
   - Text label (Arial 16pt) centered in box
   - Inline name editing: TextBox overlay on single-click (200ms delay to distinguish from drag)
   - Double-click → open text editor tab

3. **Transition Arrow Rendering**
   - Straight line from source state center to target state center
   - Offset endpoints to avoid overlapping state borders (offset = 10px)
   - Arrowhead polygon (14×14 isosceles triangle)
   - Label (alias or predicate) offset perpendicular from line midpoint
   - Geometry updates when state positions change

4. **Loop Arrow Rendering**
   - Circle (radius 40) above the state
   - Arrowhead pointing back to the state
   - Label to the right of the circle
   - Double-click arrowhead → open text editor tab with "{stateName} (loop)"

5. **Edit Arrow PopOver** (`EditArrowPopOverView`)
   - Floating panel with DropShadow effect
   - Two text fields: Alias (optional) and Predicate
   - Bidirectional binding to arrow properties
   - Auto-dismiss on mouse exit

6. **Interaction Modes** (managed by `BlueprintEditorViewModel`)
   - Default mode: drag states, click to edit names, double-click for text
   - Add transition mode: click source, click target (or same state for loop)
   - Remove state mode: click state to delete
   - Remove transition mode: click arrow to delete
   - Mutually exclusive modes via toggle buttons

7. **Blueprint Editor Toolbar**
   - Bottom toolbar with 4 buttons: New State, New Transition, Remove State, Remove Transition
   - Toggle buttons with mode-specific text

8. **State Box Control**
   - Visual representation of a state (rendered in canvas)
   - Hit testing for pointer events
   - Name editing state machine (label ↔ TextBox)

9. **NameChangingMonitor** (in Domain or ViewModel layer)
   - Track registered state names
   - Prevent duplicate names
   - Auto-increment default name counter ("New state N")
   - Rollback on duplicate during name edit

10. **Blueprint Editor ViewModel**
    - `States` collection
    - `Transactions` collection
    - `LoopTransactions` collection
    - `CurrentMode` property
    - `AddState()`, `AddTransition(source, target)`, `RemoveState()`, `RemoveTransition()`
    - `GetBlueprintModel()` — serialize canvas to model
    - `SetBlueprintModel(model)` — restore from model

11. **Blueprint-to-LISMA Conversion** (in Domain layer)
    - Main state text → base content
    - Regular transitions → `state "key" { ... } from start1, start2, ...;`
    - Loop transitions → pseudo-state pattern
    - Merge transitions to same target with same predicate
    - Track CodeRegions for error mapping

#### Acceptance Checklist

- [ ] Canvas renders Main (green) and Init (blue) states at correct positions
- [ ] User states render as Coral rounded rectangles
- [ ] States are draggable with mouse
- [ ] Dragging updates arrow positions in real-time
- [ ] Arrow geometry connects state centers correctly
- [ ] Arrowhead rotates to match line angle
- [ ] Loop arrows render as circles with arrowheads
- [ ] New State button creates a state at (10, 200)
- [ ] New Transition mode: click two states → arrow created
- [ ] New Transition mode: click same state twice → loop arrow created
- [ ] Remove State mode: click state → state and arrows deleted
- [ ] Remove Transition mode: click arrow → arrow deleted
- [ ] Modes are mutually exclusive (toggle buttons)
- [ ] Inline name editing works (single-click, 200ms delay)
- [ ] Duplicate names are prevented
- [ ] Duplicate names rollback to previous name
- [ ] Double-click state → text editor tab opens
- [ ] Double-click loop arrowhead → text editor tab with "(loop)" suffix
- [ ] Edit Arrow PopOver opens on arrow click
- [ ] PopOver fields bind bidirectionally to arrow properties
- [ ] PopOver dismisses on mouse exit
- [ ] `GetBlueprintModel()` serializes canvas correctly
- [ ] `SetBlueprintModel()` restores canvas from model
- [ ] Blueprint-to-LISMA produces correct output for all scenarios
- [ ] Main/Init states can be removed in remove mode (replicating original behavior)
- [ ] Canvas scrolls when content exceeds viewport
- [ ] No state can be placed at negative coordinates

---

### Phase 9: UI — Tasks PopOver, Results, and Chart Viewer

**Goal:** Implement the Tasks PopOver, simulation result management, axis picker dialog, CSV export, and chart viewer integration.

#### Steps

1. **Tasks PopOver** (`TasksPopOverView`)
   - Two sections: "In Progress" and "Completed"
   - In-progress items: task label, progress bar, abort button
   - Completed items: task label, Show button, Export button, Remove button, Details chevron
   - Details chevron → nested PopOver with simulation metadata

2. **Select Variables Dialog** (`SelectVariablesDialog`)
   - X-axis ComboBox (pre-select TIME)
   - Y-axis ListView with checkboxes (multi-select)
   - Select All / Unselect All buttons
   - Ok / Close buttons

3. **Simulation Result Service**
   - `CommitResult()` — add to completed list
   - `RemoveResult()` — remove from list
   - `ShowChart(result)` — open axis picker, launch Grin with selected axes
   - `ExportToFile(result, filePath)` — async CSV export:
     - Header: `x, [DE columns], [AE columns], f0, f1, ...`
     - Stream points from `BinaryFilePointProvider`
     - Write to buffered writer

4. **Grin Process Launcher Integration**
   - Launch Grin with binary file path, X-axis, and Y-axis arguments
   - Resolve script from config

5. **CSV Export**
   - File picker for output path (filter `*.csv`)
   - Background task for non-blocking export
   - Use `BinaryFilePointProvider` to stream points

#### Acceptance Checklist

- [ ] Tasks PopOver opens from Tasks button
- [ ] In-progress section shows running simulations with progress bars
- [ ] Abort button cancels the simulation
- [ ] Completed section shows finished simulations
- [ ] Show button opens axis picker dialog
- [ ] Select Variables dialog lists all columns
- [ ] X-axis ComboBox pre-selects TIME
- [ ] Y-axis checkboxes support multi-select
- [ ] Select All / Unselect All work correctly
- [ ] Ok launches Grin chart viewer
- [ ] Export button opens file picker
- [ ] CSV export writes correct format
- [ ] CSV export runs asynchronously (non-blocking)
- [ ] Details PopOver shows simulation metadata
- [ ] Remove button removes entry from completed list
- [ ] Progress bar updates in real-time during simulation
- [ ] Tasks are removed from in-progress after completion or cancellation

---

### Phase 10: Polish, Integration Testing, and Documentation

**Goal:** Final polish, cross-platform testing, and documentation.

#### Steps

1. **Styling & Theming**
   - Consistent color palette matching original (Main=LightGreen, Init=LightBlue, User=Coral)
   - Fluent theme integration
   - Custom accent colors if needed

2. **Keyboard Shortcuts**
   - Verify all shortcuts work (Ctrl+N, Ctrl+B, Ctrl+O, Ctrl+S, Ctrl+W, Ctrl+X, Ctrl+C, Ctrl+V, Ctrl+F4, Ctrl+F5)
   - Cross-platform: Cmd on macOS

3. **Window Management**
   - Minimum size enforcement (500×600)
   - State persistence (position, size, maximized)
   - Multiple monitor support

4. **Error Handling**
   - Server not found → user-friendly error dialog
   - gRPC errors → displayed in error list
   - File I/O errors → error dialogs
   - Simulation failures → error messages in tasks

5. **Performance**
   - Blueprint canvas rendering performance (test with 50+ states)
   - Text editor responsiveness with large files
   - CSV export progress feedback

6. **Cross-Platform Testing**
   - Linux (primary development platform)
   - Windows (gRPC Unix socket support)
   - macOS (Unix socket support)

7. **Documentation**
   - Update `docs/dotnet-ui-migration/` with final architecture notes
   - Add README to `isma-ui-dotnet/` solution
   - Document build and run instructions
   - Document testing approach

#### Acceptance Checklist

- [ ] All keyboard shortcuts work correctly
- [ ] Window respects minimum size
- [ ] Window state persists across restarts
- [ ] Server connection errors are handled gracefully
- [ ] Compilation errors display in error list
- [ ] Blueprint canvas handles 50+ states without lag
- [ ] Text editor handles 1000+ line files smoothly
- [ ] CSV export completes without freezing UI
- [ ] App runs on Linux
- [ ] App runs on Windows
- [ ] App runs on macOS
- [ ] All tests pass
- [ ] Documentation is complete

---

## Dependency Mapping Summary

| Original (Java/Kotlin) | Replacement (.NET/C#) | Notes |
|------------------------|----------------------|-------|
| JavaFX Application | Avalonia `Application` | Entry point, lifecycle |
| JavaFX Stage/Scene | Avalonia `Window` | Main window |
| JavaFX BorderPane | Avalonia `BorderPane` or `Grid` | Layout |
| JavaFX TabPane | Avalonia `TabControl` | Tab management |
| JavaFX TableView | Avalonia `DataGrid` (NuGet) | Error list table |
| JavaFX CodeArea (fxmisc.richtext) | AvalonEdit `TextEditor` | LISMA text editing |
| JavaFX Pane (canvas) | Avalonia `Panel` (custom) | Blueprint canvas |
| JavaFX Rectangle | Avalonia `Rectangle` + `CornerRadius` | State boxes |
| JavaFX Line | Avalonia `Path` with `LineGeometry` | Arrow shafts |
| JavaFX Polygon | Avalonia `Path` with `PathGeometry` | Arrowheads |
| JavaFX Circle | Avalonia `Ellipse` | Loop arrows |
| JavaFX VBox/HBox | Avalonia `StackPanel` | PopOver layout |
| JavaFX ToolBar | Avalonia `StackPanel` (horizontal) | Bottom toolbar |
| JavaFX PopOver | Avalonia `Popup` or custom overlay | Edit popover, tasks |
| JavaFX DropShadow | Avalonia `BoxShadow` on `Border` | PopOver shadow |
| Koin DI | Microsoft.Extensions.DependencyInjection | Dependency injection |
| TornadoFX ViewModel | CommunityToolkit.Mvvm `ObservableObject` | MVVM base class |
| JavaFX Properties | CommunityToolkit.Mvvm `[ObservableProperty]` | Property change notification |
| Kotlin Coroutines | C# `async`/`await` + `Task` | Concurrency |
| Kotlin Flow | C# `IAsyncEnumerable` / `ObservableCollection` | Reactive collections |
| gRPC-Java (Netty Epoll) | Grpc.Net.Client (cross-platform) | gRPC client |
| Ktor CIO HTTP | `HttpClient` (built-in) | HTTP client |
| kotlinx.serialization | System.Text.Json | JSON serialization |
| JavaFX ObservableList | `ObservableCollection<T>` | Reactive collections |
| JavaFX FileChooser | Avalonia `FileDialog` | File dialogs |
| JavaFX Preferences | File-based JSON storage | Preferences persistence |

---## Business Features Inventory

All features from the original application that must be implemented:

| # | Feature | Phase | Complexity |
|---|---------|-------|------------|
| 1 | Multi-project editing (tabs) | 6 | Low |
| 2 | LISMA text editing with syntax highlighting | 7 | Medium |
| 3 | Remote syntax highlighting (server-driven) | 7 | Medium |
| 4 | Visual statechart (blueprint) editing | 8 | High |
| 5 | State creation, drag, rename | 8 | Medium |
| 6 | Transition arrow creation and management | 8 | High |
| 7 | Loop transition arrows | 8 | High |
| 8 | Edit arrow PopOver (alias/predicate) | 8 | Medium |
| 9 | Inline state name editing | 8 | Medium |
| 10 | Blueprint-to-LISMA conversion | 5 (ViewModels) | High |
| 11 | Model compilation (via gRPC) | 2 | Low |
| 12 | Model validation (Verify) | 6 | Low |
| 13 | Simulation execution (via gRPC) | 2 | Low |
| 14 | Real-time progress monitoring | 9 | Medium |
| 15 | Simulation cancellation | 9 | Low |
| 16 | Result download and caching | 2 | Low |
| 17 | Error list display | 6 | Low |
| 18 | Simulation parameters configuration | 6 | Low |
| 19 | Parameter presets (store/load JSON) | 3 | Low |
| 20 | Chart visualization (Grin process) | 9 | Low |
| 21 | Variable axis selection dialog | 9 | Medium |
| 22 | CSV export of results | 9 | Medium |
| 23 | Window state persistence | 7 | Low |
| 24 | Menu bar and toolbar commands | 6 | Low |
| 25 | Keyboard shortcuts | 10 | Low |
| 26 | Clipboard propagation (cut/copy/paste) | 7 | Low |
| 27 | Tasks PopOver (in-progress + completed) | 9 | Medium |
| 28 | State content editing (double-click → text tab) | 7/8 | Medium |
| 29 | Name uniqueness enforcement | 8 | Low |
| 30 | Parallel execution settings | 6 | Low |
| 31 | Result simplification settings | 6 | Low |
