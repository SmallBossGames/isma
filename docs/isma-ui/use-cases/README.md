# Use Cases — ISMA UI

End-to-end user flows for the ISMA desktop application. Each document describes a complete scenario from trigger to resolution, including preconditions, main flow, alternative flows, and postconditions.

## Index

| ID | Use Case | Description |
|----|----------|-------------|
| [UC-001](UC-001-application-startup.md) | Application Startup and Default Workspace | Launch the application, server initialization, restore last session |
| [UC-002](UC-002-create-edit-lisma-project.md) | Create and Edit LISMA Text Project | Create a new text-based project, write LISMA source code, syntax highlighting |
| [UC-003](UC-003-create-edit-blueprint-statechart.md) | Create and Edit Blueprint Statechart | Visual statechart editor: states, transitions, loops, content editing |
| [UC-004](UC-004-open-save-close-projects.md) | Open, Save, and Close Projects | File I/O: open `.iscm2`/`.scisma`, save, save all, save as, close |
| [UC-005](UC-005-verify-model.md) | Verify Model (Syntax/Semantic Validation) | Validate LISMA source against the server's PDE translator |
| [UC-006](UC-006-run-simulation.md) | Run Simulation with Results Visualization | Full simulation pipeline: compile → run → monitor → download → chart |
| [UC-007](UC-007-export-csv.md) | Export Simulation Results to CSV | Export binary simulation results to CSV format |
| [UC-008](UC-008-configure-simulation-parameters.md) | Configure and Load/Store Simulation Parameters | Set up simulation parameters, save as presets, reload later |
| [UC-009](UC-009-multi-project-workflow.md) | Multi-Project Workflow | Manage multiple concurrent projects (text + blueprint tabs) |
| [UC-010](UC-010-cancel-simulation.md) | Cancel Running Simulation | Stop a mid-execution simulation from the Tasks PopOver |

## Cross-Cutting Concerns

### Architecture Context

All use cases assume the following architecture:

- **ISMA UI** — JavaFX desktop application (this module)
- **isma-server** — Separate JVM process providing compilation, validation, and simulation via gRPC over Unix Domain Sockets
- **Grin** — External chart viewer process launched on-demand
- **Koin DI** — Dependency injection wiring all services and UI components
- **Virtual thread dispatcher** — Simulation execution runs on Java 21+ virtual threads

### Shared Components

| Component | Used In | Role |
|-----------|---------|------|
| `ProjectService` | UC-002, UC-003, UC-004, UC-009 | Manages the observable set of open projects |
| `ProjectFileService` | UC-004 | FileChooser-based open/save operations |
| `SimulationServerFacade` | UC-005, UC-006, UC-008, UC-010 | Orchestrates gRPC + HTTP communication with server |
| `SimulationService` | UC-006, UC-010 | Simulation lifecycle orchestration |
| `SimulationParametersService` | UC-008 | Parameter view models and persistence |
| `SimulationResultService` | UC-006, UC-007 | Completed result management and export |
| `ModelErrorService` | UC-005, UC-006 | Error list tracking and display |
| `PreferencesProvider` | UC-001 | Window geometry and last-opened files persistence |
| `IsmaEditorTabPane` | UC-002, UC-003, UC-009 | Tab-based project display |
| `TasksPopOver` | UC-006, UC-007, UC-010 | Running/completed simulation task management |

### File Formats

| Extension | Type | Content |
|-----------|------|---------|
| `.iscm2` | LISMA text project | Plain text LISMA source code |
| `.scisma` | Blueprint statechart | JSON-encoded `BlueprintModel` |
| `.im2` | Legacy ISMA project | Legacy format (backward compatibility) |
| `.params.json` | Simulation parameters | JSON-serialized `SimulationParametersModel` |
| `.bin` | Simulation result | Binary `SimulationPoint` data (cached, not user-facing) |
| `.csv` | Exported results | CSV with time, DE/AE variables, RHS values |
