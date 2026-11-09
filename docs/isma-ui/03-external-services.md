# External Services

## Purpose

The `external-services` module handles all communication with the standalone CLI tools. It launches the LISMA compiler CLI and the simulation runner CLI as separate processes, owns the CLI protocols (stdin/stdout/stderr plumbing, exit codes, the binary result stream), and exposes a facade that maps CLI outcomes to high-level DTOs. It also contains the LSP client that drives the standalone `isma-lsp` language server process for syntax highlighting (see [../isma-lsp/](../isma-lsp/README.md)).

## Structure

The module source lives in `external-services/src/main/kotlin/ru/isma/next/external/` and contains: `BinaryEquationIndexProvider.kt`, `BinaryFilePointProvider.kt`, and `SimulationServerFacade.kt`. The `cli/` subdirectory contains the CLI process layer: `CliProcess.kt`, `CliLauncher.kt`, `CliToolLaunchers.kt`, `CliToolException.kt`, `CliWorkDir.kt`, `LismaCompilerCli.kt`, `SimulationRunnerCli.kt`, and `ActiveSimulation.kt`. The `dtos/` subdirectory contains `ExternalDtoTypes.kt` and `RunSimulationParams.kt`. The `lsp/` subdirectory contains the LSP client stack: `LspTransport.kt`, `ProcessLspTransport.kt`, `LspProcessManager.kt`, and `LspClient.kt`. Module group: `ru.isma.next.ui`, version: `1.0.0-SNAPSHOT`.

## Module Configuration

**File:** `external-services/build.gradle.kts`

The module applies the Kotlin JVM plugin and Java modules plugin. Dependencies include project references to `:isma-ui:domain` and `:isma-jvm-lib:exchange-format`. External dependencies cover kotlinx-coroutines-core, kotlinx-serialization-json (LSP JSON), and slf4j-api. See `external-services/build.gradle.kts` for the full configuration.

## CLI Process Layer

### CliProcess

**File:** `cli/CliProcess.kt`

Wraps a launched `java.lang.Process`: exposes `stdin`/`stdout` streams, `waitExitCode()`, and the accumulated `stderr()` (drained on a background thread so the pipe cannot deadlock). `terminate()` sends SIGTERM on Unix (so the tool can run its shutdown hooks) and falls back to a hard kill if the process does not exit within 2 seconds; on Windows it hard-kills directly.

### CliLauncher

**File:** `cli/CliLauncher.kt`

Resolves the path of one configured CLI tool and launches it. Path resolution order: environment variable, then the system property; `IllegalStateException` when the tool is not configured.

### CliToolLaunchers

**File:** `cli/CliToolLaunchers.kt`

Holds the two configured launchers:

| Tool | Env var | System property |
| --- | --- | --- |
| `lisma-compiler-cli` | `ISMA_COMPILER_CLI` | `isma.compiler.cli` |
| `simulation-runner-cli` | `ISMA_RUNNER_CLI` | `isma.runner.cli` |

### CliWorkDir

**File:** `cli/CliWorkDir.kt`

The shared work directory for CLI tool artifacts — compiled `.class` models and simulation result files — in the system temp directory (`{java.io.tmpdir}/isma-ui/`).

### CliToolException

**File:** `cli/CliToolException.kt`

An `IllegalStateException` raised when a CLI tool fails outside its documented contract (unexpected exit code, launch failure). Carries the tool's stderr for diagnostics.

## Compiler CLI

### LismaCompilerCli

**File:** `cli/LismaCompilerCli.kt`

Adapter for the standalone LISMA compiler CLI. Protocol: LISMA source on stdin. Compile mode — exit 0 puts the compiled `.class` bytes on stdout, exit 1 puts `row:col:message` error lines on stdout. Validate mode (`--validate`) — exit 0 is clean, exit 1 puts error lines on stdout. Any other exit code is a tool failure (`CliToolException`).

| Method | Description |
| --- | --- |
| `compile(source): CompilationOutcome` | Compiles source; `Success(compiledModel)` or `Failed(errors)` |
| `validate(source): List<CompilationErrorDto>` | Validates without producing a model |

`parseErrorLine` parses a `<row>:<col>:<message>` diagnostic line; row/col of -1 mean the error has no source position.

## Runner CLI

### SimulationRunnerCli

**File:** `cli/SimulationRunnerCli.kt`

Adapter for the standalone simulation runner CLI. Protocol: `--model <class> --start --end --step --method` plus optional accuracy (`--accuracy <v> --accuracy-enabled`), stability (`--stability-enabled`), and event-detection (`--gamma <v> --low-border <v>`) flags. The runner streams the result to stdout in the ISMA exchange binary format, one row per integration step; exit 0 is success, exit 1 is failure (`simulation-runner: <message>` on stderr). `--list-methods` prints the bundled integration method names, one per line.

| Method | Description |
| --- | --- |
| `start(params): ActiveSimulation` | Launches a run, returns a handle to the process |
| `getMethods(): List<String>` | Lists bundled integration methods |

### ActiveSimulation

**File:** `cli/ActiveSimulation.kt`

A running simulation runner process. Reads the runner's stdout on a virtual thread, tees the byte stream into a result file (`{workdir}/isma-simulation-<uuid>.bin`), and parses the binary format to expose the TIME column of each streamed row for progress tracking. Whole rows are read in bulk and the file sink is buffered (`BufferedOutputStream`) so the pipe stays drained and the runner is never back-pressured. The TIME values are buffered in a plain `LinkedBlockingQueue` (no coroutine channel) and drained with a blocking `takeTime()` — stream completion is signalled by a sentinel, never by an exception.

