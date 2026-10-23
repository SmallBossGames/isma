# External Services

## Purpose

The `external-services` module handles all communication with the ISMA server process. It manages the server's lifecycle (start/stop as a child JVM process), provides gRPC clients over Unix Domain Sockets (Netty Epoll), an HTTP client over Unix sockets (Ktor CIO) for result file downloads, a facade that orchestrates these clients into high-level operations, and an LSP client that drives the standalone `isma-lsp` language server process for syntax highlighting (see [../isma-lsp/](../isma-lsp/README.md)).

## Structure

The module source lives in `external-services/src/main/kotlin/ru/isma/next/external/` and contains: `BinaryEquationIndexProvider.kt`, `BinaryFilePointProvider.kt`, `CompilationClient.kt`, `DownloadClient.kt`, `GrpcLismaCompilerClient.kt`, `GrpcSimulationClient.kt`, `HttpSimulationClient.kt`, `SimulationServerFacade.kt`, and `SimulationServerManager.kt`. The `dtos/` subdirectory contains `ExternalDtoTypes.kt` and `RunSimulationParams.kt`. The `lsp/` subdirectory contains the LSP client stack: `LspTransport.kt`, `ProcessLspTransport.kt`, `LspProcessManager.kt`, and `LspClient.kt`. Module group: `ru.isma.next.ui`, version: `1.0.0-SNAPSHOT`.

## Module Configuration

**File:** `external-services/build.gradle.kts`

The module applies the Kotlin JVM plugin and Java modules plugin. Dependencies include project references to `:isma-ui:grpc`, `:isma-ui:domain`, and `:isma-jvm-lib:exchange-format`. External dependencies cover gRPC-Netty, gRPC-protobuf, gRPC-stub, protobuf-java, kotlinx-coroutines-core, kotlinx-serialization-json, slf4j-api, ktor-client-core, ktor-client-cio, kotlinx-io-core, and Netty transport (with Linux x86_64 epoll classifier). See `external-services/build.gradle.kts` for the full configuration.

## Server Lifecycle

### SimulationServerManager

**File:** `SimulationServerManager.kt`

Manages the isma-server child process. The script path is resolved from environment variable `ISMA_SERVER_SCRIPT` or system property `isma.server.script`. The class contains a nested `SocketPaths` data class with `grpc` and `http` string properties, and exposes `start(): SocketPaths` and `stop()` methods.

**Startup protocol:**

1. `ProcessBuilder(scriptPath).redirectErrorStream(true).start()` launches the server
2. Reads stdout line-by-line, skipping `WARNING:`, `SLF4J:`, blank, and log-prefixed lines
3. Expects the first non-skipped line to be `Starting gRPC server on Unix socket: <path>`
4. Extracts gRPC socket path and HTTP socket path (line containing `HTTP_SOCKET=<path>`)
5. Returns `SocketPaths(grpc, http)`

The `start()` method checks if already running, creates a `ProcessBuilder`, reads the first 4 non-skipped lines from stdout, extracts the gRPC socket from the first line and the HTTP socket from the `HTTP_SOCKET=` line (throwing `IllegalStateException` if not found), stores the paths, sets `running = true`, and registers a shutdown hook. See `SimulationServerManager.kt` for the full implementation. A shutdown hook registers `stop()` to ensure the server process is destroyed on JVM exit.

### SimulationServerFacade

**File:** `SimulationServerFacade.kt`

The facade orchestrates the three client connections. It's initialized lazily via `warmup()` called from `IsmaApplication.init`. The class takes a `SimulationServerManager` in its constructor and holds late-initialized references to `grpcClient` (GrpcSimulationClient), `httpClient` (HttpSimulationClient), and `compilerClient` (GrpcLismaCompilerClient). Exposes `warmup()` and `shutdown()` methods. See `SimulationServerFacade.kt` for the full implementation.

**Methods:**

