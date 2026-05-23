# ISMA UI → .NET 10 + Avalonia 12 Migration Plan

## Overview

This document provides a comprehensive, multi-step migration plan for porting the ISMA desktop UI from Java/Kotlin/JavaFX to .NET 10 / C# / Avalonia 12. The plan prioritizes a clean architecture with clear separation of concerns, testable business logic, and feature parity with the original application.

## Target Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│  ISMA.App (Avalonia 12 UI)                                      │
│  ├── Views/          (AXAML + InitializeComponent code-behind)  │
│  ├── Controls/       (Reusable controls: PropertiesGrid, etc.)  │
│  ├── Services/       (UI-specific services: TextEditorFactory)  │
│  └── App.axaml / Program.cs                                    │
├─────────────────────────────────────────────────────────────────┤
│  ISMA.ViewModels (Presentation Layer — UI-framework agnostic)   │
│  ├── ViewModels/             (All MVVM viewmodels)             │
│  ├── Services/               (Presentation services)           │
│  └── Converters/             (IValueConverter implementations) │
├─────────────────────────────────────────────────────────────────┤
│  ISMA.Domain (Domain Layer — pure, no dependencies)             │
│  ├── Models/         (Pure POCOs + DTOs)                       │
│  ├── Conversion/     (BlueprintToLismaConverter — pure algo)   │
│  └── Contracts/      (Service interfaces for infrastructure)   │
├─────────────────────────────────────────────────────────────────┤
│  ISMA.Infrastructure (Infrastructure Layer)                     │
│  ├── Server/         (gRPC/HTTP client, server mgmt)           │
│  ├── FileStorage/    (File I/O, preferences)                   │
│  └── ChartViewer/    (Grin process launcher)                   │
├─────────────────────────────────────────────────────────────────┤
│  ISMA.Tests (xUnit + FluentAssertions + Moq)                    │
│  ├── Domain/         (Model + conversion tests)                │
│  └── ViewModels/     (ViewModel + service tests)               │
└─────────────────────────────────────────────────────────────────┘
```

**Dependency graph:**
```
ISMA.App → ISMA.ViewModels → ISMA.Domain
ISMA.App → ISMA.Infrastructure → ISMA.Domain
ISMA.ViewModels → ISMA.Domain
ISMA.Tests → ISMA.Domain, ISMA.ViewModels (mocking ISMA.Infrastructure)

Dependency direction:
  App depends on ViewModels + Infrastructure
  ViewModels depends on Domain (interfaces)
  Infrastructure depends on Domain (interfaces + models)
  Tests depends on Domain + ViewModels (Infrastructure is mocked)

No circular dependencies allowed.
```

**Key architectural decisions:**

- **MVVM** with `CommunityToolkit.Mvvm` — `[ObservableProperty]`, `[RelayCommand]`, `[INotifyPropertyChanged]`
- **Microsoft.Extensions.DependencyInjection** — standard .NET DI with `ServiceCollectionExtensions` pattern
- **System.Text.Json** — JSON serialization with `[JsonSerializable]` source generation
- **Grpc.Net.Client** — cross-platform gRPC client with UnixDomainSocket handler (no Linux-only epoll)
- **Avalonia.Controls.DataGrid** — for error list table
- **ICSharpCode.AvalonEdit** — for LISMA text editor (syntax highlighting, line numbers)
- **Custom Panel** — for blueprint editor canvas rendering (OnRender override)
- **ViewLocator pattern** — automatic View resolution from ViewModel type
- **Avalonia 12 features**: `x:DataType` compiled bindings, `IsVisible` (not `Visibility`), `BoxShadow`, Container Queries, Control Themes
- **Tests**: xUnit + FluentAssertions + Moq for Domain and ViewModels

## Project Structure

```
isma-ui-dotnet/
├── ISMA.Domain/                              # Pure domain models + DTOs + interfaces
│   ├── Models/
│   │   ├── SimulationPoint.cs                # x, yForDe (double[]), rhs (double[][])
│   │   ├── SimulationProgress.cs             # startTime, endTime, currentTime
│   │   ├── SimulationMetadata.cs             # columnNames (List<string>)
│   │   ├── MetricData.cs                     # startTime, endTime, simulationTime
│   │   ├── CauchyInitials.cs                 # startTime, endTime, initialStep
│   │   ├── IntegrationMethodParameters.cs    # selectedMethod, accuracy, isAccuracyInUse,
│   │   │                                   #   isStableAllowedInUse, isStableInUse,
│   │   │                                   #   isParallelInUse, server, port
│   │   ├── EventDetectionParameters.cs       # isEventDetectionInUse, isStepLimitInUse,
│   │   │                                   #   gamma, lowBorder
│   │   ├── ResultSavingParameters.cs         # savingTarget (SaveTarget enum)
│   │   ├── ResultProcessingParameters.cs     # isSimplifyInUse, selectedSimplifyMethod, tolerance
│   │   ├── SimulationParameters.cs           # composite: cauchyInitials, eventDetection,
│   │   │                                   #   integrationMethod, resultSaving
│   │   ├── LismaTextModel.cs                 # fullText, regions (List<CodeRegion>)
│   │   ├── CodeRegion.cs                     # name, startLine, endLine + fragmentNameByIndex()
│   │   ├── BlueprintModel.cs                 # main, init, states[], transactions[], loopTransactions[]
│   │   ├── BlueprintStateModel.cs            # canvasPositionX, canvasPositionY, name, text
│   │   ├── BlueprintTransactionModel.cs      # startStateName, endStateName, predicate, alias
│   │   ├── BlueprintLoopTransactionModel.cs  # stateName, predicate, alias, text
│   │   ├── ErrorInfo.cs                      # row, position, fragmentName, message
│   │   ├── InProgressSimulation.cs           # id, modelName, parameters, progress (0.0–1.0)
│   │   ├── CompletedSimulation.cs            # id, modelName, equationIndexProvider, metricData,
│   │   │                                   #   parameters, cachedFile, cachedColumnNames
│   │   ├── WindowPreferences.cs              # x, y, width, height, isMaximized
│   │   ├── DefaultFilesPreferences.cs        # lastOpenedProjectPath (string[])
│   │   └── Preferences.cs                    # windowPreferences, defaultFilesPreferences
│   ├── Dtos/                                 # DTOs for server communication
│   │   ├── CompileResult.cs                  # modelId, errors (List<CompilationError>), warnings
│   │   ├── ValidationResult.cs               # errors, warnings
│   │   ├── CompilationError.cs               # row, column, message
│   │   ├── SyntaxTokenDto.cs                 # start, length, kind (SyntaxTokenKind enum)
│   │   ├── SyntaxTokenKind.cs                # Unspecified, Keyword, Comment, Number, Text
│   │   ├── CachedSimulationResult.cs         # file (FileInfo), columnNames
│   │   ├── RunSimulationParams.cs            # startTime, endTime, initialStep, methodName,
│   │   │                                   #   accuracy, isAccuracyInUse, isStabilityControlInUse,
│   │   │                                   #   compiledModelId, eventDetectionGamma, eventDetectionLowBorder
│   │   └── SocketPaths.cs                    # grpc, http (Unix socket paths)
│   ├── Conversion/
   │   │   └── BlueprintToLismaConverter.cs      # BlueprintModel → LismaTextModel (pure algorithm)
   │   │       ├── ConvertToLisma()              # Main flow: main text → transactions → loops
   │   │       ├── CreateTransactionKey()        # "{targetStateName} ({predicate})"
   │   │       └── StateBlockModel.cs            # Helper for state block generation
   │   ├── Results/                              # Sealed result types for domain operations
   │   │   └── LismaPdeTranslationResult.cs      # Sealed interface: SuccessTranslation / FailedTranslation
   │   ├── Contracts/                            # Service interfaces (implemented in Infrastructure)
