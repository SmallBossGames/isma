# ISMA (Инструментальное моделирование)

Kotlin-based mathematical modeling and symbolic computation environment for educational and analytical purposes.

---

## Technology Stack

- **Language:** Kotlin
- **JVM Runtime:** Java Virtual Machine
- **Build System:** Gradle with Kotlin DSL (`build.gradle.kts`)
- **IDE:** IntelliJ IDEA

---

## Working Order — Read Docs Before Code

**Always read `docs/` before reading any source code.** When investigating or modifying a component:

1. Check if `docs/<module>/` exists (e.g. `docs/isma-server/`)
2. Read the documentation first — it provides context, architecture, and API contracts
3. Only then read the source code files

---

## Project Modules

### Core Math & Analysis
- **grin/** - JavaFX-based interactive function visualization and analysis environment
  - `gui/` - JavaFX GUI components
  - `gui:app/`, `gui:common/`, `gui:concatenation/` - GUI submodules
  - `analytic-fu/` - Analytical function parser, expression evaluator, validator
  - `math/` - Numerical math primitives — derivatives, integration, intersection search

### Compiler Infrastructure
- **isma-compiler/** - HSM (High School Math) compiler
  - `hsm-core/` - Core compiler
  - `hsm-fdm/` - Finite difference method backend
  - `hsm-jvm/`, `hsm-jvm-calcmodel/` - JVM implementation
  - `lisma-translator-hsm/` - LISMA to HSM translator

### User Interface
- **isma-ui/** - JavaFX-based UI components (see `isma-ui/AGENTS.md` for details)
  - `app/` - Main application entry point
  - `domain/` - Domain models and business logic
  - `external-services/` - gRPC client layer for server communication
  - `grpc/` - Generated gRPC stubs
  - `text-editor/` - Text editing component
  - `blueprint-editor/` - Blueprint/visual editing component
  - `toolkit/` - Shared UI utilities

### Next-Generation Core
- **isma-next-core/** - Core functionality
- **isma-next-math-engine/** - Math engine
- **isma-next-math-common/** - Common math utilities

### Solver Library
- **isma-solver/** - Numerical integration methods
  - `api/`, `core/` - Core solver API and implementation
  - `lib-meta/` - SPI interface for integration method factories
  - `lib-utils/` - Utilities
  - `lib:euler/`, `lib:rk2/`, `lib:rk3/`, `lib:rk31/`, `lib:rkmerson/`, `lib:rkfehlberg/` - Integration methods

### Protocol Buffers
- **protobuf-contracts/** - Shared protobuf definitions
  - `v1/` - Proto definitions
    - `simulation_service/` - Simulation service proto files
    - `compiler_service/` - LISMA compiler service proto files

### Server (gRPC-based)
- **isma-server/** - Server with Netty transport and Koin DI
  - `app/` - Application entry point
  - `domain/` - Business logic with handlers
  - `grpc/` - Generated gRPC stubs
  - `infrastructure/` - Infrastructure implementations
  - *See `isma-server/AGENTS.md` for details*

### Documentation
- **docs/isma-server/** - Server module documentation
- **docs/isma-ui/** - UI module documentation
- **docs/grin/** - Grin (Graphical Interactive Functions) documentation
- **docs/dotnet-ui-migration/** - WPF to Avalonia migration documentation and guides
- **docs/legacy/** - Legacy system documentation and historical context
- **docs/todo/** - TODO templates and planning

### Out-of-Process Architecture (UI + Server)

The UI launches the server as a separate JVM process communicating via gRPC over Unix Domain Sockets.

- **isma-ui/external-services/** - gRPC client layer
  - `GrpcSimulationClient` - Netty-based gRPC client with Unix socket transport
  - `SimulationServerManager` - Manages server process lifecycle (start/stop)
  - `SimulationServerFacade` - High-level facade for simulation operations
- **Server launch** — `ISMA_SERVER_SCRIPT` env var (bundle) or `isma.server.script` sysprop (IDE/Gradle)
- **Bundle** — `.ci-cd/build-bundle.sh` creates `build/bundle/` with both apps + `run-ui.sh` launcher

---

## Gradle Configuration

- **Centralized versions:** `gradle/libs.versions.toml`
- **All dependency/plugin versions** must be defined in `libs.versions.toml` with aliases
- Use `{ version.ref = "..." }` for version references
- No duplicate `group`, `version`, or `repositories` declarations
- `java.sourceCompatibility` and `java.targetCompatibility` must match

## Java Modules

All applications in this repository should use `module-info.java` for proper module system configuration.

## Build

```bash
./gradlew build          # Build all modules
./gradlew :module:build  # Build specific module
./.ci-cd/build-bundle.sh # Build application distribution bundle UI + server
```

## Architecture Rules

### Separate Domain Models from gRPC Contracts

**Never use gRPC-generated protobuf types in business logic.** Each module must have its own domain models that are mapped from/to gRPC contracts at the boundary.

- **Server `domain/`** — pure business logic, domain entities, and handlers. No gRPC stubs.
- **Server `infrastructure/`** — maps gRPC types to domain types (and vice versa) when crossing the gRPC boundary.
- **UI `external-services/`** — maps gRPC protobuf types to UI domain models. No protobuf types leak into `app/`, `domain/`, or UI components.
- **UI `domain/`** — pure domain models and interfaces. No gRPC types, no JavaFX types.

This ensures that protocol changes only affect the mapping layer, not the business logic. It also keeps domain models free from protobuf annotations, code generation artifacts, and wire-format concerns.

## Proto Contracts

All protobuf definitions live in `protobuf-contracts/v1/`. Proto changes require rebuilding both `isma-server:grpc` and `isma-ui:grpc` modules.
