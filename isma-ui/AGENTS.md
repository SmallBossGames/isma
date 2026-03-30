# ISMA-UI

User interface module for the ISMA application, built with Kotlin and JavaFX.

## Technology Stack

- **Language:** Kotlin
- **UI Framework:** JavaFX (via TornadoFX)
- **Build System:** Gradle with Kotlin DSL

## Modules

### app/
Main application entry point. Launches the UI and manages server process lifecycle.

- **Main class:** `ru.isma.next.app.launcher.IsmaApplication`
- **Module:** `isma.ui.app.main`
- **Dependencies:** All other isma-ui modules, gRPC client, Koin DI

### domain/
Domain models and business logic for UI layer.

- Pure Kotlin module with no UI dependencies
- Used by other UI modules for shared domain concepts

### external-services/
gRPC client layer for communicating with the ISMA server.

- `GrpcSimulationClient` - Netty-based gRPC client with Unix socket transport
- `SimulationServerManager` - Manages server process lifecycle (start/stop)
- `SimulationServerFacade` - High-level facade for simulation operations

### grpc/
Generated gRPC stubs from protobuf contracts.

- Protobuf source: `../../protobuf-contracts`
- Uses gRPC-Java with Netty transport

### text-editor/
Text editing component for mathematical expressions.

- Uses `fxmisc.richtext` library for rich text editing

### blueprint-editor/
Blueprint/visual editing component.

- Uses JavaFX with FXML

### toolkit/
Shared UI utilities and components.

- Common JavaFX utilities used across modules

## Building

```bash
./gradlew :isma-ui:app:build
```

## Running

The app requires:
- `isma.server.script` system property pointing to the server launch script
- `isma.grin.script` system property pointing to the grin launch script

When running via Gradle, these are auto-configured to point to the bundle directory.