│   │   ├── ISimulationServerFacade.cs        # compile, validate, highlight, run, monitor,
│   │   │                                   #   download, cancel, getMethods, shutdown
│   │   ├── IEquationIndexProvider.cs         # getDifferentialEquationCount, getAlgebraicEquationCount,
│   │   │                                   #   getDifferentialEquationCode, getAlgebraicEquationCode
│   │   ├── ISimulationResultReader.cs        # Results (IEnumerable<SimulationPoint>)
│   │   ├── ISyntaxHighlighter.cs             # Highlight(source) → List<SyntaxTokenDto>
│   │   └── ITextEditorFactory.cs             # CreateTextEditor(text, onTextChanged), DisposeInstance
│   └── ISMA.Domain.csproj
├── ISMA.Infrastructure/                      # gRPC, file I/O, external processes
│   ├── Server/
│   │   ├── SimulationServerManager.cs        # Process lifecycle, socket path parsing
│   │   ├── SimulationServerFacade.cs         # Orchestration: compile, validate, run, monitor...
│   │   ├── GrpcSimulationClient.cs           # gRPC client for SimulationServiceGrpc
│   │   ├── GrpcLismaCompilerClient.cs        # gRPC client for LismaCompilerServiceGrpc
│   │   ├── HttpSimulationClient.cs           # HTTP client for binary result downloads
│   │   ├── BinaryFilePointProvider.cs        # Read binary results → IEnumerable<SimulationPoint>
│   │   └── BinaryEquationIndexProvider.cs    # Parse column prefixes (DE_, AE_, f)
│   ├── FileStorage/
│   │   ├── ProjectFileService.cs             # Open/save projects by extension
│   │   └── PreferencesProvider.cs            # preferences.json persistence
│   ├── ChartViewer/
│   │   └── GrinProcessLauncher.cs            # Launch external chart viewer process
│   └── ISMA.Infrastructure.csproj
├── ISMA.ViewModels/                          # Presentation layer (UI-framework agnostic)
│   ├── ViewModels/
│   │   ├── MainWindowViewModel.cs            # Projects, ActiveProject, 14 commands
│   │   ├── IProjectViewModel.cs              # Name, File, EditorContent, NameChanged, Dispose
│   │   ├── LismaProjectViewModel.cs          # LISMA text content, data provider bridge
│   │   ├── BlueprintProjectViewModel.cs      # Blueprint model, convertToLisma integration
│   │   ├── SimulationParametersViewModel.cs  # 5 parameter sections with snapshot/commit
│   │   ├── SimulationServiceViewModel.cs     # Simulate(), StopSimulation(), TrackingTasks
│   │   ├── SimulationResultViewModel.cs      # CommitResult(), ShowChart(), ExportToFile()
│   │   ├── ErrorListViewModel.cs             # Errors collection, PutErrorList()
│   │   ├── CauchyInitialsViewModel.cs        # Bound to CauchyInitials properties
   │   ├── MethodSettingsViewModel.cs        # Bound to IntegrationMethod properties + method list
   │   ├── EventDetectionViewModel.cs        # Bound to EventDetection properties
   │   └── ResultProcessingViewModel.cs      # Bound to ResultProcessing properties
│   │   ├── BlueprintEditorViewModel.cs       # States, Transactions, Modes, Add/Remove operations
│   │   ├── TasksPopOverViewModel.cs          # InProgress + Completed sections
│   │   ├── SelectVariablesDialogViewModel.cs # XAxis, YAxis selection for chart viewer
│   │   ├── EditArrowPopOverViewModel.cs      # Alias + Predicate for arrow editing
│   │   └── InProgressSimulationViewModel.cs  # Id, ModelName, Progress, CanAbort
 │   ├── Models/                               # Presentation-specific models (NamedPickerItem, etc.)
   │   │   └── NamedPickerItem.cs                # Generic item for axis picker dialog
   │   ├── Services/                             # Presentation services (UI orchestration, no Avalonia deps)
│   │   ├── SimulationService.cs              # Orchestrates: snapshot → compile → run → monitor → download
│   │   ├── SimulationResultService.cs        # Manages completed results, CSV export, chart launch
│   │   ├── SimulationParametersService.cs    # Parameter state management, store/load
│   │   ├── ProjectService.cs                 # Project collection management
│   │   ├── ModelErrorService.cs              # Error list management
│   │   ├── LismaPdeService.cs                # LISMA validation → Success/Failure
│   │   └── SyntaxHighlighterService.cs       # Delegates to server, maps token kinds
│   ├── Converters/
│   │   ├── DoubleConverter.cs                # string ↔ double (with normalization)
│   │   ├── IntegerConverter.cs               # string ↔ int (with normalization)
│   │   ├── ProgressToPercentConverter.cs     # 0.0–1.0 → percentage
│   │   ├── BoolToVisibilityConverter.cs      # bool → IsVisible binding
│   │   └── SaveTargetConverter.cs            # SaveTarget enum → display string
│   └── ISMA.ViewModels.csproj
├── ISMA.App/                                 # Avalonia 12 UI
│   ├── Views/
│   │   ├── MainWindow.axaml                  # Main window: MenuBar, ToolBar, TabControl,
│   │   │                                   #   SettingsPanel, ErrorList, ProcessBar
│   │   ├── SettingsPanelView.axaml           # 4-tab settings panel
│   │   ├── EditorTabPaneView.axaml           # TabControl bound to Projects collection
│   │   ├── IsmaTextEditorView.axaml          # AvalonEdit TextEditor with syntax highlighting
│   │   ├── BlueprintEditorView.axaml         # Canvas panel + toolbar + edit popover
│   │   ├── TasksPopOverView.axaml            # In-progress + Completed sections
│   │   ├── IsmaErrorListTableView.axaml      # DataGrid for errors
│   │   ├── SimulationProcessBarView.axaml    # Play button + Tasks button
│   │   ├── SelectVariablesDialog.axaml       # X-axis ComboBox + Y-axis CheckBoxList
│   │   ├── EditArrowPopOverView.axaml        # Alias + Predicate text fields
│   │   └── Settings/
│   │       ├── CauchyInitialsView.axaml      # Start, End, Step
│   │       ├── MethodSettingsView.axaml      # Method, Accurate, Accuracy, Stable, Parallel,
│   │       │                                 #   Server, Port
│   │       ├── EventDetectionView.axaml      # In use, Gamma, Step limit, Low border
  │   │       └── ResultProcessingView.axaml    # Save result (MEMORY/FILE), Simplify checkbox,
   │   │                                         #   Simplify method (Radial-Distance/Douglas-Peucker), Tolerance
│   ├── Controls/
│   │   ├── BlueprintCanvasPanel.cs           # Custom Panel: OnRender for states/arrows
│   │   ├── PropertiesGrid.axaml              # Reusable label+control grid
│   │   └── NumericCell.axaml                 # Custom DataGrid cell for row/position columns
│   ├── Services/                             # UI-specific services (Avalonia dependencies)
│   │   ├── EditorPlatformService.cs          # Cut/Copy/Paste event propagation
│   │   └── TextEditorFactory.cs              # Creates/disposes AvalonEdit instances
│   ├── ViewModels/                           # (Empty — all ViewModels in ISMA.ViewModels)
│   ├── Converters/                           # (Empty — all converters in ISMA.ViewModels)
│   ├── App.axaml                             # Fluent theme, resource dictionaries, styles
│   ├── App.xaml.cs                           # DI registration, application lifecycle
│   ├── Program.cs                            # Avalonia entry point
│   └── ISMA.App.csproj
├── ISMA.Tests/                               # xUnit + FluentAssertions + Moq
│   ├── Domain/
│   │   ├── SimulationParametersTests.cs      # Serialization, defaults, snapshot/commit
│   │   ├── BlueprintModelTests.cs            # Empty model defaults, state/transaction creation
│   │   ├── BlueprintToLismaConversionTests.cs # All conversion scenarios
│   │   ├── PreferencesTests.cs               # Load/save, window geometry
│   │   ├── SimulationPointTests.cs           # Equals/GetHashCode for arrays
│   │   └── CodeRegionTests.cs                # Line range tracking, fragmentNameByIndex
│   ├── ViewModels/
│   │   ├── SimulationServiceViewModelTests.cs # Mock server: compile errors, success, progress, cancel
│   │   ├── ProjectViewModelTests.cs          # Create/close projects, name changes, file save
│   │   ├── SimulationParametersViewModelTests.cs # Snapshot captures, commit applies
│   │   ├── ErrorListViewModelTests.cs        # Clear and repopulate
│   │   ├── MainWindowViewModelTests.cs       # Commands exist and invoke
│   │   ├── BlueprintEditorViewModelTests.cs  # Add/remove states and transitions
│   │   └── BlueprintToLismaConversionViewModelTests.cs # ViewModel uses converter correctly
│   └── ISMA.Tests.csproj
├── Directory.Build.props                     # Common properties: nullable, analyzers, LangVersion
├── Directory.Packages.props                  # Centralized package versions
└── isma-ui-dotnet.sln
```

---

## Avalonia 12 Specifics

The following Avalonia 12 features and changes must be used throughout:

| Feature | Usage |
|---------|-------|
| **`.axaml` extension** | All XAML files use `.axaml`, not `.xaml` |
| **Compiled bindings** | `<AvaloniaUseCompiledBindingsByDefault>true</AvaloniaUseCompiledBindingsByDefault>` in .csproj; `x:DataType` on all root elements |
| **`IsVisible` (bool)** | Replace WPF `Visibility` enum — `IsVisible="False"` = Collapsed |
| **`BoxShadow`** | CSS-like syntax on `Border` — replaces `DropShadowEffect` |
| **Pseudo-classes** | `:pointerover`, `:pressed`, `:focus`, `:disabled` — replace WPF `Triggers` |
| **Style classes** | `Classes="h1 primary"` — replace WPF `Style x:Key` |
| **Container Queries** | `ContainerQuery` markup extension for responsive layouts |
| **Control Themes** | `<ControlTheme>` for custom controls — replace implicit styles |
| **`$parent[]` binding** | `{Binding $parent[Grid].Property}` — replace `RelativeSource AncestorType` |
| **`#name` binding** | `{Binding #myControl.Text}` — replace `ElementName` |
| **`$self` binding** | `{Binding $self.Property}` — replace `RelativeSource Self` |
| **`OnFormFactor`** | Static platform detection: `{OnFormFactor Desktop='250,*', Mobile='*'}` |
| **`AvaloniaUI.DiagnosticsSupport`** | Developer tools (NOT deprecated `Avalonia.Diagnostics`) |

