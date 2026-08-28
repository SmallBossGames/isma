# ISMA Server — Technical Documentation

Quick navigation to detailed documents:

| Document | Description |
|----------|-------------|
| [01-overview.md](01-overview.md) | Architecture overview, module structure, design principles, communication flows |
| [02-grpc-api.md](02-grpc-api.md) | Complete gRPC API reference: both services, all RPCs, request/response types, error mapping |
| [03-domain-layer.md](03-domain-layer.md) | Domain handlers, entities, interfaces, DI configuration |
| [04-infrastructure-layer.md](04-infrastructure-layer.md) | Stores, simulation executor, translator, syntax highlighting |
| [05-app-layer.md](05-app-layer.md) | Entry point, gRPC services, HTTP server, DI wiring |
| [06-build-and-deployment.md](06-build-and-deployment.md) | Build configuration, dependencies, startup/shutdown, deployment |
| [07-transport-layer.md](07-transport-layer.md) | Unix domain sockets via Netty, Ktor CIO, client discovery, troubleshooting |

## Quick Start

### Building

Run `./gradlew :isma-server:app:build` to build the application module and all dependencies.

### Running (from source)

Launch the JAR with `--socket-path` and `--http-socket-path` CLI arguments to specify Unix socket paths. The built artifact is located at `build/libs/isma-server-app-1.0.0-SNAPSHOT.jar`.

### Interacting

Use `grpcurl` with the `-plaintext` flag and the `-grpc-header ":pseudo-protocol-version:1.0.0"` header to invoke gRPC methods over the Unix socket. For simulation result downloads, use `curl` with the `--unix-socket` flag pointing to the HTTP socket path and request the `/simulation/{id}/download` endpoint.

## Key Files

| Path | Purpose |
|------|---------|
| `isma-server/app/src/main/kotlin/.../Application.kt` | Server entry point, CLI parsing, server lifecycle |
| `isma-server/app/src/main/kotlin/.../AppModule.kt` | Koin DI module (gRPC service bindings) |
| `isma-server/app/src/main/kotlin/.../grpc/SimulationServiceGrpcImpl.kt` | gRPC SimulationService: 5 RPC methods |
| `isma-server/app/src/main/kotlin/.../grpc/LismaCompilerServiceGrpcImpl.kt` | gRPC LismaCompilerService: 4 RPC methods |
| `isma-server/app/src/main/kotlin/.../http/HttpRoutes.kt` | Ktor HTTP routes (`GET /simulation/{id}/download`) |
| `isma-server/domain/src/main/kotlin/.../handlers/runSimulation/RunSimulationHandlerImpl.kt` | Simulation start logic |
| `isma-server/domain/src/main/kotlin/.../handlers/runSimulation/RunSimulationParameters.kt` | Simulation parameters data class |
| `isma-server/domain/src/main/kotlin/.../handlers/monitorSimulation/MonitorSimulationHandlerImpl.kt` | Polling progress updates |
| `isma-server/domain/src/main/kotlin/.../handlers/compileLisma/CompileLismaHandlerImpl.kt` | LISMA → HSM compilation |
| `isma-server/domain/src/main/kotlin/.../handlers/validateLisma/ValidateLismaHandlerImpl.kt` | LISMA validation |
| `isma-server/domain/src/main/kotlin/.../DomainModule.kt` | DI configuration (handler interfaces → implementations) |
| `isma-server/domain/src/main/kotlin/.../handlers/runSimulation/ILismaTranslator.kt` | ILismaTranslator interface + TranslationException |
| `isma-server/infrastructure/src/main/kotlin/.../SimulationExecutorImpl.kt` | Simulation execution engine (virtual threads, result writing) |
| `isma-server/infrastructure/src/main/kotlin/.../LismaTranslatorImpl.kt` | LISMA → HSM translation with FDM support |
| `isma-server/infrastructure/src/main/kotlin/.../HighlightLismaHandlerImpl.kt` | ANTLR4-based syntax highlighting |
| `isma-server/infrastructure/src/main/kotlin/.../InfrastructureModule.kt` | DI configuration (stores, executors, translators) |
| `isma-server/grpc/build.gradle.kts` | Protobuf/gRPC code generation config |
| `isma-server/app/build.gradle.kts` | Application build config (main class, dependencies) |
