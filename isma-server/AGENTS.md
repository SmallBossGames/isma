# isma-server

gRPC-based server for ISMA with Netty transport layer.

## Modules

| Module | Description |
|--------|-------------|
| **app/** | Main application entry point. Starts gRPC server with Netty transport, uses domain sockets for IPC. Also includes Ktor HTTP server for result downloads. |
| **domain/** | Domain layer - shared business logic with Koin DI. Contains handlers for simulation and compiler operations, plus domain interfaces for stores and executors. |
| **grpc/** | gRPC code generation from protobuf. Proto files sourced from `../../protobuf-contracts/v1/` |
| **infrastructure/** | Infrastructure layer - implements domain interfaces. Uses ServiceLoader for integration methods, includes compilers (HSM, FDM, JVM), simulation executor, and stores. |

## Architecture

```
app/
 └── Application.kt (main class)
      ├── Starts Koin DI (domainModule, infrastructureModule, appModule)
      ├── Creates gRPC server on Unix domain socket (SimulationService + LismaCompilerService)
      └── Creates Ktor HTTP server (CIO engine) for result downloads
       └── Imports: ru.nstu.isma.server.domain, ru.nstu.isma.server.grpc

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
          ├── IDeleteCompiledModelHandler / DeleteCompiledModelHandlerImpl
          └── IHighlightLismaHandler / HighlightLismaHandlerImpl (infra impl)

domain/simulation/ — ISimulationExecutor, ISimulationSessionStore, SimulationSession, SimulationStatus
domain/compiler/   — ICompiledModelStore
domain/integration/ — IIntegrationMethodsStore

infrastructure/
 ├── stores/
 │   ├── simulationSessions/ — ISimulationSessionStore implementation
 │   ├── compiledModels/     — ICompiledModelStore implementation
 │   └── integrationMethods/ — IIntegrationMethodsStore (ServiceLoader for IIntegrationMethodFactory)
 ├── simulation/
 │   └── SimulationExecutorImpl — Virtual threads, result writing
 ├── translation/
 │   └── LismaTranslatorImpl — LISMA → HSM translation with FDM support
 └── highlight/
     └── HighlightLismaHandlerImpl — ANTLR4-based syntax highlighting
```

## Handler Responsibilities

| Handler | Purpose |
|---------|---------|
| `IRunSimulationHandler` | Starts a simulation, returns running simulation result |
| `IGetSimulationResultHandler` | Retrieves completed simulation results |
| `IMonitorSimulationHandler` | Monitors simulation progress (progress %, step, time) |
| `ICancelSimulationHandler` | Cancels a running simulation |
| `IListSimulationMethodsHandler` | Lists available integration methods (Euler, RK2, RK3, RK31, RKF, RKM) |
| `ICompileLismaHandler` | Compiles LISMA source code to HSM model |
| `IValidateLismaHandler` | Validates LISMA source code |
| `IDeleteCompiledModelHandler` | Deletes a compiled model |
| `IHighlightLismaHandler` | Syntax highlighting for LISMA source (ANTLR4-based) |

## gRPC Services

| Service | RPC Methods |
|---------|-------------|
| `SimulationService` | RunSimulation, GetSimulationResult, MonitorSimulation, CancelSimulation, ListSimulationMethods |
| `LismaCompilerService` | Compile, Validate, DeleteCompiledModel, HighlightLisma |

## Key Dependencies

| Dependency | Purpose |
|------------|---------|
| isma-solver:lib-meta | SPI interface for integration method factories |
| isma-solver:api, isma-solver:core | Solver API and implementation |
| isma-compiler:hsm-core, hsm-fdm, hsm-jvm | HSM compiler components |
| grpc-netty | Netty-based gRPC server transport |
| netty-transport + netty-transport-classes-epoll | Epoll event loop for Linux domain sockets |
| grpc-stub, grpc-protobuf, grpc-java | gRPC API and generated code |
| protobuf-java | Protocol buffers |
| kotlin-reflect, koin-core | DI and reflection |
| ktor-server-core, ktor-server-cio | Ktor HTTP server (for result downloads) |
| antlr4-runtime | Syntax highlighting for LISMA |

## Proto Contracts

gRPC stubs are generated from `../../protobuf-contracts/v1/`. Two services:

- **simulation_service** — `run_simulation_request.proto` with key messages:
  - `AccuracyConfig` (field 5) — enables adaptive step size control; presence of the message = enabled
  - `StabilityConfig` (field 6) — enables stability control for RK methods; presence = enabled
  - `EventDetectionConfig` (field 8) — enables event detection with gamma/low_border parameters

- **compiler_service** — LISMA compilation and validation endpoints

Proto changes require rebuilding both `isma-server:grpc` and `isma-ui:grpc`.

## Build

```bash
./gradlew :isma-server:app:build
```