---

## Unix Socket Cross-Platform Support

The original uses Linux-only Netty Epoll for Unix Domain Sockets. The .NET implementation must be cross-platform:

| Platform | gRPC Unix Socket | HTTP Unix Socket |
|----------|-----------------|------------------|
| **Linux** | `UnixDomainSocketEndPoint` | `UnixDomainSocketEndPoint` |
| **Windows** | Named pipe (`Microsoft.IO.NamedPipeChannel`) | Named pipe |
| **macOS** | `UnixDomainSocketEndPoint` | `UnixDomainSocketEndPoint` |

Implementation: Create an `IUnixSocketHandler` abstraction in Infrastructure with platform-specific implementations. Use `Grpc.Net.Client` with a custom `HttpHandler` that selects the appropriate transport.

---

## Exchange-Format Binary Reader

The original uses `ru.isma.next.exchange.format` for binary simulation result reading. This library must be **ported/reimplemented in .NET** inside `ISMA.Infrastructure`:

- `BinaryFilePointProvider` reads binary `.bin` files
- Each record: `x` (double), `yForDe[]` (double[]), `rhs[][]` (double[][])
- Metadata: column names, equation counts (DE_, AE_, f prefixes)
- Stream as `IEnumerable<SimulationPoint>` for CSV export and chart display
- Port the Java `readAllPointsSequence()` and metadata parsing logic
- The `BinaryEquationIndexProvider` parses column prefixes to derive equation counts and codes
- This is the most complex infrastructure component — treat it as a separate porting effort

---

## Migration Phases

### Phase 0: Foundation & Project Setup

**Goal:** Create the solution structure, configure build, DI, and verify the project compiles.

#### Steps

1. Create .NET 10 solution with 5 projects (Domain, Infrastructure, ViewModels, App, Tests)
2. Configure `Directory.Build.props` for common properties (nullable enabled, analyzers, LangVersion=12)
3. Configure `Directory.Packages.props` for centralized package version management
4. Add NuGet packages:
   - **Domain:** None (pure POCOs)
   - **Infrastructure:** `Grpc.Net.Client`, `Grpc.Net.Client.Web`, `Google.Protobuf`, `Grpc.Tools`, `Microsoft.Extensions.Logging.Abstractions`, `System.IO.Pipelines`
   - **ViewModels:** `CommunityToolkit.Mvvm`, `Microsoft.Extensions.DependencyInjection.Abstractions`
   - **App:** `Avalonia.Themes.Fluent`, `Avalonia.Controls.DataGrid`, `Avalonia.Desktop`, `CommunityToolkit.Mvvm`, `Microsoft.Extensions.DependencyInjection`, `ICSharpCode.AvalonEdit`, `AvaloniaUI.DiagnosticsSupport`, `Avalonia.Fonts.Inter`
   - **Tests:** `xunit`, `xunit.runner.visualstudio`, `FluentAssertions`, `Moq`, `Microsoft.NET.Test.Sdk`, `coverlet.collector`
5. Configure `Grpc.Tools` protobuf generation from `protobuf-contracts/simulation/`
6. Set up DI registration skeleton in `App.xaml.cs` using `ServiceCollectionExtensions` pattern
7. Create minimal `MainWindow.axaml` with empty content to verify the app runs
8. Create `ViewLocator` for automatic View resolution from ViewModel type

#### Acceptance Checklist

- [ ] Solution builds with `dotnet build` (zero errors)
- [ ] `dotnet run` launches an empty Avalonia 12 window
- [ ] All 5 projects compile (zero errors)
- [ ] gRPC stubs generate correctly from protobuf contracts
- [ ] DI container resolves `MainWindowViewModel`
- [ ] Tests project compiles (no tests yet)
- [ ] `.axaml` files compile (XAML validation passes)
- [ ] `ViewLocator` resolves `MainWindowViewModel` → `MainWindow.axaml`
- [ ] `Directory.Packages.props` centralizes all package versions

---

### Phase 1: Domain Layer — Models, DTOs, Interfaces & Conversion Algorithm

**Goal:** Implement all domain models, DTOs, service interfaces, and the pure `convertToLisma()` algorithm. Zero external dependencies.

#### Steps

1. **Simulation Models** (pure POCOs with `[JsonSerializable]` support)
   - `SimulationPoint` — `double X`, `double[] YForDe`, `double[][] Rhs`
     - Override `Equals`/`GetHashCode` using `SequenceEqual` for arrays
   - `SimulationProgress` — `double StartTime`, `double EndTime`, `double CurrentTime`
   - `SimulationMetadata` — `List<string> ColumnNames`
   - `MetricData` — `long StartTime`, `long EndTime`, derived `SimulationTime`

2. **Simulation Parameters Models**
   - `CauchyInitials` — `double StartTime`, `double EndTime`, `double InitialStep`
   - `IntegrationMethodParameters` — `string SelectedMethod`, `double Accuracy`,
      `bool IsAccuracyInUse`, `bool IsStableAllowedInUse`, `bool IsStableInUse`,
      `bool IsParallelInUse`, `string Server`, `int Port`
      - NOTE: `IsStableAllowedInUse` is an internal flag (set by infrastructure based on server capabilities).
        `IsStableInUse` is the user-facing flag. Both are preserved from original for model fidelity.
        Only `IsStableInUse` is exposed in the ViewModel UI.
   - `EventDetectionParameters` — `bool IsEventDetectionInUse`, `bool IsStepLimitInUse`,
     `double Gamma`, `double LowBorder`
   - `ResultSavingParameters` — `SaveTarget SavingTarget` (enum: `Memory`, `File`)
   - `ResultProcessingParameters` — `bool IsSimplifyInUse`, `string SelectedSimplifyMethod`,
     `double Tolerance`
   - `SimulationParameters` — composite of all above (serializable)

3. **Project Models**
   - `LismaTextModel` — `string FullText`, `List<CodeRegion> Regions`
     - Static `DefaultFragment` (CodeRegion with name "Main", lines 0–0)
     - `FragmentNameByIndex(int index)` method
   - `CodeRegion` — `string Name`, `int StartLine`, `int EndLine`
   - `BlueprintModel` — `BlueprintStateModel Main`, `BlueprintStateModel Init`,
     `BlueprintStateModel[] States`, `BlueprintTransactionModel[] Transactions`,
     `BlueprintLoopTransactionModel[] LoopTransactions`
     - Static `Empty` property matching original defaults (Main at 10,10; Init at 10,100)
   - `BlueprintStateModel` — `double CanvasPositionX`, `double CanvasPositionY`,
     `string Name`, `string Text`
   - `BlueprintTransactionModel` — `string StartStateName`, `string EndStateName`,
     `string Predicate`, `string Alias = ""`
   - `BlueprintLoopTransactionModel` — `string StateName`, `string Predicate`,
     `string Alias = ""`, `string Text`

4. **UI Models**
   - `ErrorInfo` — `int Row`, `int Position`, `string FragmentName`, `string Message`
   - `InProgressSimulation` — `int Id`, `string ModelName`, `SimulationParameters Parameters`,
     `double Progress` (0.0–1.0)
   - `CompletedSimulation` — `int Id`, `string ModelName`, `IEquationIndexProvider? EquationIndexProvider`,
      `MetricData MetricData`, `SimulationParameters Parameters`, `string CachedFile`,
      `List<string> CachedColumnNames`
      - NOTE: `IEquationIndexProvider` is a domain interface (dependency inversion). Infrastructure implements it.
        Used for CSV export to map equation indices to column names. Nullable since not all results have it.

5. **Preferences Models**
   - `WindowPreferences` — `double X`, `double Y`, `double Width`, `double Height`, `bool IsMaximized`
   - `DefaultFilesPreferences` — `string[] LastOpenedProjectPath`
   - `Preferences` — `WindowPreferences WindowPreferences`, `DefaultFilesPreferences DefaultFilesPreferences`

6. **DTOs** (for server communication)
   - `CompileResult` — `string ModelId`, `List<CompilationError> Errors`, `List<string> Warnings`
   - `ValidationResult` — `List<CompilationError> Errors`, `List<string> Warnings`
   - `CompilationError` — `int Row`, `int Column`, `string Message`
   - `SyntaxTokenDto` — `int Start`, `int Length`, `SyntaxTokenKind Kind`
   - `SyntaxTokenKind` — `Unspecified`, `Keyword`, `Comment`, `Number`, `Text`
   - `CachedSimulationResult` — `string File`, `List<string> ColumnNames`
   - `RunSimulationParams` — `double StartTime`, `double EndTime`, `double InitialStep`,
     `string MethodName`, `double Accuracy`, `bool IsAccuracyInUse`,
     `bool IsStabilityControlInUse`, `string CompiledModelId`,
     `double? EventDetectionGamma`, `double? EventDetectionLowBorder`
   - `SocketPaths` — `string Grpc`, `string Http`

