# ISMA Daemon — Technical Documentation

The daemon is a pure orchestrator: it launches the LISMA compiler CLI and the
simulation runner CLI as separate processes and coordinates them over a frozen
gRPC API. It performs no LISMA translation and no in-process simulation.

Quick navigation to detailed documents:

| Document | Description |
|----------|-------------|
| [01-overview.md](01-overview.md) | Architecture overview, module structure, design principles, communication flows |
| [02-grpc-api.md](02-grpc-api.md) | Complete gRPC API reference: both services, all RPCs, request/response types, error mapping |
| [03-domain-layer.md](03-domain-layer.md) | Domain handlers, entities, interfaces, DI configuration |
| [04-infrastructure-layer.md](04-infrastructure-layer.md) | Stores, simulation executor (runner CLI launcher), process registry, compiler CLI launcher |
| [05-app-layer.md](05-app-layer.md) | Entry point, gRPC services, HTTP server, DI wiring |
| [06-build-and-deployment.md](06-build-and-deployment.md) | Build configuration, dependencies, startup/shutdown, deployment |
| [07-transport-layer.md](07-transport-layer.md) | Unix domain sockets via Netty, Ktor CIO, client discovery, troubleshooting |

## Quick Start

### Building

Run `./gradlew :isma-daemon:app:build` to build the application module and all dependencies.

### Running (from source)

Launch the JAR with `--socket-path` and `--http-socket-path` CLI arguments to specify Unix socket paths. The built artifact is located at `build/libs/isma-daemon-app-1.0.0-SNAPSHOT.jar`. The daemon resolves the compiler CLI from the `lib/` directory next to its jar and the batteries-included runner from its own embedded distribution (`runner/lib`), or via `ISMA_COMPILER_CLI` / `ISMA_RUNNER_CLI`.

### Interacting

Use `grpcurl` with the `-plaintext` flag and the `-grpc-header ":pseudo-protocol-version:1.0.0"` header to invoke gRPC methods over the Unix socket. For simulation result downloads, use `curl` with the `--unix-socket` flag pointing to the HTTP socket path and request the `/simulation/{id}/download` endpoint.

## Key Files

| Path | Purpose |
|------|---------|
| `isma-daemon/app/src/main/kotlin/.../Application.kt` | Daemon entry point, CLI parsing, server lifecycle |
| `isma-daemon/app/src/main/kotlin/.../AppModule.kt` | Koin DI module (gRPC service bindings) |
| `isma-daemon/app/src/main/kotlin/.../grpc/SimulationServiceGrpcImpl.kt` | gRPC SimulationService: 5 RPC methods |
| `isma-daemon/app/src/main/kotlin/.../grpc/LismaCompilerServiceGrpcImpl.kt` | gRPC LismaCompilerService: 3 RPC methods |
| `isma-daemon/app/src/main/kotlin/.../http/HttpRoutes.kt` | Ktor HTTP routes (`GET /simulation/{id}/download`) |
| `isma-daemon/domain/src/main/kotlin/.../handlers/runSimulation/RunSimulationHandlerImpl.kt` | Simulation start logic |
| `isma-daemon/domain/src/main/kotlin/.../handlers/runSimulation/RunSimulationParameters.kt` | Simulation parameters data class |
| `isma-daemon/domain/src/main/kotlin/.../handlers/monitorSimulation/MonitorSimulationHandlerImpl.kt` | Polling progress updates |
| `isma-daemon/domain/src/main/kotlin/.../handlers/compileLisma/CompileLismaHandlerImpl.kt` | LISMA → compiled `.class` (via compiler CLI) |
| `isma-daemon/domain/src/main/kotlin/.../handlers/validateLisma/ValidateLismaHandlerImpl.kt` | LISMA validation (via compiler CLI) |
| `isma-daemon/domain/src/main/kotlin/.../compiler/CompiledModel.kt` | Compiled model (holds the `.class` bytes) |
| `isma-daemon/domain/src/main/kotlin/.../compiler/ILismaCompiler.kt` | Compiler interface (compile → artifact, validate → errors) |
| `isma-daemon/domain/src/main/kotlin/.../simulation/ISimulationProcessRegistry.kt` | Process registry for cancellation |
| `isma-daemon/domain/src/main/kotlin/.../DomainModule.kt` | DI configuration (handler interfaces → implementations) |
| `isma-daemon/infrastructure/src/main/kotlin/.../simulation/SimulationExecutorImpl.kt` | Launches the runner CLI, reads the runner's stdout (deriving progress from the TIME column), completes/fails/cancels the session |
| `isma-daemon/infrastructure/src/main/kotlin/.../simulation/SimulationProcessRegistry.kt` | Tracks runner processes so CancelSimulation can kill them |
| `isma-daemon/infrastructure/src/main/kotlin/.../translation/CliLismaCompiler.kt` | Launches the compiler CLI (compile → artifact, validate → error lines) |
| `isma-daemon/infrastructure/src/main/kotlin/.../InfrastructureModule.kt` | DI configuration (stores, executor, process registry, compiler) |
| `isma-daemon/grpc/build.gradle.kts` | Protobuf/gRPC code generation config |
| `isma-daemon/app/build.gradle.kts` | Application build config (main class, dependencies) |
