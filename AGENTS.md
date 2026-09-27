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

1. Check if `docs/<module>/` exists (e.g. `docs/isma-daemon/`)
2. Read the documentation first — it provides context, architecture, and API contracts
3. Only then read the source code files

---

## Project Modules

### Core Math & Analysis
- **grin/** - JavaFX-based interactive function visualization and analysis environment *(git submodule → [grin-javafx](https://github.com/SmallBossGames/grin-javafx), standalone Gradle project)*
  - `gui/` - JavaFX GUI components
  - `gui:app/`, `gui:common/`, `gui:concatenation/` - GUI submodules
  - `analytic-fu/` - Analytical function parser, expression evaluator, validator
  - `math/` - Numerical math primitives — derivatives, integration, intersection search

### Compiler Infrastructure
- **isma-compiler/** - HSM (High School Math) compiler
  - `hsm-core/` - Core compiler
  - `hsm-fdm/` - Finite difference method backend
  - `hsm-jvm/`, `hsm-jvm-calcmodel/` - JVM implementation
  - `cli/` - Standalone LISMA compiler CLI: reads LISMA from stdin, writes the compiled `.class` file (analyzed hybrid-system class with the translated HSM inlined as a serialized blob) to stdout (`lisma-compiler-cli`); also supports `--validate`; launched by isma-daemon as a separate process

### User Interface
- **isma-ui/** - JavaFX-based UI components *(git submodule → [isma-ui-javafx](https://github.com/SmallBossGames/isma-ui-javafx), standalone Gradle project; see `isma-ui/AGENTS.md` for details)*
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

### Simulation Runner
- **isma-runner/** - Standalone simulation runner CLI. Accepts a compiled `.class` file + simulation parameters, loads the class in a fresh classloader, extracts and deserializes the inlined HSM blob, runs the simulation (equation index provider, initials), and writes the result file in the existing binary format. Launched by isma-daemon as a separate process per simulation.

### Protocol Buffers
- **protobuf-contracts/** - Shared protobuf definitions
  - `v1/` - Proto definitions
    - `simulation_service/` - Simulation service proto files
    - `compiler_service/` - LISMA compiler service proto files

### Daemon (gRPC-based orchestrator)
- **isma-daemon/** - Orchestration daemon with Netty transport and Koin DI. Pure orchestrator: launches the LISMA compiler CLI and the simulation runner CLI as separate processes; no in-process translation or simulation.
  - `app/` - Application entry point
  - `domain/` - Business logic with handlers
  - `grpc/` - Generated gRPC stubs
  - `infrastructure/` - Infrastructure implementations (CLI launching, process registry, stores)
  - *See `isma-daemon/AGENTS.md` for details*

### Language Server (LSP)
- **isma-lsp/** - Standalone LISMA language server: LSP over stdio (JSON-RPC 2.0, Content-Length framing), semantic tokens via ANTLR4 lexer
  - `protocol/` - LSP framing + request dispatch
  - `lsp/` - LISMA language service (semantic tokens) + token codec
  - `smoke.py` - stdio smoke test
  - *See `docs/isma-lsp/` for details*

### Documentation
- **docs/isma-daemon/** - Daemon module documentation
- **docs/isma-ui/** - UI module documentation
- **docs/isma-lsp/** - Language server documentation
- **docs/grin/** - Grin (Graphical Interactive Functions) documentation
- **docs/dotnet-ui-migration/** - WPF to Avalonia migration documentation and guides
- **docs/legacy/** - Legacy system documentation and historical context
- **docs/todo/** - TODO templates and planning

### Out-of-Process Architecture (UI + Daemon)

The UI launches the daemon as a separate JVM process communicating via gRPC over Unix Domain Sockets, and the `isma-lsp` language server as a separate process communicating via LSP over stdio (syntax highlighting). The daemon in turn launches two standalone CLIs as separate processes: the LISMA compiler CLI (`isma-compiler:cli`) to compile LISMA sources to a compiled `.class` file (LISMA on stdin, `.class` on stdout), and the simulation runner CLI (`isma-runner`) to run each simulation and write the result file.

- **isma-ui/external-services/** - gRPC client layer
  - `GrpcSimulationClient` - Netty-based gRPC client with Unix socket transport
  - `SimulationServerManager` - Manages daemon process lifecycle (start/stop)
  - `SimulationServerFacade` - High-level facade for simulation operations
  - `lsp/` - LSP client layer (`LspProcessManager`, `LspClient`, `LspTransport`) driving `isma-lsp`
- **Daemon launch** — `ISMA_DAEMON_SCRIPT` env var (bundle) or `isma.daemon.script` sysprop (IDE/Gradle)
- **LSP launch** — `ISMA_LSP_SCRIPT` env var (bundle) or `isma.lsp.script` sysprop (IDE/Gradle)
- **Compiler CLI launch** — `ISMA_COMPILER_CLI` env var or `isma.compiler.cli` sysprop (path to launch script, jar, or lib dir); fallback: the daemon's own `lib/` directory (the CLI jar ships there via `runtimeOnly`)
- **Runner CLI launch** — `ISMA_RUNNER_CLI` env var or `isma.runner.cli` sysprop (path to launch script, jar, or lib dir); fallback: the daemon's own `lib/` directory (the runner jar ships there via `runtimeOnly`)
- **Bundle** — `.ci-cd/build-bundle.sh` creates `build/bundle/` with all apps + `run-ui.sh` launcher

---

## Gradle Configuration

- **Centralized versions:** `gradle/libs.versions.toml`
- **All dependency/plugin versions** must be defined in `libs.versions.toml` with aliases
- Use `{ version.ref = "..." }` for version references
- No duplicate `group`, `version`, or `repositories` declarations
- `java.sourceCompatibility` and `java.targetCompatibility` must match

## Java Modules

All applications in this repository should use `module-info.java` for proper module system configuration.


## Git Submodules

- `protobuf-contracts/` → https://github.com/SmallBossGames/isma-contracts-proto
- `isma-ui/` → https://github.com/SmallBossGames/isma-ui-javafx
- `grin/` → https://github.com/SmallBossGames/grin-javafx

Fresh clones need `git submodule update --init --recursive` before building.
`isma-ui` and `grin` are standalone Gradle projects — build them with their own `./gradlew`, not the root one.

## Build

```bash
./gradlew build          # Build all modules
./gradlew :module:build  # Build specific module
./.ci-cd/build-bundle.sh # Build application distribution bundle UI + daemon
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

All protobuf definitions live in `protobuf-contracts/v1/` (git submodule). Proto changes require rebuilding the `isma-daemon:grpc` module in this repo, and syncing the `protobuf-contracts/` copy in the isma-ui-javafx repo and rebuilding `isma-ui:grpc` there.