7. **Conversion Algorithm** (pure algorithm, no dependencies)
   - `BlueprintToLismaConverter.ConvertToLisma(BlueprintModel)` → `LismaTextModel`
   - **Algorithm:**
     1. Start with main state text as base content
     2. Process transactions: group by target state + predicate, create `state "key" { ... } from start1, start2, ...;` blocks
     3. Process loop transactions: create pseudo-state pattern
        - `state <name>_pseudo_1 (<predicate>) { ... } from <state>;`
        - `state <name> (1 > 0) { ... } from <pseudo_1>;`
     4. Track `CodeRegion` for each generated fragment (line numbers)
     5. Return `LismaTextModel` with full text and regions list
   - Helper: `CreateTransactionKey(targetState, predicate)` → `"{targetState} ({predicate})"`
   - Helper: `StateBlockModel` — builds state block string with proper formatting

8. **Service Interfaces** (in `Contracts/`, implemented in Infrastructure and ViewModels)
   - `ISimulationServerFacade` — `CompileModel(source) → CompileResult`, `ValidateModel(source) → ValidationResult`,
     `HighlightSource(source) → SyntaxTokenDto[]`, `RunSimulation(params) → long`,
     `MonitorSimulation(id) → IAsyncEnumerable<SimulationProgress>`,
     `DownloadResult(id) → CachedSimulationResult`, `CancelSimulation(id) → void`,
     `GetSimulationMethods() → string[]`, `Shutdown() → void`
   - `IEquationIndexProvider` — `GetDifferentialEquationCount()`, `GetAlgebraicEquationCount()`,
     `GetDifferentialEquationCode(index)`, `GetAlgebraicEquationCode(index)`
   - `ISimulationResultReader` — `Results` (IEnumerable<SimulationPoint>)
   - `ISyntaxHighlighter` — `Highlight(source) → SyntaxTokenDto[]`
   - `ITextEditorFactory` — `CreateTextEditor(text, onTextChanged)`, `DisposeInstance(editor)`

#### Acceptance Checklist

- [ ] All domain model classes compile with zero dependencies on external libraries
- [ ] All DTOs compile (no dependencies)
- [ ] `SimulationPoint.Equals`/`GetHashCode` work correctly for arrays
- [ ] `BlueprintModel.Empty` matches original defaults exactly (Main at 10,10; Init at 10,100)
- [ ] `SaveTarget` enum has `Memory` and `File` values
- [ ] `SimulationParameters` serializes and deserializes correctly with `System.Text.Json`
- [ ] `CodeRegion.FragmentNameByIndex()` works correctly
- [ ] `BlueprintToLismaConverter.ConvertToLisma()` produces correct LISMA text for:
  - [ ] Empty blueprint (Main + init only)
  - [ ] Single state with content
  - [ ] Multiple states with regular transitions
  - [ ] Loop transitions (pseudo-state pattern)
  - [ ] Multiple transitions to same target with same predicate (merge behavior)
- [ ] `LismaPdeTranslationResult` sealed interface with `SuccessTranslation` and `FailedTranslation` variants
- [ ] `dotnet build ISMA.Domain` produces zero errors and zero warnings
- [ ] All DTOs pass JSON serialization round-trip test

---

### Phase 2: Infrastructure Layer — Server Communication

**Goal:** Implement gRPC client, HTTP client, server lifecycle management, and binary result reading. All interfaces from Domain.Contracts are implemented.

#### Steps

1. **SimulationServerManager**
   - Resolve script path from `ISMA_SERVER_SCRIPT` env var or `isma.server.script` config key
   - Launch server as child process via `ProcessBuilder`
   - Parse stdout: skip `WARNING:`, `SLF4J:`, blank, log-prefixed lines
   - Extract gRPC socket path from first non-skipped line
   - Extract HTTP socket path from line containing `HTTP_SOCKET=`
   - Return `SocketPaths(grpc, http)`
   - Register shutdown hook to stop server on process exit
   - Cross-platform: use `ProcessStartInfo` with proper argument escaping

2. **Unix Socket Handler** (cross-platform abstraction)
   - `IUnixSocketHandler` interface
   - `LinuxUnixSocketHandler` — uses `UnixDomainSocketEndPoint`
   - `WindowsNamedPipeHandler` — uses named pipes
   - `MacUnixSocketHandler` — uses `UnixDomainSocketEndPoint`
   - Factory selects handler based on `RuntimeInformation.IsOSPlatform()`

3. **GrpcSimulationClient**
   - Use `Grpc.Net.Client` with custom `HttpHandler` wrapping the Unix socket handler
   - `RunSimulation(params) → long` — blocking call
   - `MonitorSimulation(id) → IAsyncEnumerable<SimulationProgress>` — server streaming
   - `DownloadResult(id) → string downloadUrl` — returns URL for HTTP download
   - `CancelSimulation(id) → void` — blocking call
   - `GetSimulationMethods() → string[]` — blocking call

4. **GrpcLismaCompilerClient**
   - `CompileModel(source) → CompileResult` — modelId + errors + warnings
   - `ValidateModel(source) → ValidationResult` — errors + warnings
   - `HighlightSource(source) → SyntaxTokenDto[]` — token positions and kinds
   - `DeleteCompiledModel(modelId) → void`

5. **HttpSimulationClient**
   - `DownloadResultToFile(downloadUrl, destinationPath) → FileInfo`
   - Create cache directory `<temp>/isma-simulation-cache/` if not exists
   - Use `HttpClient` with proper timeout

6. **SimulationServerFacade** (implements `ISimulationServerFacade`)
   - `Warmup()` — start server, create all clients
   - `Shutdown()` — dispose all clients and stop server
   - `CompileModel(source) → CompileResult`
   - `ValidateModel(source) → ValidationResult`
   - `HighlightSource(source) → SyntaxTokenDto[]`
   - `RunSimulation(params) → long`
   - `MonitorSimulation(id) → IAsyncEnumerable<SimulationProgress>`
   - `DownloadResult(id) → CachedSimulationResult` — gets URL from gRPC, downloads via HTTP
   - `CancelSimulation(id) → void`
   - `GetSimulationMethods() → string[]`

7. **BinaryFilePointProvider** (implements `ISimulationResultReader`)
   - Read binary result files using ported exchange-format library
   - Each record: `x` (double), `yForDe[]` (double[]), `rhs[][]` (double[][])
   - Stream as `IEnumerable<SimulationPoint>`
   - Parse metadata: column names, equation counts
   - Static `ReadMetadata(file) → SimulationMetadata`

8. **BinaryEquationIndexProvider** (implements `IEquationIndexProvider`)
   - Parse column name prefixes: `DE_` (differential), `AE_` (algebraic), `f` (forcing)
   - `GetDifferentialEquationCount()` — count DE_ columns
   - `GetAlgebraicEquationCount()` — count AE_ columns
   - `GetDifferentialEquationCode(index)` — return column name for DE at index
   - `GetAlgebraicEquationCode(index)` — return column name for AE at index

9. **GrinProcessLauncher**
   - Resolve script from `ISMA_GRIN_SCRIPT` env var or `isma.grin.script` config key
   - `Launch(resultFile, xAxisColumn, chartColumns)` — starts Grin via `ProcessBuilder`
   - Arguments: `--result-file <path>`, `--x-axis <name>`, `--charts <names>`
   - Register shutdown hook to destroy process

#### Acceptance Checklist

- [ ] `SimulationServerManager` starts and stops the server process correctly
- [ ] Socket path parsing handles all output formats (WARNING, SLF4J, blank lines)
- [ ] gRPC client connects via Unix Domain Socket (Linux) or named pipes (Windows)
- [ ] `Warmup()` successfully initializes all clients
- [ ] `Shutdown()` cleanly disposes all clients and stops server
- [ ] `CompileModel()` returns `CompileResult` with modelId and errors
- [ ] `ValidateModel()` returns `ValidationResult`
- [ ] `HighlightSource()` returns token list with correct positions and kinds
- [ ] `RunSimulation()` returns a simulation ID
- [ ] `MonitorSimulation()` streams progress updates as `IAsyncEnumerable`
- [ ] `DownloadResult()` saves binary file to cache directory
- [ ] `CancelSimulation()` stops a running simulation
- [ ] `GetSimulationMethods()` returns available method names
- [ ] `BinaryFilePointProvider` reads binary files and yields `SimulationPoint`s
- [ ] `BinaryEquationIndexProvider` correctly parses DE_, AE_, f column prefixes
- [ ] `GrinProcessLauncher` launches the chart viewer process with correct arguments
- [ ] Shutdown hook terminates server on process exit
- [ ] Cross-platform: Unix socket handler works on Linux (primary)

---

### Phase 3: Infrastructure Layer — File Storage & Preferences

**Goal:** Implement file I/O for projects and preferences persistence.

#### Steps