| Method | Client | Description |
| --- | --- | --- |
| `warmup()` | — | Starts server, creates all 3 clients |
| `compileModel(String)` | `compilerClient` | Compiles LISMA source, returns `CompileResult` |
| `validateModel(String)` | `compilerClient` | Validates LISMA source, returns `ValidationResult` |
| `deleteCompiledModel(String)` | `compilerClient` | Removes compiled model from server cache |
| `runSimulation(RunSimulationParams)` | `grpcClient` | Starts simulation, returns `simulationId` |
| `monitorSimulation(Long, Double)` | `grpcClient` | Returns `Flow<SimulationProgress>` |
| `downloadResultToCache(Long)` | `grpcClient` + `httpClient` | Gets download URL, downloads to temp file |
| `cancelSimulation(Long)` | `grpcClient` | Cancels running simulation |
| `getSimulationMethods()` | `grpcClient` | Lists available integration methods |
| `shutdown()` | All | Shuts down clients and server process |

**DTOs defined in `dtos/ExternalDtoTypes.kt`:**

`CachedSimulationResult` (File + List<String> columnNames), `CompileResult` (String modelId + List<CompilationErrorDto> errors + List<String> warnings), `ValidationResult` (List<CompilationErrorDto> errors + List<String> warnings), and `CompilationErrorDto` (Int row + Int column + String message). See `ExternalDtoTypes.kt` for the full definitions.

**DTOs relocated from facade:** `RunSimulationParams.kt` is now in `dtos/` subdirectory (was previously at the root of `external/`).

**New extracted clients:**
- `CompilationClient.kt` — encapsulates compile, validate, and delete-compiled-model operations (previously inline in `SimulationServerFacade`)
- `DownloadClient.kt` — encapsulates result download logic (previously inline in `SimulationServerFacade`)

## gRPC Clients

### GrpcSimulationClient

**File:** `GrpcSimulationClient.kt`

Netty gRPC client using Linux Epoll for Unix Domain Socket transport. See `GrpcSimulationClient.kt` for the full implementation.

### RunSimulationParams

**File:** `dtos/RunSimulationParams.kt`

DTO passed to `serverFacade.runSimulation()` containing: `startTime`, `endTime`, `initialStep` (all Double), `methodName` (String), `accuracy` (Double), `isAccuracyInUse` (Boolean), `isStabilityControlInUse` (Boolean), `compiledModelId` (String), and optional `eventDetectionGamma` and `eventDetectionLowBorder` (both Double?, null when event detection is disabled). See `RunSimulationParams.kt` for the full definition.

### GrpcSimulationClient

**File:** `GrpcSimulationClient.kt`

A class that takes a `socketPath` string in its constructor. Creates a `MultiThreadIoEventLoopGroup` with `EpollIoHandler.newFactory()`, builds a `ManagedChannel` via `NettyChannelBuilder` targeting a `DomainSocketAddress` with `EpollDomainSocketChannel`, plaintext negotiation, and a 1-year keepalive interval (effectively disabled since the channel lives for the application lifetime). Exposes a `blockingStub` via `SimulationServiceGrpc.newBlockingStub(channel)`. The `shutdown()` method shuts down the channel and event loop group. See `GrpcSimulationClient.kt` for the full implementation.

### GrpcLismaCompilerClient

**File:** `GrpcLismaCompilerClient.kt`

Same Epoll/Unix socket setup, communicates with `LismaCompilerServiceGrpc` for compile, validate, and delete operations.

## HTTP Client

### HttpSimulationClient

**File:** `HttpSimulationClient.kt`

Ktor CIO HTTP client for downloading simulation result files over Unix Domain Sockets. Used exclusively by `downloadResultToCache()` — the gRPC client returns a download URL, and the HTTP client fetches the binary file.

## LSP Client

The LSP client stack in `lsp/` drives the standalone `isma-lsp` language server (see [../isma-lsp/](../isma-lsp/README.md)) for syntax highlighting. It replaces the previous gRPC `Highlight` RPC.

### LspTransport

**File:** `lsp/LspTransport.kt`

