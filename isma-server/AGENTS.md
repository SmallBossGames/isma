# isma-server

gRPC-based server for ISMA with Netty transport layer.

## Modules

| Module | Description |
|--------|-------------|
| **app/** | Main application entry point. Starts gRPC server with Netty transport, uses domain sockets for IPC. Also includes Ktor HTTP server. |
| **domain/** | Domain layer - shared business logic with Koin DI. Contains handlers for simulation operations. |
| **grpc/** | gRPC code generation from protobuf. Proto files sourced from `../../protobuf-contracts` |
| **infrastructure/** | Infrastructure layer - implements domain interfaces. Uses ServiceLoader for integration methods and includes compilers (HSM, FDM, JVM). |

## Architecture

```
app/
 └── ApplicationKt (main class)
      ├── Starts Koin DI (domainModule, infrastructureModule, appModule)
      ├── Creates gRPC server on Unix domain socket
      └── Creates Ktor HTTP server (CIO engine)
       └── Imports: ru.nstu.isma.server.domain, ru.nstu.isma.server.grpc

grpc/
 └── Generates gRPC stubs from proto files
      └── Proto source: ../../protobuf-contracts/

domain/
 └── Handlers (Koin DI)
      ├── IRunSimulationHandler / RunSimulationHandlerImpl
      ├── IGetSimulationResultHandler / GetSimulationResultHandlerImpl
      ├── IMonitorSimulationHandler / MonitorSimulationHandlerImpl
      └── IListSimulationMethodsHandler / ListSimulationMethodsHandlerImpl

infrastructure/
 └── IntegrationMethodsStore (ServiceLoader for IIntegrationMethodFactory)
      └── Loads: Euler, Runge-Kutta 2, Runge-Kutta 3, Runge-Kutta 3-1, Runge-Kutta-Fehlberg, Runge-Kutta-Merson
```

## Handler Responsibilities

| Handler | Purpose |
|---------|---------|
| `IRunSimulationHandler` | Starts a simulation, returns running simulation result |
| `IGetSimulationResultHandler` | Retrieves completed simulation results |
| `IMonitorSimulationHandler` | Monitors simulation progress (progress %, step, time) |
| `IListSimulationMethodsHandler` | Lists available integration methods (Euler, RK2, RK3, RK31, RKF, RKM) |

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
| ktor-server-core, ktor-server-cio | Ktor HTTP server (for potential HTTP endpoints) |

## Proto Contracts

gRPC stubs are generated from `../../protobuf-contracts/`. Key messages in `run_simulation_request.proto`:

- `AccuracyConfig` (field 5) — enables adaptive step size control; presence of the message = enabled
- `StabilityConfig` (field 6) — enables stability control for RK methods; presence = enabled
- `EventDetectionConfig` (field 8) — enables event detection with gamma/low_border parameters

Proto changes require rebuilding `isma-server:grpc`.

## Build

```bash
./gradlew :isma-server:app:build
```