1. **ProjectFileService**
   - `Open(ownerWindow) → List<string> filePaths` — FileDialog with filters:
     - `All ISMA Files` (`.iscm2`, `.scisma`, `.im`)
     - `LISMA Text` (`.iscm2`)
     - `State Chart` (`.scisma`)
     - `Legacy` (`.im`)
   - `Open(paths) → List<ProjectType>` — dispatches by extension:
     - `.iscm2` → LISMA text project
     - `.scisma` → Blueprint project (JSON-encoded `BlueprintModel`)
     - `.im` → Legacy text project (backward compatibility, TODO)
   - `Save(project) → bool` — writes project content to file
     - LISMA text → write `FullText` to file
     - Blueprint → serialize `BlueprintModel` to JSON
   - `SaveAs(project) → bool` — same as Save but with FileDialog
   - `SaveAll(projects) → bool` — iterate all projects, save each

2. **PreferencesProvider**
   - Resolve settings file path from config (default: app data directory + `preferences.json`)
   - `Load() → Preferences` — deserialize JSON
   - `Save(preferences) → void` — serialize JSON
   - `CommitWindow(WindowPreferences) → void` — update and persist
   - `CommitFiles(DefaultFilesPreferences) → void` — update and persist
   - Restore last opened file paths on startup

3. **Default values registry** (shared constants, no UI dependencies)
    - Cauchy: `StartTime=0.0`, `EndTime=10.0`, `InitialStep=0.1`
    - Integration: `Accuracy=0.1`, `Server="localhost"`, `Port=7890`
    - Event Detection: `Gamma=0.8`, `LowBorder=0.001`
    - Simplify methods: `["Radial-Distance", "Douglas-Peucker"]`
    - File extension constants: `.iscm2`, `.scisma`, `.im`, `.params.json`, `preferences.json`

#### Acceptance Checklist

- [ ] Opening `.iscm2` file reads and returns file path
- [ ] Opening `.scisma` file reads and returns file path
- [ ] Opening `.im` file reads and returns file path (legacy, backward compat)
- [ ] Save writes correct format for LISMA text projects
- [ ] Save writes correct JSON format for blueprint projects
- [ ] Preferences persist across app restarts
- [ ] Last opened files are restored on startup
- [ ] Window geometry is saved and restored
- [ ] Store Settings saves parameters to JSON
- [ ] Load Settings restores parameters from JSON
- [ ] Default values match original application exactly

---

### Phase 4: ViewModels — Presentation Layer

**Goal:** Implement all ViewModels and presentation services using CommunityToolkit.Mvvm. These are UI-framework agnostic and fully testable.

#### Steps

1. **MainWindowViewModel**
   - `ObservableCollection<IProjectViewModel> Projects`
   - `IProjectViewModel? ActiveProject`
   - `bool ShowSettings` (binds to settings panel visibility)
   - Commands (via `[RelayCommand]`):
     - `NewText()` — delegates to `ProjectService.CreateNew("New project")`
     - `NewBlueprint()` — delegates to `ProjectService.CreateNewBlueprint("New statechart")`
     - `Open()` — delegates to `ProjectFileService.Open()`
     - `Save()` — delegates to `ProjectFileService.Save(ActiveProject)`
     - `SaveAll()` — delegates to `ProjectFileService.SaveAll(Projects)`
     - `Close()` — delegates to `ProjectService.Close(ActiveProject)`
     - `CloseAll()` — delegates to `ProjectService.CloseAll()`
     - `Exit()` — closes application
     - `Cut()` — delegates to `EditorPlatformService.Cut()`
     - `Copy()` — delegates to `EditorPlatformService.Copy()`
     - `Paste()` — delegates to `EditorPlatformService.Paste()`
     - `Verify()` — delegates to `LismaPdeService.Validate(ActiveProject)`
     - `Run()` — delegates to `SimulationService.Simulate()`
     - `StoreSettings()` — delegates to `SimulationParametersService.Store()`
     - `LoadSettings()` — delegates to `SimulationParametersService.Load()`

2. **ProjectViewModel** (interface + implementations)
   - `IProjectViewModel` — `string Name`, `string? FilePath`, `object EditorContent`,
     `event Action? NameChanged`, `void Dispose()`
   - `LismaProjectViewModel` — wraps `LismaTextModel`, integrates with data provider
   - `BlueprintProjectViewModel` — wraps `BlueprintModel`, integrates with `convertToLisma()`

3. **Data Provider Bridges** (per-project scoped)
   - `LismaProjectDataProvider` — bridge between `LismaProjectViewModel` and text editor
     - `string Text` — getter reads from editor, setter writes to editor
   - `BlueprintProjectDataProvider` — bridge between `BlueprintProjectViewModel` and blueprint editor
     - `BlueprintModel Blueprint` — getter/setter

4. **SimulationService** (presentation service)
   - `ObservableCollection<InProgressSimulationViewModel> TrackingTasks`
   - `Simulate()` — orchestrates full simulation flow:
     1. Snapshot parameters from `SimulationParametersService`
     2. Get active project source text
     3. Call `serverFacade.CompileModel(source)`
     4. Map compilation errors to `ErrorInfo` and call `ModelErrorService.PutErrorList(errors)`
     5. Call `serverFacade.RunSimulation(params)` → `simulationId`
     6. Start monitoring: `serverFacade.MonitorSimulation(id)` → stream progress
     7. For each progress update: normalize to 0.0–1.0, update `InProgressSimulationViewModel.Progress`
     8. Call `serverFacade.DownloadResult(id)` → `CachedSimulationResult`
     9. Create `CompletedSimulation` and call `SimulationResultService.CommitResult()`
     10. Remove from `TrackingTasks`
   - `StopSimulation(InProgressSimulationViewModel)` — call `serverFacade.CancelSimulation()`
   - Uses `Task.Run` or `Channels` for background execution (no virtual threads in .NET)

5. **SimulationResultService** (presentation service)
   - `ObservableCollection<CompletedSimulation> TrackingTasksResults`
   - `CommitResult(CompletedSimulation)` — add to collection (thread-safe)
   - `RemoveResult(CompletedSimulation)` — remove from collection
   - `ShowChart(CompletedSimulation)` — open axis picker dialog, launch Grin with selected axes
   - `ExportToFile(CompletedSimulation, filePath)` — async CSV export:
     - Header: `x, [DE column names], [AE column names], f0, f1, ..., fN`
     - Stream points from `BinaryFilePointProvider`
     - Write to buffered `StreamWriter` on background thread

6. **SimulationParametersViewModel**
    - `CauchyInitials` — `double StartTime`, `double EndTime`, `double Step`
    - `IntegrationMethod` — `string SelectedMethod`, `double Accuracy`, `bool IsAccuracyInUse`,
      `bool IsStableInUse`, `bool IsParallelInUse`, `string Server`, `int Port`
      - NOTE: `IsStableAllowedInUse` exists in the Domain model but is NOT exposed in the ViewModel UI.
        It is an internal flag set based on server capabilities, not a user-configurable setting.
    - `EventDetection` — `bool IsEventDetectionInUse`, `bool IsStepLimitInUse`, `double Gamma`, `double LowBorder`
    - `ResultSaving` — `SaveTarget SavingTarget`
    - `ResultProcessing` — `bool IsSimplifyInUse`, `string SelectedSimplifyMethod`, `double Tolerance`
    - `ObservableCollection<string> IntegrationMethods`
    - `Snapshot() → SimulationParameters` — capture all properties
    - `Commit(SimulationParameters) → void` — apply all properties

7. **ErrorListViewModel**
   - `ObservableCollection<ErrorInfo> Errors`
   - `PutErrorList(IEnumerable<ErrorInfo>)` — clear and add all

8. **Settings ViewModels** (each in its own file, bound to SimulationParametersViewModel)
    - `CauchyInitialsViewModel` — `StartTime`, `EndTime`, `Step` properties
    - `MethodSettingsViewModel` — `SelectedMethod`, `Accuracy`, `IsAccuracyInUse`, `IsStableAllowedInUse`, `IsStableInUse`, `IsParallelInUse`, `Server`, `Port`, `IntegrationMethods`
    - `EventDetectionViewModel` — `IsEventDetectionInUse`, `IsStepLimitInUse`, `Gamma`, `LowBorder`
    - `ResultProcessingViewModel` — `IsSimplifyInUse`, `SelectedSimplifyMethod`, `Tolerance`

9. **BlueprintEditorViewModel**
   - `ObservableCollection<BlueprintStateViewModel> States`
   - `ObservableCollection<BlueprintTransactionViewModel> Transactions`
   - `ObservableCollection<BlueprintLoopTransactionViewModel> LoopTransactions`
   - `BlueprintEditorMode CurrentMode` (enum: Default, AddTransition, RemoveState, RemoveTransition)
   - `AddState(double x, double y)` — create state at position
   - `AddTransition(BlueprintStateViewModel source, BlueprintStateViewModel target)` — create arrow
   - `RemoveState(BlueprintStateViewModel state)` — remove state and all associated arrows
   - `RemoveTransition(BlueprintTransactionViewModel arrow)` — remove arrow
   - `RemoveLoop(BlueprintLoopTransactionViewModel arrow)` — remove loop arrow
   - `GetBlueprintModel() → BlueprintModel` — serialize canvas to model
   - `SetBlueprintModel(BlueprintModel model) → void` — restore from model
   - `ResetEditorMode()` — clear all modes

