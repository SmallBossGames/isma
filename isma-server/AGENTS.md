# isma-server

gRPC-based server for ISMA with Netty transport layer.

## Modules

| Module | Description |
|--------|-------------|
| **app/** | Main application entry point. Starts gRPC server with Netty transport, uses domain sockets for IPC |
| **domain/** | Domain layer - shared business logic with Koin DI |
| **grpc/** | gRPC code generation from protobuf. Proto files sourced from `../../protobuf-contracts` |

## Architecture

```
app/
 └── ApplicationKt (main class)
      ├── Uses gRPC server with Netty transport
      └── Imports: ru.nstu.isma.server.domain, ru.nstu.isma.server.grpc

grpc/
 └── Generates gRPC stubs from proto files
      └── Proto source: ../../protobuf-contracts/

domain/
 └── Shared domain logic (Koin DI)
```

## Key Dependencies

| Dependency | Purpose |
|------------|---------|
| grpc-netty | Netty-based gRPC server transport |
| netty-transport + netty-transport-classes-epoll | Epoll event loop for Linux domain sockets |
| grpc-stub, grpc-protobuf, grpc-java | gRPC API and generated code |
| protobuf-java | Protocol buffers |
| kotlin-reflect, koin-core | DI and reflection |

## Build

```bash
./gradlew :isma-server:app:build
```
