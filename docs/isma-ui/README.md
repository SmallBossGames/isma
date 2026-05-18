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

## Key Files

| File | Role |
| --- | --- |
| `app/src/main/kotlin/.../launcher/IsmaApplication.kt` | JavaFX `Application` entry point |
| `app/src/main/kotlin/.../launcher/DependecyInjectionRootModule.kt` | Koin DI root startup |
| `app/src/main/kotlin/.../services/project/ProjectService.kt` | Project lifecycle management |
| `app/src/main/kotlin/.../services/simualtion/SimulationService.kt` | Simulation orchestration |
| `external-services/src/main/kotlin/.../SimulationServerFacade.kt` | Server communication facade |
| `external-services/src/main/kotlin/.../SimulationServerManager.kt` | Server process lifecycle |
| `text-editor/src/main/kotlin/.../IsmaTextEditor.kt` | Rich text editor (fxmisc.richtext) |
| `blueprint-editor/src/main/kotlin/.../IsmaBlueprintEditor.kt` | Visual statechart editor |
| `domain/src/main/kotlin/.../models/SimulationResult.kt` | Domain model for simulation results |

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
| `text-editor` | Rich text editing with syntax highlighting | `controls`, `fxml` | `ru.isma.next.editor.text` |
| `blueprint-editor` | Visual statechart editor | `controls`, `fxml` | `ru.isma.next.editor.blueprint` |
| `toolkit` | Shared JavaFX utilities | `controls` | `ru.isma.javafx.extensions` |