10. **BlueprintStateViewModel**
     - `double CanvasPositionX`, `double CanvasPositionY`
     - `string Name`
     - `string Text`
     - `bool IsEditable`
     - `bool IsMain`, `bool IsInit`
     - `string FillColorHex` (hex color string: "#90EE90" for Main, "#ADD8E6" for Init, "#F08080" for user)
     - NOTE: Color stored as hex string to keep ViewModels UI-framework agnostic (Avalonia `Color` type is not available in ViewModels)

11. **BlueprintTransactionViewModel**
    - `BlueprintStateViewModel StartState`, `BlueprintStateViewModel EndState`
    - `string Predicate`, `string Alias`

12. **BlueprintLoopTransactionViewModel**
    - `BlueprintStateViewModel State`
    - `string Predicate`, `string Alias`, `string Text`

13. **TasksPopOverViewModel**
    - `ObservableCollection<InProgressSimulationViewModel> InProgress`
    - `ObservableCollection<CompletedSimulation> Completed`
    - Properties for UI binding

14. **SelectVariablesDialogViewModel**
    - `ObservableCollection<string> AllColumns`
    - `string SelectedXAxis` (pre-select "TIME")
    - `ObservableCollection<NamedPickerItem> YAxisItems`
    - `ICommand SelectAllCommand`, `ICommand UnselectAllCommand`, `ICommand OkCommand`, `ICommand CloseCommand`

15. **EditArrowPopOverViewModel**
    - `string Alias`
    - `string Predicate`

16. **InProgressSimulationViewModel**
    - `int Id`, `string ModelName`, `SimulationParameters Parameters`
    - `double Progress` (0.0–1.0, `[ObservableProperty]`)
    - `bool CanAbort`

#### Acceptance Checklist

- [ ] All ViewModels inherit from `ObservableObject` (CommunityToolkit.Mvvm)
- [ ] All properties use `[ObservableProperty]` or `[NotifyPropertyChangedFor]`
- [ ] All commands use `[RelayCommand]`
- [ ] `MainWindowViewModel` has all 15 commands (NewText, NewBlueprint, Open, Save, SaveAll, Close, CloseAll, Exit, Cut, Copy, Paste, Verify, Run, StoreSettings, LoadSettings)
- [ ] `SimulationService.Simulate()` implements full flow with proper error handling
- [ ] Progress updates propagate via `[ObservableProperty]` from background task
- [ ] `SimulationParametersViewModel` has all 5 parameter sections
- [ ] `IsStableAllowedInUse` exists in Domain model but NOT in ViewModel UI (internal flag only)
- [ ] `ErrorListViewModel` supports clearing and repopulating
- [ ] `BlueprintEditorViewModel` has all 4 editor modes
- [ ] `SelectVariablesDialogViewModel` has axis selection logic
- [ ] `NamedPickerItem<T>` generic model supports axis picker dialog
- [ ] `LismaPdeTranslationResult` sealed interface used by LismaPdeService
- [ ] `CompletedSimulationViewModel` exposes column names for axis picker
- [ ] ViewModels have zero Avalonia dependencies
- [ ] `dotnet build ISMA.ViewModels` produces zero errors

---

### Phase 5: Tests — Domain & ViewModels

**Goal:** Write unit tests for all business logic. UI layer is not tested directly.

#### Steps

1. **Domain Tests** (`ISMA.Tests.Domain`)
   - `SimulationParametersTests` — serialization round-trip, default values, snapshot/commit
   - `BlueprintModelTests` — Empty model defaults, state/transaction creation
   - `BlueprintToLismaConversionTests` — convertToLisma produces correct LISMA text for:
     - Empty blueprint (Main + init only)
     - Single state with content
     - Multiple states with regular transitions
     - Loop transitions (pseudo-state pattern)
     - Multiple transitions to same target with same predicate (merge behavior)
   - `PreferencesTests` — load/save preferences, window geometry persistence
   - `SimulationPointTests` — Equals/GetHashCode for arrays
   - `CodeRegionTests` — line range tracking, `FragmentNameByIndex()`

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
   - `BlueprintEditorViewModelTests` — add/remove states and transitions
   - `BlueprintToLismaConversionViewModelTests` — ViewModel uses converter correctly

#### Acceptance Checklist

- [ ] All domain tests pass (`dotnet test ISMA.Tests`)
- [ ] Blueprint-to-LISMA conversion tests cover all scenarios from UX spec
- [ ] Simulation flow tests mock the server facade
- [ ] ViewModel tests verify command execution
- [ ] Test coverage for simulation parameters serialization
- [ ] Tests run in CI without external dependencies (all mocking)
- [ ] `dotnet test` produces 100% green

---

### Phase 6: UI — Application Shell, Menu, Toolbar, Settings, and Error List

**Goal:** Build the main window layout, menu bar, toolbar, settings panel, error list, and process bar. These are the simplest UI components.

#### Steps

1. **App.axaml**
   - Fluent theme: `<FluentTheme />`
   - Resource dictionaries merged from `Converters/`, `Controls/`, `Styles/`
   - Global styles: fonts, colors, pseudo-class hover effects
   - `x:CompileBindings="True"` on Application root

2. **MainWindow.axaml**
   - BorderPane layout:
     - Top: `MenuBar` + `ToolBar`
     - Center: `ContentControl` bound to `EditorTabPaneView`
     - Right: `ContentControl` bound to `SettingsPanelView` (IsVisible bound to `ShowSettings`)
     - Bottom: `ErrorList` (collapsible drawer) + `SimulationProcessBar`
   - Minimum size: `MinWidth="500" MinHeight="600"`
   - Title: `"ISMA"`
   - `x:DataType="vm:MainWindowViewModel"`

3. **MenuBar** (`IsmaMenuBarView`)
   - File menu: New Text (Ctrl+N), New Statechart (Ctrl+B), Open (Ctrl+O), Save (Ctrl+S), Save As, Save All, Close, Close All, Exit (Ctrl+W)
   - Edit menu: Cut (Ctrl+X), Copy (Ctrl+C), Paste (Ctrl+V)
   - Simulation menu: Verify (Ctrl+F4), Run (Ctrl+F5), Store Settings, Load Settings
   - All commands bound to `MainWindowViewModel` via compiled bindings
   - Accelerators: `InputBindings` for keyboard shortcuts

4. **ToolBar** (`IsmaToolBarView`)
   - Icon buttons: New model, New statechart, Open model, Save current, Save all, Cut, Copy, Paste, Verify, Store Settings, Load Settings
   - Material Design icons (use `Avalonia.MaterialDesign` or inline SVG)
   - Same commands as menu bar
   - Separators between logical groups

5. **Settings Panel** (`SettingsPanelView`)
   - `TabControl` with 4 tabs: Initials, Integration, Event Detection, Result Processing
   - Each tab uses `PropertiesGrid` for label+control rows
   - Bind to `SimulationParametersViewModel` via compiled bindings
   - Width: 240px per tab content

6. **PropertiesGrid** (custom reusable control)
   - Grid layout: label on left, control on right
   - Support for: `Double`, `Integer`, `String`, `Boolean`, `Enum`, `ComboBox`
   - Two-way binding support via compiled bindings
   - Conditional visibility: fields disabled when parent checkbox is unchecked

7. **Error List** (`IsmaErrorListTableView`)
   - `DataGrid` bound to `ErrorListViewModel.Errors`
   - Columns: Row (5%), Position (5%), Fragment (10%), Message (80%)
   - Custom `NumericCell` for Row and Position columns (formatted as integers)
   - Auto-resize columns

8. **Simulation Process Bar** (`SimulationProcessBarView`)
   - `StackPanel` (horizontal) with:
     - Play button (RunCommand) — Material Design `play_arrow` icon
     - Tasks button — opens `TasksPopOverView` as a `Popup`
   - Located at bottom of window

#### Acceptance Checklist

- [ ] MainWindow displays with all regions (top, center, right, bottom)
- [ ] Menu bar shows all items with correct shortcuts
- [ ] Toolbar shows all buttons with icons
- [ ] Commands execute ViewModel methods (zero business logic in code-behind)
- [ ] Settings panel shows all 4 sections with correct controls
- [ ] PropertiesGrid renders label+control rows correctly
- [ ] Error list DataGrid displays errors with correct column widths
- [ ] Process bar shows play button and tasks button
- [ ] Settings panel visibility toggles correctly via `ShowSettings` binding
- [ ] Window minimum size is 500×600
- [ ] Window title is "ISMA"
- [ ] No business logic in any code-behind file (only `InitializeComponent()`)
- [ ] All bindings use compiled bindings (`x:DataType`)
- [ ] Pseudo-classes used for hover/pressed states on buttons

---

### Phase 7: UI — Project Tabs, Text Editor, and File Operations

**Goal:** Implement tab-based project management, LISMA text editor with syntax highlighting, and file open/save.

#### Steps

1. **Editor Tab Pane** (`EditorTabPaneView`)
   - `TabControl` bound to `MainWindowViewModel.Projects` via `ItemsSource`
   - `DataTemplate` for `IProjectViewModel` → shows `EditorContent`
   - Tab close → `CloseCommand` for that project
   - Tab selection → sets `ActiveProject`
   - Tab title bound to project `Name`
   - Unsaved indicator: asterisk in tab title when project has unsaved changes

