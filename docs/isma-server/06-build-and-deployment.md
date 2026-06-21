# Build & Deployment

## Build Configuration

### Gradle Modules

```
:isma-server:grpc          — gRPC code generation (protobuf → Java)
:isma-server:domain        — Domain interfaces + handler implementations
:isma-server:infrastructure — Concrete store/executor implementations
:isma-server:app           — Application entry point + gRPC/HTTP services
```

### Build Scripts

#### `grpc/build.gradle.kts`

```kotlin
group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

plugins {
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.java.modules)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.protobuf.java.get().version}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.grpc.java.get().version}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins { id("grpc") {} }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("../../protobuf-contracts")   // Proto source location
        }
    }
}

dependencies {
    implementation(libs.grpc.netty.shaded)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)
}
```

**Code generation:** Protobuf files from `protobuf-contracts/v1/` are compiled into Java gRPC stubs. The generated classes include:
- Service interfaces (`SimulationServiceGrpc`, `LismaCompilerServiceGrpc`)
- Message classes (`RunSimulationRequest`, `CompileResponse`, etc.)
- Builder classes for all messages

**Important:** Proto changes require rebuilding this module. Generated code should NOT be committed (it's derived from `.proto` files).

#### `domain/build.gradle.kts`

```kotlin
group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-next-core"))
    implementation(libs.koin.core)
}
```

Domain depends only on external libraries and core project modules. No infrastructure or app dependencies.

#### `infrastructure/build.gradle.kts`

```kotlin
group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-server:domain"))
    implementation(project(":isma-jvm-lib:exchange-format"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))
    implementation(project(":isma-solver:lib-utils"))
    implementation(project(":isma-compiler:lisma-translator-hsm"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:hsm-fdm"))
    implementation(project(":isma-compiler:hsm-jvm"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))
    implementation(project(":isma-next-core"))
    implementation(libs.antlr4.runtime)
    implementation(libs.koin.core)
    implementation(libs.slf4j.api)
}
```

#### `app/build.gradle.kts`

```kotlin
group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    application
}

application {
    mainClass.set("ru.nstu.isma.server.app.ApplicationKt")
}

dependencies {
    implementation(project(":isma-server:domain"))
    implementation(project(":isma-server:grpc"))
    implementation(project(":isma-server:infrastructure"))
    implementation(project(":isma-solver:lib-meta"))

    // gRPC + Netty
    implementation(libs.grpc.netty)
    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact { classifier = "linux-x86_64" }
    }
    implementation(libs.netty.codec)
    implementation(libs.netty.handler)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)
    implementation(libs.grpc.services)

    // DI & Logging
    implementation(libs.kotlin.reflect)
    implementation(libs.koin.core)
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    // HTTP (Ktor)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.content.negotiation)
}
```

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
| `isma-solver:api` | `IIntegrationMethodFactory`, `DaeSystemStepSolver`, `IntgResultPoint` | domain, infrastructure |
| `isma-solver:core` | `DefaultDaeSystemStepSolver` | infrastructure |
| `isma-solver:lib-meta` | SPI interface `IIntegrationMethodFactory` | app (ServiceLoader) |
| `isma-compiler:hsm-core` | `HSM`, `IsmaErrorList`, `IsmaSyntaxError` | domain, infrastructure |
| `isma-compiler:hsm-fdm` | `FDMConverter` | infrastructure |
| `isma-compiler:hsm-jvm` | `EquationIndexProvider` | infrastructure |
| `isma-compiler:hsm-jvm-calcmodel` | Calculation model utilities | infrastructure |
| `isma-compiler:lisma-translator-hsm` | `LismaTranslator`, `InputTranslator` | infrastructure |
| `isma-next-core` | `HsmCompiler`, `HybridSystemSimulator`, `SimulationInitials` | domain, infrastructure |
| `isma-jvm-lib:exchange-format` | `writeAll()` (binary file writer) | infrastructure |

### Third-Party Dependencies

| Library | Purpose |
|---------|---------|
| Netty (with Epoll) | gRPC server transport + Unix domain sockets |
| gRPC (Netty + Protobuf) | RPC framework |
| Koin | Lightweight DI framework |
| Ktor (CIO) | Embedded HTTP server |
| ANTLR4 | LISMA lexer for syntax highlighting |
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
        K3["LismaTranslator()"]
        K4["HsmCompiler()"]
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

---

## Build Commands

```bash
# Build entire isma-server module (all 4 submodules)
./gradlew :isma-server:build

# Build just the application (includes all dependencies)
./gradlew :isma-server:app:build

# Generate gRPC stubs only (after proto changes)
./gradlew :isma-server:grpc:generateProto

# Build with tests
./gradlew :isma-server:test

# Build distribution bundle (UI + server together)
./.ci-cd/build-bundle.sh
```

### Common Issues

| Problem | Solution |
|---------|----------|
| Proto files not found | Run `./gradlew :isma-server:grpc:generateProto` after pulling proto changes |
| Missing Epoll native library | Server requires Linux x86_64; Epoll classifier `linux-x86_64` must be available |
| Socket file already exists | Server auto-deletes stale sockets at startup; manually `rm /tmp/isma-*.sock` if needed |
| ServiceLoader finds no integration methods | Ensure `isma-solver:lib:euler`, `lib:rk2`, etc. are in the classpath and have `META-INF/services/ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory` entries |

---

## Deployment

### Process Launch

The server is launched by the ISMA UI process (`SimulationServerManager`) as a separate JVM. The UI:

1. Determines the server launch script (from `ISMA_SERVER_SCRIPT` env var or `isma.server.script` system property)
2. Launches the server process
3. Reads `GRPC_SOCKET` and `HTTP_SOCKET` from stdout
4. Connects to the gRPC server via the Unix socket path
5. Connects to the HTTP server for file downloads

### Bundle Distribution

The `.ci-cd/build-bundle.sh` script creates a distribution bundle at `build/bundle/` containing:
- `isma-server` executable
- `isma-ui` executable
- `run-ui.sh` launcher script

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
