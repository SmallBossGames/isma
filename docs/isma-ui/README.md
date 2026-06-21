# ISMA-UI

User interface module for the ISMA application, built with Kotlin and JavaFX.

## Purpose

ISMA-UI is the desktop client for the ISMA mathematical modeling environment. It provides a multi-project editing interface with syntax-highlighted LISMA source code, a visual statechart (blueprint) editor, simulation execution pipeline, and result visualization. The UI runs as a separate process from `isma-server`, communicating via gRPC over Unix Domain Sockets (Netty Epoll) and HTTP over Unix sockets (Ktor CIO).

## Quick Navigation

| Document | Description |
| --- | --- |
| [Overview](01-overview.md) | Architecture, module structure, dependency graph, DI wiring |
| [Domain Layer](02-domain-layer.md) | Domain models, interfaces, result streaming |
| [External Services](03-external-services.md) | gRPC client, HTTP client, server lifecycle, facade |
| [UI Components](04-ui-components.md) | App entry point, views, editors, toolbars, models |
| [Build & Deployment](05-build-and-deployment.md) | Gradle config, modules, dependencies, startup |
| [UX Reference](06-ux-reference.md) | Complete user experience specification: windows, menus, dialogs, transitions, features |
| [Blueprint Editor UX](07-blueprint-editor-ux.md) | Detailed specification of the visual statechart editor: canvas, states, arrows, popover, toolbar, modes, LISMA conversion, Avalonia migration mapping |
| [Use Cases](use-cases/README.md) | End-to-end user flows: startup, editing, simulation, results, multi-project workflows |

## Key Files

| File | Role |
| --- | --- |
| `app/src/main/kotlin/.../launcher/IsmaApplication.kt` | JavaFX `Application` entry point |
| `app/src/main/kotlin/.../launcher/DependecyInjectionRootModule.kt` | Koin DI root startup |
| `app/src/main/kotlin/.../launcher/GrinProcessLauncher.kt` | Grin chart viewer process launcher |
| `app/src/main/kotlin/.../services/koin/KoinExtentions.kt` | Service-layer DI modules |
| `app/src/main/kotlin/.../views/koin/KoinExtensions.kt` | View-layer DI modules |
| `app/src/main/kotlin/.../services/project/ProjectService.kt` | Project lifecycle management |
| `app/src/main/kotlin/.../services/simualtion/SimulationService.kt` | Thin coordinator (delegates to SimulationTaskService) |
| `app/src/main/kotlin/.../services/simualtion/SimulationTaskService.kt` | Full simulation lifecycle (compile, run, monitor, download) |
| `app/src/main/kotlin/.../services/simualtion/SimulationParametersService.kt` | Simulation parameter management |
| `app/src/main/kotlin/.../models/projects/LismaTextModel.kt` | LISMA text model with CodeRegion tracking |
| `app/src/main/kotlin/.../models/simulation/SimulationTask.kt` | Running/completed/failed simulation task tracking |
| `app/src/main/kotlin/.../models/simulation/CompletedSimulationModel.kt` | Completed simulation result wrapper |
| `app/src/main/kotlin/.../utilities/BlueprintModelExtensions.kt` | Blueprint-to-LISMA conversion |
| `app/src/main/kotlin/.../services/ModelErrorService.kt` | Compilation/validation error tracking |
| `app/src/main/kotlin/.../services/editors/SyntaxHighlighterService.kt` | Syntax highlighting service |
| `app/src/main/kotlin/.../services/editors/TextEditorFactory.kt` | Text editor factory |
| `app/src/main/kotlin/.../services/preferences/PreferencesProvider.kt` | Window and file preferences persistence |
| `app/src/main/kotlin/.../views/layout/Drawer.kt` | Collapsible drawer panel |
| `app/src/main/kotlin/.../views/dialogs/ItemsPickerDialog.kt` | Variable/axis selection dialog |
| `external-services/src/main/kotlin/.../SimulationServerFacade.kt` | Server communication facade |
| `external-services/src/main/kotlin/.../SimulationServerManager.kt` | Server process lifecycle |
| `external-services/src/main/kotlin/.../RunSimulationParams.kt` | Simulation parameter DTO |
| `text-editor/src/main/kotlin/.../IsmaTextEditor.kt` | Rich text editor (fxmisc.richtext) |
| `text-editor/src/main/kotlin/.../services/EditorPlatformService.kt` | Cut/copy/paste event propagation |
| `blueprint-editor/src/main/kotlin/.../IsmaBlueprintEditor.kt` | Visual statechart editor (UI only) |
| `blueprint-editor/src/main/kotlin/.../IsmaBlueprintViewModel.kt` | Blueprint editor logic (MVVM ViewModel) |
| `blueprint-editor/src/main/kotlin/.../EditorMode.kt` | Sealed class for editor modes |
| `blueprint-editor/src/main/kotlin/.../constants/BlueprintEditorConstants.kt` | All magic numbers |
| `domain/src/main/kotlin/.../models/SimulationResult.kt` | Domain model for simulation results |
| `toolkit/src/main/kotlin/.../controls/PropertiesGrid.kt` | Reusable property grid component |
| `toolkit/src/main/kotlin/.../coroutines/flow/CollectionsExtensions.kt` | ObservableList/Set → Flow bridges |

## Building

```bash
./gradlew :isma-ui:app:build
```

## Running

Requires two system properties pointing to server and Grin launcher scripts:

- `-Disma.server.script=<path>` — ISMA server launch script
- `-Disma.grin.script=<path>` — Grin chart viewer launch script

When running via Gradle (`JavaExec`), these are auto-configured to `$rootDir/build/bundle/`.

## Module Index

| Module | Description | JavaFX Modules | Main Package |
| --- | --- | --- | --- |
| `app` | Application entry, views, services, models | `controls`, `fxml` | `ru.isma.next.app` |
| `domain` | Pure Kotlin domain models | — | `ru.isma.next.domain.models` |
| `external-services` | gRPC/HTTP clients, server manager | — | `ru.isma.next.external` |
| `grpc` | Generated gRPC stubs | — | `ru.nstu.isma.contracts.simulation` |
| `text-editor` | Rich text editing with syntax highlighting | `controls` | `ru.isma.next.editor.text` |
| `blueprint-editor` | Visual statechart editor | `controls` | `ru.isma.next.editor.blueprint` |
| `toolkit` | Shared JavaFX utilities | `controls` | `ru.isma.javafx.extensions` |

## Documents by Audience

| Document | Target Audience |
| --- | --- |
| [01-overview](01-overview.md) | Architects, contributors understanding module layout |
| [02-domain-layer](02-domain-layer.md) | Backend developers working with simulation models |
| [03-external-services](03-external-services.md) | Developers modifying server communication |
| [04-ui-components](04-ui-components.md) | Developers modifying existing JavaFX UI |
| [05-build-and-deployment](05-build-and-deployment.md) | DevOps, contributors setting up the build |
| [06-ux-reference](06-ux-reference.md) | Anyone implementing a replacement UI with feature parity |
| [07-blueprint-editor-ux](07-blueprint-editor-ux.md) | Migrator implementing the Avalonia statechart editor, or anyone needing deep canvas/interaction details |
| [Use Cases](use-cases/README.md) | Product owners, testers, and new contributors understanding end-to-end user workflows |