2. **LISMA Text Editor** (`IsmaTextEditorView`)
   - Use `ICSharpCode.AvalonEdit` `TextEditor` control
   - Monospace font: `FontFamily="Consolas"` or `Courier New`
   - `FontOptions = { FontRenderingEmSize = 12 }`
   - `ShowLineNumbers = true`
   - Syntax highlighting via `TextEditor.SyntaxHighlighting`
   - LISMA syntax highlighting definition:
     - Keywords → orange, bold
     - Comments → gray, italic
     - Numbers → blue
   - Syntax computed server-side via `HighlightSource()` gRPC call
   - Apply highlighting spans on text change with debouncing (100ms)
   - Cut/Copy/Paste via `EditorPlatformService` integration

3. **EditorPlatformService** (UI-specific service)
   - Expose `Action? CutRequested`, `CopyRequested`, `PasteRequested` events
   - Propagate clipboard commands from menu/toolbar to focused editor
   - AvalonEdit has built-in cut/copy/paste — wire events to `TextEditor.Cut()`, `Copy()`, `Paste()`

4. **TextEditorFactory** (in App.Services)
   - Create `TextEditor` instances for blueprint state text editing
   - Dispose instances on tab close
   - Wire up text change callbacks via `TextEditor.TextArea.TextChanged` event

5. **File Operations**
   - Open dialog: `FileDialog` with filters (`.iscm2`, `.scisma`, `.im`)
   - Save dialog: `FileDialog` with type-specific filter
   - Save All: iterate all projects, save each
   - Unsaved changes: track `bool IsDirty` in project viewmodel, show asterisk in tab title

6. **Window State Persistence**
   - Save window geometry on `Closing` event
   - Restore on startup from `PreferencesProvider`
   - Save last opened file paths

#### Acceptance Checklist

- [ ] Multiple tabs can be opened simultaneously
- [ ] Tab titles show project names (filename if saved, "New project" otherwise)
- [ ] Unsaved changes indicated with asterisk in tab title
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
- [ ] Window geometry persists across restarts
- [ ] Last opened files restore on startup

---

### Phase 8: UI — Blueprint Editor (Complex Canvas)

**Goal:** Implement the visual statechart editor with canvas rendering, state boxes, transition arrows, loop arrows, edit popover, and all interaction modes. This is the most complex UI component.

#### Steps

1. **BlueprintCanvasPanel** (custom `Panel`)
    - Override `MeasureOverride` and `ArrangeOverride` for absolute positioning
    - Render states and arrows via `Draw(DrawingContext)` override (direct drawing for performance)
    - Handle pointer events: `PointerPressed`, `PointerMoved`, `PointerReleased`
    - Double-click detection: check `e.ClickCount > 1` in `PointerPressed` handler
    - Scroll support via `ScrollViewer` wrapper in AXAML
    - Coordinate clamping: `max(position, 0.0)` — negative coordinates forbidden

2. **State Box Rendering** (in `OnRender`)
   - Rounded rectangles: `DrawRoundedRectangle(fill, pen, rect, 20, 20)`
   - Main state: `#90EE90` (LightGreen) fill, fixed position (20, 10)
   - Init state: `#ADD8E6` (LightBlue) fill, fixed position (10, 100)
   - User states: `#F08080` (Coral) fill, draggable
   - Text label: `DrawText(font, point, name)` — Arial 16pt, centered
   - Inline name editing: `TextBox` overlay on single-click (200ms `DispatcherTimer` delay)
   - Double-click → open text editor tab via `ITextEditorFactory`

3. **Transition Arrow Rendering** (in `OnRender`)
   - Straight line from source state center to target state center
   - Offset endpoints: `offsetDistance = 10.0`, perpendicular to line
   - Arrowhead: `DrawPolygon(polygonPoints)` — 14×14 isosceles triangle
   - Label: `DrawText(font, labelPosition, displayedText)` — alias if present, else predicate
   - Geometry updates when state positions change (re-render on position change)

4. **Loop Arrow Rendering** (in `OnRender`)
   - Circle: `DrawEllipseGeometry(rect)` with radius 40
   - Arrowhead pointing back to state
   - Label to the right of circle (X offset 120, Y offset -10)
   - Double-click arrowhead → open text editor tab with `"{stateName} (loop)"`

5. **Edit Arrow PopOver** (`EditArrowPopOverView`)
   - `Border` with `BoxShadow` effect (radius 20, color `#D3D3D3`)
   - `CornerRadius="5"`, `Padding="10"`, `MinWidth="300"`
   - `StackPanel` with two `TextBox` fields: Alias (optional) and Predicate
   - Bidirectional binding to `EditArrowPopOverViewModel`
   - Auto-dismiss on `PointerExited` event

6. **Interaction Modes** (managed by `BlueprintEditorViewModel`)
   - `BlueprintEditorMode` enum: `Default`, `AddTransition`, `RemoveState`, `RemoveTransition`
   - Default mode: drag states, click to edit names, double-click for text
   - Add transition mode: click source state, click target state (same state = loop)
   - Remove state mode: click state to delete (Main/Init can be clicked but no removal handler)
   - Remove transition mode: click arrow to delete
   - Mutually exclusive modes via toggle buttons

7. **Blueprint Editor Toolbar**
   - Bottom `StackPanel` (horizontal) with 4 buttons:
     - New State — creates state at (10, 200)
     - New Transition / Stop adding transaction — toggle button
     - Remove state / Stop remove state — toggle button
     - Remove transition / Stop remove transition — toggle button
   - Separator between "add" group and "remove" group

8. **NameChangingMonitor** (in ViewModels layer)
   - `HashSet<string> ExistedNames`
   - `int NextNameCounter = 1`
   - `TryRegister(name) → bool` — returns false if duplicate
   - `TryUnregister(name) → bool` — removes from registry
   - `CreateNextDefaultName() → string` — returns `"New state N"`
   - Name edit rollback: save `previousName`, restore on duplicate

9. **Blueprint-to-LISMA Integration**
   - `BlueprintProjectViewModel.Snapshot()` calls `BlueprintToLismaConverter.ConvertToLisma(BlueprintModel)`
   - Returns `LismaTextModel` with full text and `CodeRegion` list
   - Used by simulation service for compilation

10. **State Content Editing**
    - Double-click state → `ITextEditorFactory.CreateTextEditor(state.Text, onTextChanged)`
    - New tab named after state, content bound to state text
    - Tab close → `ITextEditorFactory.DisposeInstance(editor)`
    - Double-click loop arrowhead → tab named `"{stateName} (loop)"`

#### Acceptance Checklist

- [ ] Canvas renders Main (green) and Init (blue) states at correct positions
- [ ] User states render as Coral rounded rectangles (CornerRadius=20)
- [ ] States are draggable with mouse (PointerPressed → PointerMoved → PointerReleased)
- [ ] Dragging updates arrow positions in real-time
- [ ] Arrow geometry connects state centers correctly (offset 10px perpendicular)
- [ ] Arrowhead rotates to match line angle
- [ ] Loop arrows render as circles (radius 40) with arrowheads
- [ ] New State button creates a state at (10, 200) with auto-generated name
- [ ] New Transition mode: click two states → arrow created
- [ ] New Transition mode: click same state twice → loop arrow created
- [ ] Remove State mode: click state → state and arrows deleted
- [ ] Remove Transition mode: click arrow → arrow deleted
- [ ] Modes are mutually exclusive (toggle buttons call ResetEditorMode)
- [ ] Inline name editing works (single-click, 200ms DispatcherTimer delay)
- [ ] Duplicate names are prevented (NameChangingMonitor)
- [ ] Duplicate names rollback to previous name
- [ ] Double-click state → text editor tab opens
- [ ] Double-click loop arrowhead → text editor tab with "(loop)" suffix
- [ ] Edit Arrow PopOver opens on arrow click
- [ ] PopOver fields bind bidirectionally to arrow properties
- [ ] PopOver dismisses on PointerExited
- [ ] `GetBlueprintModel()` serializes canvas correctly
- [ ] `SetBlueprintModel()` restores canvas from model
- [ ] Blueprint-to-LISMA produces correct output for all scenarios
- [ ] Canvas scrolls when content exceeds viewport
- [ ] No state can be placed at negative coordinates
- [ ] Duplication prevention: no duplicate transitions between same states
- [ ] Duplication prevention: only one loop arrow per state

---

### Phase 9: UI — Tasks PopOver, Results, and Chart Viewer

**Goal:** Implement the Tasks PopOver, simulation result management, axis picker dialog, CSV export, and chart viewer integration.

#### Steps

1. **Tasks PopOver** (`TasksPopOverView`)
   - `Popup` anchored to Tasks button
   - Two sections: "In progress" and "Completed"
   - In-progress items: task label (`TextBlock`), progress bar (`ProgressBar`), abort button
   - Completed items: task label, Show button, Export button, Remove button, Details chevron (`TextBlock` with "⋯")
   - Details chevron → nested `Popup` with simulation metadata

