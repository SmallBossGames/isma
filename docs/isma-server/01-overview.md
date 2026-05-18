# ISMA Server Architecture Overview

## Purpose

`isma-server` is a gRPC-based server application for the ISMA (Instrumental Modeling) mathematical modeling environment. It provides simulation execution, model compilation, and LISMA language services via a gRPC API using Unix domain sockets for inter-process communication.

## Position in the ISMA Architecture

```mermaid
flowchart TB
    UI["🖥 ISMA UI\n(JavaFX / Blueprint Editor)"] -->|"gRPC over\nUnix Domain Socket"| Server
    subgraph Server["isma-server"]
        direction LR
        App["📦 app/"]
        Dom["📦 domain/"]
        Infra["📦 infrastructure/"]
    end
    Server -->|depends on| ExtDeps["External Dependencies"]
    subgraph ExtDeps["External Libraries"]
        direction LR
        Solver["isma-solver:lib-meta\nSolver Methods"]
        Compiler["isma-compiler/\nHSM / FDM / JVM"]
        Next["isma-next-core\nHSM Compiler / FDM"]
    end
    App <--> Dom
    Dom <--> Infra
    App -->|imports| Dom
    Infra -->|implements| Dom
```

The server communicates with the UI via a **two-service gRPC architecture**:

1. **SimulationService** — Run, monitor, and manage numerical simulations
2. **LismaCompilerService** — Compile, validate, and highlight LISMA source code

## Module Structure

```
isma-server/
├── app/                  # Application entry point, gRPC & HTTP services
│   └── src/main/kotlin/ru/nstu/isma/server/app/
│       ├── Application.kt            # Main class, server startup
│       ├── AppModule.kt              # Koin DI module (gRPC service bindings)
│       ├── grpc/
│       │   ├── SimulationServiceGrpcImpl.kt
│       │   └── LismaCompilerServiceGrpcImpl.kt
│       └── http/
│           └── HttpRoutes.kt         # Ktor HTTP routes (result download)
│
├── domain/               # Domain layer: interfaces + handler implementations
│   └── src/main/kotlin/ru/nstu/isma/domain/
│       ├── DomainModule.kt           # Koin DI module (handler bindings)
│       ├── compiler/
│       │   └── ICompiledModelStore.kt
│       ├── handlers/
│       │   ├── cancelSimulation/
│       │   ├── compileLisma/
│       │   ├── deleteCompiledModel/
│       │   ├── getSimulationResult/
│       │   ├── highlightLisma/
│       │   ├── listSimulationMethods/
│       │   ├── monitorSimulation/
│       │   ├── runSimulation/
│       │   └── validateLisma/
│       ├── integration/
│       │   └── IIntegrationMethodsStore.kt
│       └── simulation/
│           ├── ISimulationExecutor.kt
│           ├── ISimulationSessionStore.kt
│           ├── SimulationSession.kt
│           └── SimulationStatus.kt
│
├── infrastructure/       # Infrastructure layer: concrete implementations
│   └── src/main/kotlin/ru/nstu/isma/server/infrastructure/
│       ├── InfrastructureModule.kt   # Koin DI module (infrastructure bindings)
│       ├── highlight/
│       │   └── HighlightLismaHandlerImpl.kt
│       ├── simulation/
│       │   └── SimulationExecutorImpl.kt
│       ├── stores/
│       │   ├── compiledModels/CompiledModelStore.kt
│       │   ├── integrationMethods/IntegrationMethodsStore.kt
│       │   └── simulationSessions/SimulationSessionStore.kt
│       └── translation/
│           └── LismaTranslatorImpl.kt
│
└── grpc/                 # gRPC code generation (proto → Java stubs)
    └── Protobuf source: ../../protobuf-contracts/
```

### Module Dependencies

```mermaid
flowchart LR
    Grpc["📦 :isma-server:grpc\n(generated stubs, no project deps)"]
    Domain["📦 :isma-server:domain\n(handlers + interfaces)"]
    Infra["📦 :isma-server:infrastructure\n(concrete implementations)"]
    App["📦 :isma-server:app\n(entry point + services)"]
    Ext1["isma-solver:api\nisma-compiler:hsm-core\nisma-next-core\nkoin-core"]
    Ext2["isma-solver:core/lib-utils\nisma-compiler:hsm-fdm/jvm/translator\nantlr4-runtime"]
    Ext3["isma-solver:lib-meta\ngRPC/Netty/Ktor libs"]
    Grpc -.->|no project deps| Ext1
    Domain -->|depends on| Ext1
    Infra -->|depends on| Ext2
    App -->|depends on| Domain
    App -->|depends on| Grpc
    App -->|depends on| Infra
    App -->|depends on| Ext3
```

## Design Principles

### 1. Interface-Based Domain Layer

The `domain/` module contains both **interfaces** and **handler implementations**, following a hexagonal architecture pattern. Interfaces define contracts; implementations are in the `infrastructure/` module. The `domain/` module's handler implementations depend on infrastructure interfaces declared in the same module.

```mermaid
flowchart LR
    subgraph Domain["📦 domain/"]
        Dir["Domain Interfaces"]
        Dih["Domain Handler Impls"]
    end
    subgraph Infra["📦 infrastructure/"]
        Ish["Infrastructure Impls"]
    end
    Dir -->|"declares"| Dih
    Dih -->|depends on| Ish
    Ish -->|"implements"| Dir
```