Byte-level transport interface: `send(json: String)`, `readNext(): String?` (blocking; null on end of stream), and `close()`. Implementations deliver complete LSP messages without `Content-Length` framing.

### ProcessLspTransport

**File:** `lsp/ProcessLspTransport.kt`

`LspTransport` backed by a child process speaking LSP over stdio. stdout carries the protocol (`Content-Length: N\r\n\r\n` + N UTF-8 bytes); stderr is drained to the logger so it cannot corrupt the protocol stream.

### LspProcessManager

**File:** `lsp/LspProcessManager.kt`

Manages the `isma-lsp` child process, mirroring `SimulationServerManager` for the gRPC server. The script path is resolved from environment variable `ISMA_LSP_SCRIPT` or system property `isma.lsp.script`. `start()` is idempotent — repeated calls return the same transport; `stop()` closes the transport and kills the process.

### LspClient

**File:** `lsp/LspClient.kt`

Minimal LSP client. Takes an `LspTransport` in its constructor (the app wires `LspClient(LspProcessManager().start())`). A daemon reader thread dispatches responses to `CompletableDeferred` instances keyed by request id. The `initialize`/`initialized` handshake is performed lazily on the first request.

| Member | Description |
| --- | --- |
| `suspend semanticTokens(documentId: String, source: String): List<Token>` | Opens the document on first call (`didOpen`), updates on subsequent calls (`didChange` with incrementing version), then requests `semanticTokens/full` and decodes the relative-format data |
| `closeDocument(documentId: String)` | Sends `textDocument/didClose` (idempotent) |
| `shutdown()` | Blocking best-effort: `shutdown` request, `exit` notification, transport close. Never throws |

`Token` is a data class `(line: Int, startChar: Int, length: Int, type: String)` with line/column offsets. Document URIs are `file:///isma/<documentId>.lisma`. Requests time out after 10 seconds. The token legend (`keyword`, `comment`, `number`) is captured from the `initialize` response.

## Data Providers

### BinaryFilePointProvider

**File:** `BinaryFilePointProvider.kt`

Implements `SimulationResultReader`. Takes a `File` and `List<String>` columnNames in its constructor. Reads binary simulation results using `ru.isma.next.exchange.format.readAllPointsSequence()` and streams as `Flow<SimulationPoint>`. Provides a companion object method `readMetadata(file: File): SimulationMetadata`. See `BinaryFilePointProvider.kt` for the full implementation.

### BinaryEquationIndexProvider

**File:** `BinaryEquationIndexProvider.kt`

Implements `IEquationIndexProvider` by parsing column name prefixes:
- `DE_` — differential equation variables
- `AE_` — algebraic equation variables
- `f` — forcing functions

Derives equation counts and codes from the column metadata returned with the simulation result.

## Communication Flow: Simulation Run

The simulation run follows this sequence: `SimulationService` calls `Facade.compileModel(source)`, which sends a `CompileRequest` via `GrpcSimulationClient` to the server. The server returns a `CompileResponse`, which the client converts to `CompileResult`. Next, `runSimulation(params)` sends a `RunSimulationRequest` and returns a `simulationId`. Then `monitorSimulation(id)` initiates a server stream of `SimulationProgress` values, which the client emits as a `Flow<SimulationProgress>`. Finally, `downloadResultToCache(id)` calls `getSimulationResult()` to get a download URL, then the `HttpSimulationClient` fetches the binary file via HTTP GET. The facade returns a `CachedSimulationResult` to the UI.

## Error Handling

| Scenario | Behavior |
| --- | --- |
| Server script not found | `IllegalStateException` from `resolveServerScriptPath()` |
| Server produces no output | `IllegalStateException` — "isma-server started but produced no output" |
| HTTP socket not found in output | `IllegalStateException` — "HTTP socket not found in server output" |
| Empty download URL | `IllegalStateException` — "Download URL is empty" |
| gRPC failure | Propagated as gRPC `StatusException` from the blocking stub |
| Missing Netty Epoll native library | `UnsatisfiedLinkError` at channel creation time (Linux-only dependency) |