| Member | Description |
| --- | --- |
| `takeTime(): Double?` | Blocks until the next TIME value of an integration step is available; returns `null` when the stream has ended (run finished or cancelled) |
| `getResult(): CachedSimulationResult` | Waits for exit; returns the result file + column names; throws `CliToolException` on non-zero exit |
| `cancel()` | Graceful termination of the runner (SIGTERM → hard kill) |

**Binary format:** a metadata header (uint16 BE column count, then per-column uint16 BE name length + UTF-8 name), followed by one row per time step — each row a sequence of big-endian doubles, the first column being TIME.

## Facade

### SimulationServerFacade

**File:** `SimulationServerFacade.kt`

High-level facade that drives the two CLI tools directly. Model IDs are local paths of compiled `.class` files (written to the work dir); simulation IDs are local counters.

**Methods:**

| Method | Description |
| --- | --- |
| `compileModel(String)` | Compiles LISMA source; on success writes the `.class` to the work dir and returns `CompileResult` with the file path as `modelId`; on failure returns the diagnostics |
| `validateModel(String)` | Validates LISMA source, returns `ValidationResult` |
| `runSimulation(RunSimulationParams)` | Launches the runner, returns a local `simulationId` |
| `monitorSimulation(Long, (SimulationProgress) -> Unit)` | Blocking: pulls `takeTime()` in a loop on the caller's thread and invokes the callback with coalesced updates — at most one per 100 ms or 0.005 of the time span (the runner emits one row per integration step); the final time is always reported. Returns when the stream ends |
| `downloadResultToCache(Long)` | Blocking: waits for the run to finish, returns the result file + column names as `CachedSimulationResult` |
| `cancelSimulation(Long)` | Cancels the running simulation (SIGTERM → hard kill) |
| `getSimulationMethods()` | Lists available integration methods via the runner's `--list-methods` |
| `shutdown()` | Terminates running simulations and deletes compiled model files |

**DTOs defined in `dtos/ExternalDtoTypes.kt`:**

`CachedSimulationResult` (File + List<String> columnNames), `CompileResult` (String modelId + List<CompilationErrorDto> errors + List<String> warnings), `ValidationResult` (List<CompilationErrorDto> errors + List<String> warnings), `CompilationErrorDto` (Int row + Int column + String message), and the `CompilationOutcome` sealed class (`Success(ByteArray)` / `Failed(List<CompilationErrorDto>)`). See `ExternalDtoTypes.kt` for the full definitions.

**DTOs in `dtos/RunSimulationParams.kt`:** `RunSimulationParams` contains: `startTime`, `endTime`, `initialStep` (all Double), `methodName` (String), `accuracy` (Double), `isAccuracyInUse` (Boolean), `isStabilityControlInUse` (Boolean), `compiledModelId` (String), and optional `eventDetectionGamma` and `eventDetectionLowBorder` (both Double?, null when event detection is disabled). See `RunSimulationParams.kt` for the full definition.

## LSP Client

The LSP client stack in `lsp/` drives the standalone `isma-lsp` language server (see [../isma-lsp/](../isma-lsp/README.md)) for syntax highlighting.

### LspTransport

**File:** `lsp/LspTransport.kt`

Byte-level transport interface: `send(json: String)`, `readNext(): String?` (blocking; null on end of stream), and `close()`. Implementations deliver complete LSP messages without `Content-Length` framing.

### ProcessLspTransport

**File:** `lsp/ProcessLspTransport.kt`

`LspTransport` backed by a child process speaking LSP over stdio. stdout carries the protocol (`Content-Length: N\r\n\r\n` + N UTF-8 bytes); stderr is drained to the logger so it cannot corrupt the protocol stream.

### LspProcessManager

**File:** `lsp/LspProcessManager.kt`

Manages the `isma-lsp` child process. The script path is resolved from environment variable `ISMA_LSP_SCRIPT` or system property `isma.lsp.script`. `start()` is idempotent — repeated calls return the same transport; `stop()` closes the transport and kills the process.

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

The simulation run follows this sequence, all on a per-simulation virtual thread: `SimulationTaskService` calls `Facade.compileModel(source)`, which launches the compiler CLI (source on stdin) and writes the compiled `.class` to the work dir on success. Next, `runSimulation(params)` launches the runner CLI with the model path and parameters and returns a local simulation id. Then `monitorSimulation(id, onProgress)` blocks, pulling the runner's TIME column and invoking `onProgress` with coalesced updates until the stream ends. Finally, `downloadResultToCache(id)` waits for the runner to exit and returns the accumulated result file with its column names. Cancellation terminates the runner process (SIGTERM → hard kill), which ends the monitor loop.

## Error Handling

| Scenario | Behavior |
| --- | --- |
| CLI tool not configured | `IllegalStateException` from `CliLauncher.resolvePath()` |
| Compiler exit 1 | Diagnostics parsed from stdout (`row:col:message` lines), returned as `CompileResult`/`ValidationResult` errors |
| Compiler/runner unexpected exit code | `CliToolException` with the tool's stderr |
| Runner exit 1 after a run | `CliToolException` from `downloadResultToCache`; the partial result file is deleted |
| Unknown simulation id | `IllegalStateException` from the facade |
