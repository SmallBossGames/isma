# isma-daemon

gRPC-based orchestration daemon for ISMA with Netty transport. The daemon is a
**pure orchestrator**: it does no LISMA translation and no in-process
simulation. It launches two standalone CLI tools as separate processes and
coordinates them:

- **LISMA compiler CLI** (`lisma-compiler:cli`) — compiles LISMA source to a
  compiled `.class` file (and validates LISMA).
- **Simulation runner CLI** (`isma-solver:cli`, batteries-included) — runs a simulation from a
  compiled `.class` file and streams the binary result records to stdout.

The gRPC API (protobuf contracts) is frozen; the daemon only orchestrates.

## Modules

| Module | Description |
|--------|-------------|
| **app/** | Main application entry point. Starts gRPC server with Netty transport, uses domain sockets for IPC. Also includes Ktor HTTP server for result downloads. |
| **domain/** | Domain layer - shared business logic with Koin DI. Contains handlers for simulation and compiler operations, plus domain interfaces for stores, executors, and the compiler. |
| **grpc/** | gRPC code generation from protobuf. Proto files sourced from `../../protobuf-contracts/v1/` |
| **infrastructure/** | Infrastructure layer - implements domain interfaces. Launches the compiler and runner CLIs as separate processes, tracks runner processes for cancellation, and holds the in-memory stores. |

## Architecture

```
app/
 └── Application.kt (main class)
      ├── Starts Koin DI (domainModule, infrastructureModule, appModule)
      ├── Creates gRPC server on Unix domain socket (SimulationService + LismaCompilerService)
      └── Creates Ktor HTTP server (CIO engine) for result downloads
       └── Imports: ru.nstu.isma.daemon.domain, ru.nstu.isma.daemon.grpc

grpc/
 └── Generates gRPC stubs from proto files
      └── Proto source: ../../protobuf-contracts/v1/

domain/
 └── Handlers (Koin DI)
      ├── Simulation handlers:
      │   ├── IRunSimulationHandler / RunSimulationHandlerImpl
      │   ├── IGetSimulationResultHandler / GetSimulationResultHandlerImpl
      │   ├── IMonitorSimulationHandler / MonitorSimulationHandlerImpl
      │   ├── ICancelSimulationHandler / CancelSimulationHandlerImpl
      │   └── IListSimulationMethodsHandler / ListSimulationMethodsHandlerImpl
      └── Compiler handlers:
          ├── ICompileLismaHandler / CompileLismaHandlerImpl
          ├── IValidateLismaHandler / ValidateLismaHandlerImpl
          └── IDeleteCompiledModelHandler / DeleteCompiledModelHandlerImpl

domain/simulation/ — ISimulationExecutor, ISimulationProcessRegistry, ISimulationSessionStore, SimulationSession, SimulationStatus
domain/compiler/   — ICompiledModelStore, CompiledModel (compiled `.class` bytes), ILismaCompiler
domain/integration/ — IIntegrationMethodsStore

infrastructure/
 ├── stores/
 │   ├── simulationSessions/ — ISimulationSessionStore implementation
 │   ├── compiledModels/     — ICompiledModelStore implementation
 │   └── integrationMethods/ — IIntegrationMethodsStore (ServiceLoader for IIntegrationMethodFactory)
 ├── simulation/
 │   ├── SimulationExecutorImpl — launches the simulation-runner CLI, writes the `.class` to a temp file, reads the runner's stdout (deriving progress from the TIME column), and completes/fails/cancels the session
 │   └── SimulationProcessRegistry — tracks runner processes so CancelSimulation can kill them
 └── translation/
     └── CliLismaCompiler — launches the lisma-compiler CLI (compile → `.class`, validate → error lines)
```

## Orchestration (process model)

The daemon never loads model classes or runs the solver in-process. Each
operation is a separate process:

- **Compile / Validate** — `CliLismaCompiler` launches the compiler CLI. The
  CLI reads LISMA from stdin and writes the compiled `.class` to stdout
  (compile) or error lines to stdout (validate). The `.class` bytes are stored in
  `ICompiledModelStore`.
- **Run** — `SimulationExecutorImpl` writes the stored `.class` to a temp file,
  launches the runner CLI with the parameters, and reads the runner's stdout
  (which streams the binary result records, one per time step), deriving
  `progress = (t − t0) / (tEnd − t0)` from the TIME column to update the session.
  When the runner exits, the accumulated result is written to a file and the
  session is completed (result file path), failed, or left cancelled.
- **Cancel** — `CancelSimulationHandlerImpl` marks the session CANCELLED and
  `SimulationProcessRegistry` sends SIGTERM to the runner process. The runner's
  shutdown hook stops the integration loop.

### CLI resolution

Both CLIs resolve in the same order:
1. `ISMA_COMPILER_CLI` / `ISMA_RUNNER_CLI` env var, or `isma.compiler.cli` /
   `isma.runner.cli` system property — path to a launch script, a jar, or a
   lib directory.
2. Fallback: the lib directory next to the daemon's jar (installDist layout),
   running the CLI main class via `java -cp <libdir>/* <MainClass>`.

Runner main class: `ru.nstu.isma.runner.SimulationRunnerCliKt`.
Compiler main class: `ru.nstu.isma.compiler.cli.LismaCompilerCliKt`.

## Handler Responsibilities

| Handler | Purpose |
|---------|---------|
| `IRunSimulationHandler` | Starts a simulation, returns running simulation result |
| `IGetSimulationResultHandler` | Retrieves completed simulation results |
| `IMonitorSimulationHandler` | Monitors simulation progress (progress %, step, time) |
| `ICancelSimulationHandler` | Cancels a running simulation (kills the runner process) |
| `IListSimulationMethodsHandler` | Lists available integration methods (Euler, RK2, RK3, RK31, RKF, RKM) |
| `ICompileLismaHandler` | Compiles LISMA source to a compiled `.class` (via the compiler CLI) |
| `IValidateLismaHandler` | Validates LISMA source code (via the compiler CLI) |
| `IDeleteCompiledModelHandler` | Deletes a compiled model |

## gRPC Services

| Service | RPC Methods |
|---------|-------------|
| `SimulationService` | RunSimulation, GetSimulationResult, MonitorSimulation, CancelSimulation, ListSimulationMethods |
| `LismaCompilerService` | Compile, Validate, DeleteCompiledModel |

## Key Dependencies

| Dependency | Purpose |
|------------|---------|
| lisma-compiler:cli | Standalone LISMA compiler CLI (thin wrapper over the `lisma-compiler:lib` public API) — launched as a separate process (runtimeOnly, resolved via `ISMA_COMPILER_CLI` env / `isma.compiler.cli` sysprop, fallback to app lib dir) |
| isma-solver:cli | Batteries-included simulation runner CLI — launched as a separate process per simulation (embedded as its own distribution via `installRunnerDist`, resolved via `ISMA_RUNNER_CLI` env / `isma.runner.cli` sysprop, fallback to the runner's own `runner/lib`); also answers `ListSimulationMethods` via `--list-methods` |
| grpc-netty | Netty-based gRPC server transport |
| netty-transport + netty-transport-classes-epoll | Epoll event loop for Linux domain sockets |
| grpc-stub, grpc-protobuf, grpc-java | gRPC API and generated code |
| protobuf-java | Protocol buffers |
| kotlin-reflect, koin-core | DI and reflection |
| ktor-server-core, ktor-server-cio | Ktor HTTP server (for result downloads) |

## Proto Contracts

gRPC stubs are generated from `../../protobuf-contracts/v1/`. Two services:

- **simulation_service** — `run_simulation_request.proto` with key messages:
  - `AccuracyConfig` (field 5) — enables adaptive step size control; presence of the message = enabled
  - `StabilityConfig` (field 6) — enables stability control for RK methods; presence = enabled
  - `EventDetectionConfig` (field 8) — enables event detection with gamma/low_border parameters

- **compiler_service** — LISMA compilation and validation endpoints

Proto changes require rebuilding `isma-daemon:grpc` in this repo. The `isma-ui:grpc` module lives in the isma-ui-javafx repo (which keeps its own copy of `protobuf-contracts`), so that copy must be synced and rebuilt there as well.

## Build

```bash
./gradlew :isma-daemon:app:build
```
