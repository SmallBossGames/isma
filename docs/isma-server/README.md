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

## Quick Start

### Building

```bash
./gradlew :isma-server:app:build
```

### Running (from source)

```bash
java -jar build/libs/isma-server-app-1.0.0-SNAPSHOT.jar \
    --socket-path /tmp/isma.sock \
    --http-socket-path /tmp/isma-http.sock
```

### Interacting

```bash
# List available simulation methods (gRPC)
grpcurl -plaintext \
    -grpc-header ":pseudo-protocol-version:1.0.0" \
    -d '{}' \
    -authority simulation \
    /tmp/isma.sock \
    ru.nstu.isma.contracts.simulation.SimulationService/ListSimulationMethods

# Compile LISMA source
grpcurl -plaintext \
    -d '{"lisma_source_code": "const t = 0..10;"}' \
    /tmp/isma.sock \
    ru.nstu.isma.contracts.simulation.LismaCompilerService/Compile

# Download simulation result (HTTP)
curl -o result.bin \
    --unix-socket /tmp/isma-http.sock \
    http://localhost/simulation/1/download
```

## Key Files

| Path | Purpose |
|------|---------|
| `app/src/main/kotlin/.../Application.kt` | Server entry point |
| `app/src/main/kotlin/.../grpc/SimulationServiceGrpcImpl.kt` | gRPC simulation service |
| `app/src/main/kotlin/.../grpc/LismaCompilerServiceGrpcImpl.kt` | gRPC compiler service |
| `app/src/main/kotlin/.../http/HttpRoutes.kt` | Ktor HTTP routes |
| `domain/.../handlers/runSimulation/RunSimulationHandlerImpl.kt` | Simulation start logic |
| `infrastructure/.../simulation/SimulationExecutorImpl.kt` | Simulation execution engine |
| `infrastructure/.../translation/LismaTranslatorImpl.kt` | LISMA → HSM translation |
| `domain/src/main/kotlin/.../DomainModule.kt` | DI configuration (handlers) |
| `infrastructure/.../InfrastructureModule.kt` | DI configuration (infrastructure) |
| `grpc/build.gradle.kts` | Protobuf code generation |
| `app/build.gradle.kts` | Application build configuration |
