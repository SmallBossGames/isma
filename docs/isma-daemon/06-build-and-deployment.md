# Build & Deployment

## Build Configuration

### Gradle Modules

| Module | Purpose |
|--------|---------|
| `:isma-daemon:grpc` | gRPC code generation (protobuf → Java) |
| `:isma-daemon:domain` | Domain interfaces + handler implementations |
| `:isma-daemon:infrastructure` | Concrete store/executor/compiler implementations |
| `:isma-daemon:app` | Application entry point + gRPC/HTTP services |

### Build Scripts

#### `grpc/build.gradle.kts`

See `grpc/build.gradle.kts` for the full configuration. It applies the `google.protobuf` and `java.modules` plugins, configures `protoc` and `protoc-gen-grpc-java` code generation, sets the proto source directory to `../../protobuf-contracts`, and depends on gRPC and protobuf libraries.

**Code generation:** Protobuf files from `protobuf-contracts/v1/` are compiled into Java gRPC stubs. The generated classes include service interfaces (`SimulationServiceGrpc`, `LismaCompilerServiceGrpc`), message classes (`RunSimulationRequest`, `CompileResponse`, etc.), and builder classes for all messages.

**Important:** Proto changes require rebuilding this module. Generated code should NOT be committed (it's derived from `.proto` files).

#### `domain/build.gradle.kts`

See `domain/build.gradle.kts` for the full configuration. It applies `kotlin.jvm` and `java.modules` plugins and depends only on `koin-core`. Domain holds no compiler or solver modules: compiled models are stored as opaque `.class` bytes and all compilation happens in the compiler CLI process.

#### `infrastructure/build.gradle.kts`

See `infrastructure/build.gradle.kts` for the full configuration. It applies `kotlin.jvm` and `java.modules` plugins and depends on the domain module, `koin-core`, and `slf4j-api`. The daemon is a pure orchestrator, so it holds no solver or compiler modules in-process; `ListSimulationMethods` asks the runner CLI for its method list.

#### `app/build.gradle.kts`

See `app/build.gradle.kts` for the full configuration. It applies `kotlin.jvm`, `java.modules`, and `application` plugins, sets `mainClass` to `ru.nstu.isma.daemon.app.ApplicationKt`, and depends on all three internal modules, gRPC/Netty libraries, Kotlin reflection, Koin, SLF4J/Logback, and Ktor server libraries. It depends on `lisma-compiler:cli` via `runtimeOnly` (the compiler CLI jar lands in the distribution's `lib/` directory). The batteries-included runner (`isma-solver:cli`) is embedded as its own distribution via the `installRunnerDist` task (its full `bin` + `lib`, including the integration-method jars, lands in `runner/`), so the daemon launches the runner from its own lib.

### Dependency Versions

All versions are centralized in `gradle/libs.versions.toml`:

| Library | Version |
|---------|---------|
| Kotlin | 2.3.20 |
| gRPC | 1.80.0 |
| Protobuf | 4.34.1 |
| Netty | 4.2.10.Final |
| Ktor | 3.4.1 |
| Koin | 4.2.0 |
| SLF4J | 2.0.17 |
| Logback | 1.5.32 |
| ANTLR4 | 4.13.2 |
| Guava | 33.5.0-jre |

---

## Runtime Dependencies

### Project Dependencies

| Module | Provides | Used by |
|--------|----------|---------|
| `lisma-compiler:cli` | Standalone LISMA compiler CLI (launched as a process; jar ships in `lib/` via `runtimeOnly`) | app (runtimeOnly), infrastructure (launches it) |
| `isma-solver:cli` | Batteries-included simulation runner CLI (launched as a process per simulation; ships integration-method libs; embedded as its own distribution via `installRunnerDist`) | app (installRunnerDist), infrastructure (launches it) |

### Third-Party Dependencies

| Library | Purpose |
|---------|---------|
| Netty (with Epoll) | gRPC server transport + Unix domain sockets |
| gRPC (Netty + Protobuf) | RPC framework |
| Koin | Lightweight DI framework |
| Ktor (CIO) | Embedded HTTP server |
| SLF4J + Logback | Logging |
| Kotlin reflection | Koin runtime support |

---

## Startup Sequence

```mermaid
flowchart TD
    A["1. Parse CLI arguments\n(--socket-path, --http-socket-path)"] --> B["2. startKoin"]

    subgraph Koin["startKoin { domain, infrastructure, app }"]
        direction LR
        K1["IntegrationMethodsStore\n(ServiceLoader)"]
        K2["IntegrationMethodLibraryLoader.load()"]
        K3["SimulationProcessRegistry()"]
        K4["CliLismaCompiler()"]
        K5["Executors.newCachedThreadPool()"]
        K6["Handler instances\n(injected deps)"]
    end

    B --> Koin
    Koin --> C["3. Delete stale socket files"]
    C --> D["4. Create Netty Epoll\nevent loop groups\n(boss + worker)"]
    D --> E["5. Build gRPC server"]

    subgraph GrpcBuild["gRPC Server Build"]
        direction LR
        G1["Bind DomainSocketAddress\n(socketPath)"]
        G2["Register SimulationServiceGrpcImpl"]
        G3["Register LismaCompilerServiceGrpcImpl"]
        G4["Register ProtoReflectionServiceV1"]
    end

    E --> GrpcBuild
    GrpcBuild --> F["6. Start gRPC server (.start())"]
    F --> G["7. Create Ktor HTTP server\n(CIO engine, Unix socket)"]
    G --> H["8. Register HTTP route\nGET /simulation/{id}/download"]
    H --> I["9. Start HTTP server (.start(false))"]
    I --> J["10. Print GRPC_SOCKET / HTTP_SOCKET"]
    J --> K["11. Register shutdown hook"]
    K --> L["12. grpcServer.awaitTermination()\n(blocks main thread)"]

    classDef step fill:#e1f5fe,stroke:#0288d1,stroke-width:2px
    classDef subgraph fill:#f3e5f5,stroke:#7b1fa2,stroke-width:1px
    class A,B,C,D,F,G,H,I,J,K,L step
    class Koin,GrpcBuild subgraph
```

See `Application.kt` for the full startup sequence. The function performs 12 steps: (1) parse CLI arguments (`--socket-path`, `--http-socket-path`), (2) call `startKoin { modules(domainModule, infrastructureModule, appModule) }` — initializes `IntegrationMethodsStore` (ServiceLoader), `IntegrationMethodLibraryLoader`, `SimulationProcessRegistry`, `CliLismaCompiler`, `Executors.newCachedThreadPool()`, and all handler instances, (3) delete stale socket files, (4) create Netty Epoll event loop groups (boss + worker), (5) build gRPC server — binds to `DomainSocketAddress`, registers `SimulationServiceGrpcImpl`, `LismaCompilerServiceGrpcImpl`, and `ProtoReflectionServiceV1`, (6) start gRPC server (`.start()`), (7) create Ktor HTTP server (CIO engine, Unix socket), (8) register the HTTP route, (9) start the HTTP server, (10) print `GRPC_SOCKET` / `HTTP_SOCKET`, (11) register the shutdown hook, (12) block on `grpcServer.awaitTermination()`.

---

## Shutdown Sequence

```mermaid
flowchart LR
    ShutdownHook["ShutdownHook.run()"] --> S1["1. grpcServer.shutdown()\n(stop accepting new requests)"]
    S1 --> S2["2. httpServer.stop(1s/2s)\n(stop HTTP server)"]
    S2 --> S3["3. bossGroup.shutdownGracefully()\n(close boss event loop)"]
    S3 --> S4["4. workerGroup.shutdownGracefully()\n(close worker event loop)"]
    S4 --> S5["5. File(socketPath).delete()\n(remove gRPC socket file)"]
    S5 --> S6["6. File(httpSocketPath).delete()\n(remove HTTP socket file)"]

    classDef hook fill:#ffebee,stroke:#c62828,stroke-width:2px
    classDef action fill:#fff3e0,stroke:#f57c00,stroke-width:2px
    class ShutdownHook hook
    class S1,S2,S3,S4,S5,S6 action
```

See `Application.kt` for the shutdown hook implementation. The shutdown proceeds in 6 steps: (1) `grpcServer.shutdown()` — stop accepting new requests, (2) `httpServer.stop(1, 2, SECONDS)` — stop HTTP server, (3) `bossGroup.shutdownGracefully()` — close boss event loop, (4) `workerGroup.shutdownGracefully()` — close worker event loop, (5) `File(socketPath).delete()` — remove gRPC socket file, (6) `File(httpSocketPath).delete()` — remove HTTP socket file.

---

## Build Commands

| Command | Purpose |
|---------|---------|
| `./gradlew :isma-daemon:build` | Build entire isma-daemon module (all 4 submodules) |
| `./gradlew :isma-daemon:app:build` | Build just the application (includes all dependencies) |
| `./gradlew :isma-daemon:grpc:generateProto` | Generate gRPC stubs only (after proto changes) |
| `./gradlew :isma-daemon:test` | Build with tests |
| `./.ci-cd/build-bundle.sh` | Build distribution bundle (UI + daemon together) |

### Common Issues

| Problem | Solution |
|---------|----------|
| Proto files not found | Run `./gradlew :isma-daemon:grpc:generateProto` after pulling proto changes |
| Missing Epoll native library | Server requires Linux x86_64; Epoll classifier `linux-x86_64` must be available |
| Socket file already exists | Server auto-deletes stale sockets at startup; manually `rm /tmp/isma-*.sock` if needed |
| ServiceLoader finds no integration methods | Ensure `isma-solver:lib:euler`, `lib:rk2`, etc. are in the classpath and have `META-INF/services/ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory` entries |

---

## Deployment

### Process Launch

The daemon is launched by the ISMA UI process (`SimulationServerManager`) as a separate JVM. The UI:

1. Determines the daemon launch script (from `ISMA_DAEMON_SCRIPT` env var or `isma.daemon.script` system property)
2. Launches the daemon process
3. Reads `GRPC_SOCKET` and `HTTP_SOCKET` from stdout
4. Connects to the gRPC server via the Unix socket path
5. Connects to the HTTP server for file downloads

### Bundle Distribution

The `.ci-cd/build-bundle.sh` script creates a distribution bundle at `build/bundle/` containing:
- `isma-daemon-app` executable (the daemon, with the compiler CLI jar in its `lib/` and the runner distribution embedded in `runner/`)
- `isma-ui-app` executable
- `grin-app` executable
- `isma-lsp-app` executable
- `lisma-compiler-cli-app` executable — standalone LISMA compiler CLI (launch script at `bin/lisma-compiler-cli`)
- `simulation-runner-cli-app` executable — standalone batteries-included simulation runner CLI (launch script at `bin/simulation-runner-cli`)
- `run-ui.sh` launcher script (sets `ISMA_DAEMON_SCRIPT`, `ISMA_GRIN_SCRIPT`, `ISMA_LSP_SCRIPT`, `ISMA_COMPILER_CLI`, `ISMA_RUNNER_CLI`)

The standalone CLI distributions are the script paths for UIs that launch the CLIs directly (e.g. the Avalonia UI: `Compiler:ScriptPath` / `Runner:ScriptPath` in `appsettings.json`, or `ISMA_COMPILER_CLI` / `ISMA_RUNNER_CLI` env vars).

### Platform Requirements

| Requirement | Details |
|-------------|---------|
| **OS** | Linux (Epoll dependency) |
| **Architecture** | x86_64 (native Epoll classifier) |
| **Java** | JVM compatible with Kotlin 2.3.20 |
| **Unix sockets** | Required (not TCP/IP) |

---

## Configuration

### Runtime Configuration

| Method | Parameters |
|--------|------------|
| CLI arguments | `--socket-path <path>` `--http-socket-path <path>` |
| System properties | `java.io.tmpdir` (default socket location) |

### Logging

Configured via `logback.xml`:
- All output to stdout
- Console appender with timestamp, thread, level, logger name, message
- Third-party frameworks at INFO level

### gRPC Reflection

Enabled by default via `ProtoReflectionServiceV1.newInstance()`. Allows:
- `grpcurl` command-line tool to introspect the API
- IDE plugins to auto-generate client code
- Runtime service discovery