### 2. Koin Dependency Injection

Three Koin modules are layered:

| Module                 | Contents                                                          |
| ---------------------- | ----------------------------------------------------------------- |
| `domainModule`         | All handler interfaces → implementations                          |
| `infrastructureModule` | All store interfaces → implementations, external service wrappers |
| `appModule`            | gRPC service implementations that wire handlers together          |

```kotlin
// Startup order (appModule depends on the others)
startKoin {
    modules(domainModule, infrastructureModule, appModule)
}
```

### 3. Thread Pool Architecture

| Component            | Threading                                                        |
| -------------------- | ---------------------------------------------------------------- |
| gRPC calls           | Netty event loops (per-call)                                     |
| Simulation execution | Java `ExecutorService` (cached thread pool)                      |
| Virtual threads      | Used within simulation for result writing (`Thread.ofVirtual()`) |
| Session stores       | `ConcurrentHashMap` for lock-free thread safety                  |

## Communication Flow

### Simulation Lifecycle

```mermaid
sequenceDiagram
    participant Client
    participant Server as Server (gRPC)<br/>SimulationServiceGrpcImpl
    participant Domain as Domain<br/>RunSimulationHandlerImpl
    participant Infra as Infrastructure<br/>SimulationSessionStore / ExecutorService
    participant HTTP as HTTP<br/>Ktor Server

    Client->>Server: RunSimulationRequest
    Server->>Domain: handle(RunSimulationParameters)
    Domain->>Infra: get HSM model from CompiledModelStore
    Domain->>Infra: create session
    Domain->>Infra: execute() → submit to ExecutorService
    Domain-->>Server: RunningSimulationResult(simulationId)
    Server-->>Client: RunSimulationResponse (simulationId)

    Note over Infra: async simulation run (ExecutorService thread)

    Client->>Server: MonitorSimulationRequest
    Server->>Infra: query session state
    Infra-->>Server: SimulationSession
    Server-->>Client: MonitorSimulationResponse (progress)

    Client->>Server: CancelSimulationRequest
    Server->>Infra: updateStatus(CANCELLED)
    Note over Infra: Simulation detects CANCELLED<br/>throws InterruptedException

    Client->>Server: GetSimulationResultRequest
    Server->>Infra: validate session is COMPLETED
    Server-->>Client: Download URL /simulation/{id}/download

    Client->>HTTP: GET /simulation/{id}/download
    HTTP->>Infra: read result file (.bin)
    HTTP-->>Client: Result file data
```

### Model Compilation Flow

```mermaid
sequenceDiagram
    participant Client
    participant CompilerService as LismaCompilerServiceGrpcImpl
    participant CompileHandler as CompileLismaHandlerImpl
    participant Translator as LismaTranslatorImpl
    participant ModelStore as CompiledModelStore

    Client->>CompilerService: CompileRequest(lismaSourceCode)
    CompilerService->>CompileHandler: handle(sourceCode)
    CompileHandler->>Translator: translate(sourceCode)
    Translator->>Translator: LISMA source → HSM model
    alt model.isPDE
        Translator->>Translator: FDMConverter.convert()
    end
    Translator-->>CompileHandler: Result<HSM>
    CompileHandler->>ModelStore: create(hsm)
    ModelStore-->>CompileHandler: UUID
    CompileHandler-->>CompilerService: CompileLismaResult
    CompilerService-->>Client: CompileResponse(compiledModelId)

    alt translation failed
        CompileHandler->>Translator: validate(sourceCode)
        Translator-->>CompileHandler: IsmaErrorList
        CompileHandler-->>CompilerService: CompileLismaResult(errors)
        CompilerService-->>Client: CompileResponse(errors)
    end
```

## Runtime Characteristics

- **Transport**: Unix domain sockets (Linux) via Netty Epoll
- **Two sockets**: One for gRPC simulation, one for Ktor HTTP downloads
- **Socket lifecycle**: Created at startup, cleaned up on shutdown
- **Default socket paths**: `{java.io.tmpdir}/isma-{UUID}.sock` and `{java.io.tmpdir}/isma-http-{UUID}.sock`
- **Customization**: Via `--socket-path` and `--http-socket-path` CLI arguments, or `isma.server.script` system property on the client side
- **Shutdown hook**: Stops gRPC, Ktor, closes Netty groups, deletes socket files

## Error Handling

gRPC errors are mapped from Kotlin exceptions:

| Exception                  | gRPC Status           | Meaning                   |
| -------------------------- | --------------------- | ------------------------- |
| `IllegalArgumentException` | `NOT_FOUND`           | Resource not found        |
| `IllegalStateException`    | `FAILED_PRECONDITION` | Wrong state for operation |
| Other `Exception`          | `INTERNAL`            | Unexpected error          |

HTTP errors use standard Ktor `HttpResponseContent`:

| Condition                             | HTTP Status                            |
| ------------------------------------- | -------------------------------------- |
| Invalid simulation ID                 | 400 Bad Request                        |
| Simulation not found or not completed | 404 Not Found                          |
| Result file missing                   | 404 Not Found                          |
| Success                               | 200 OK with `application/octet-stream` |