2. **Select Variables Dialog** (`SelectVariablesDialog.axaml`)
   - `Window` or `ContentDialog` with:
     - X-axis: `ComboBox` bound to `AllColumns`, `SelectedXAxis` pre-selected to "TIME"
     - Y-axis: `ListBox` with `CheckBox` for each column (multi-select)
     - Select All / Unselect All buttons
     - Ok / Close buttons
   - Bound to `SelectVariablesDialogViewModel`
   - Ok command returns selected axes

3. **Simulation Result Service** (presentation service)
   - `CommitResult()` — add to completed list
   - `RemoveResult()` — remove from list
   - `ShowChart(result)` — open axis picker dialog, launch Grin with selected axes
   - `ExportToFile(result, filePath)` — async CSV export:
     - Header: `x, [DE columns], [AE columns], f0, f1, ...`
     - Stream points from `BinaryFilePointProvider`
     - Write to buffered `StreamWriter` on background thread

4. **Grin Process Launcher Integration**
   - `GrinProcessLauncher.Launch(resultFile, xAxisColumn, chartColumns)`
   - Resolve script from config (same as Phase 2)

5. **CSV Export**
   - `FileDialog` for output path (filter `*.csv`)
   - Background task: `Task.Run(() => ExportToFileAsync(...))`
   - Use `BinaryFilePointProvider` to stream points
   - Non-blocking: UI remains responsive during export

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
- [ ] CSV export writes correct format (header + data rows)
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
   - Consistent color palette matching original:
     - Main state: `#90EE90` (LightGreen)
     - Init state: `#ADD8E6` (LightBlue)
     - User states: `#F08080` (Coral)
     - Arrow lines: `#000000` (Black)
     - PopOver background: `#FFFFFF` (White)
     - PopOver shadow: `#D3D3D3` (LightGray)
   - Fluent theme integration
   - Custom accent colors in `App.axaml` if needed
   - Pseudo-class styles for hover/pressed/disabled states

2. **Keyboard Shortcuts**
   - Verify all shortcuts work:
     - `Ctrl+N` / New text
     - `Ctrl+B` / New Statechart
     - `Ctrl+O` / Open
     - `Ctrl+S` / Save
     - `Ctrl+W` / Exit
     - `Ctrl+X` / Cut
     - `Ctrl+C` / Copy
     - `Ctrl+V` / Paste
     - `Ctrl+F4` / Verify
     - `Ctrl+F5` / Run
   - Cross-platform: `Cmd` on macOS (Avalonia handles this automatically via `InputBindings`)

3. **Window Management**
   - Minimum size enforcement (`MinWidth="500" MinHeight="600"`)
   - State persistence (position, size, maximized) via `PreferencesProvider`
   - Multiple monitor support (Avalonia handles this automatically)

4. **Error Handling**
   - Server not found → user-friendly error dialog (Avalonia `MessageBox`)
   - gRPC errors → displayed in error list
   - File I/O errors → error dialogs
   - Simulation failures → error messages in tasks

5. **Performance**
   - Blueprint canvas rendering: test with 50+ states (OnRender should be efficient)
   - Text editor: test with 1000+ line files
   - CSV export: verify non-blocking behavior

6. **Cross-Platform Testing**
   - Linux (primary development platform)
   - Windows (gRPC Unix socket / named pipe support)
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
- [ ] Server connection errors are handled gracefully (error dialog)
- [ ] Compilation errors display in error list
- [ ] Blueprint canvas handles 50+ states without lag
- [ ] Text editor handles 1000+ line files smoothly
- [ ] CSV export completes without freezing UI
- [ ] App runs on Linux
- [ ] App runs on Windows
- [ ] App runs on macOS
- [ ] All tests pass (`dotnet test` 100% green)
- [ ] Documentation is complete

---

## Dependency Mapping Summary

| Original (Java/Kotlin) | Replacement (.NET/C#) | Notes |
|------------------------|----------------------|-------|
| JavaFX `Application` | Avalonia `Application` | Entry point, lifecycle |
| JavaFX `Stage`/`Scene` | Avalonia `Window` | Main window |
| JavaFX `BorderPane` | Avalonia `BorderPane` | Layout |
| JavaFX `TabPane` | Avalonia `TabControl` | Tab management |
| JavaFX `TableView` | Avalonia `DataGrid` (NuGet) | Error list table |
| JavaFX `CodeArea` (fxmisc.richtext) | AvalonEdit `TextEditor` | LISMA text editing |
| JavaFX `Pane` (canvas) | Avalonia `Panel` (custom) | Blueprint canvas |
| JavaFX `Rectangle` | Avalonia `Rectangle` + `CornerRadius` | State boxes |
| JavaFX `Line` | Avalonia `Path` with `LineGeometry` | Arrow shafts |
| JavaFX `Polygon` | Avalonia `Path` with `PathGeometry` | Arrowheads |
| JavaFX `Circle` | Avalonia `Ellipse` | Loop arrows |
| JavaFX `VBox`/`HBox` | Avalonia `StackPanel` | PopOver layout |
| JavaFX `ToolBar` | Avalonia `StackPanel` (horizontal) | Bottom toolbar |
| JavaFX `PopOver` (ControlsFX) | Avalonia `Popup` | Edit popover, tasks |
| JavaFX `DropShadow` | Avalonia `BoxShadow` on `Border` | PopOver shadow |
| Koin DI | Microsoft.Extensions.DependencyInjection | Dependency injection |
| TornadoFX `ViewModel` | CommunityToolkit.Mvvm `ObservableObject` | MVVM base class |
| JavaFX `Simple*Property` | CommunityToolkit.Mvvm `[ObservableProperty]` | Property change notification |
| Kotlin Coroutines | C# `async`/`await` + `Task` | Concurrency |
| Kotlin `Flow` | C# `IAsyncEnumerable` / `ObservableCollection` | Reactive collections |
| gRPC-Java (Netty Epoll) | Grpc.Net.Client (cross-platform) | gRPC client |
| Ktor CIO HTTP | `System.Net.Http.HttpClient` | HTTP client |
| kotlinx.serialization | System.Text.Json | JSON serialization |
| JavaFX `ObservableList` | `ObservableCollection<T>` | Reactive collections |
| JavaFX `FileChooser` | Avalonia `FileDialog` | File dialogs |
| JavaFX `Preferences` | File-based JSON storage | Preferences persistence |
| TornadoFX `drawer` | Avalonia `TabControl` or `StackPanel` | Settings panel |
| Ikonli (Material Design) | Avalonia.MaterialDesign or inline SVG | Icons |
| fxmisc.richtext | ICSharpCode.AvalonEdit | Rich text editing |

---

## Business Features Inventory

All 31 features from the original application that must be implemented:

| # | Feature | Phase | Complexity |
|---|---------|-------|------------|
| 1 | Multi-project editing (tabs) | 7 | Low |
| 2 | LISMA text editing with syntax highlighting | 7 | Medium |
| 3 | Remote syntax highlighting (server-driven) | 7 | Medium |
| 4 | Visual statechart (blueprint) editing | 8 | High |
| 5 | State creation, drag, rename | 8 | Medium |
| 6 | Transition arrow creation and management | 8 | High |
| 7 | Loop transition arrows | 8 | High |
| 8 | Edit arrow PopOver (alias/predicate) | 8 | Medium |
| 9 | Inline state name editing | 8 | Medium |
| 10 | Blueprint-to-LISMA conversion | 1 (Domain) | High |
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
| 28 | State content editing (double-click → text tab) | 8 | Medium |
| 29 | Name uniqueness enforcement | 8 | Low |
| 30 | Parallel execution settings | 6 | Low |
| 31 | Result simplification settings | 6 | Low |

---

## Key Differences from Original Architecture

| Aspect | Original (Java/Kotlin) | New (.NET/C#) |
|--------|----------------------|---------------|
| DI framework | Koin (scoped, module-based) | Microsoft.Extensions.DependencyInjection |
| MVVM framework | TornadoFX (JavaFX-first) | CommunityToolkit.Mvvm (framework-agnostic) |
| Reactive collections | JavaFX `ObservableList` + Kotlin `Flow` | `ObservableCollection<T>` + C# events |
| Concurrency | Kotlin coroutines + virtual threads | C# `async`/`await` + `Task.Run` |
| Server communication | gRPC-Java (Linux epoll only) | Grpc.Net.Client (cross-platform) |
| HTTP client | Ktor CIO | System.Net.Http.HttpClient |
| JSON serialization | kotlinx.serialization | System.Text.Json (source generation) |
| Text editor | fxmisc.richtext (JavaFX) | ICSharpCode.AvalonEdit |
| Canvas rendering | JavaFX `Pane` with child nodes | Custom `Panel` with `OnRender` |
| Styling | JavaFX CSS | Avalonia XAML styles + pseudo-classes |
| Property system | JavaFX `Simple*Property` | CommunityToolkit.Mvvm `[ObservableProperty]` |
| Testing | (not specified) | xUnit + FluentAssertions + Moq |
| Build system | Gradle | MSBuild + NuGet |
