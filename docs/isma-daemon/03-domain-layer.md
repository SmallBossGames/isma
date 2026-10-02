# Domain Layer

## Overview

The `domain/` module implements the business logic using a **handler-based architecture** with interface-implementation separation. Each use case is encapsulated in a handler pair: an interface (contract) and an implementation.

The domain layer is unusual in that it contains **both interfaces and their implementations**, while infrastructure implementations (stores, executors) live in the `infrastructure/` module. This creates a two-level dependency pattern: domain handlers depend on infrastructure stores and executors.

## Dependency Injection

All domain bindings are defined in `DomainModule.kt`. Each handler interface is registered as a singleton via `single<Interface> { Implementation(...) }`. Simulation handlers include `IRunSimulationHandler`, `IGetSimulationResultHandler`, `IMonitorSimulationHandler`, `IListSimulationMethodsHandler`, and `ICancelSimulationHandler`. LISMA compiler handlers include `ICompileLismaHandler`, `IValidateLismaHandler`, and `IDeleteCompiledModelHandler`. All handlers are registered as **singletons** (`single`) — they are stateless and delegate to injected dependencies.

---

## Handler Reference

### Simulation Handlers

#### IRunSimulationHandler / RunSimulationHandlerImpl

**Purpose:** Start a numerical simulation asynchronously.

**Dependencies:**
- `ICompiledModelStore` — retrieves the compiled model (the compiled `.class` bytes)
- `ISimulationSessionStore` — creates a new simulation session
- `ISimulationExecutor` — submits the simulation for async execution

**Flow:**
1. Retrieve `CompiledModel` from store (throws `IllegalArgumentException` if not found)
2. Create a new `SimulationSession` with start/end times
3. Call `simulationExecutor.execute(sessionId, parameters, model)`
4. Return `RunningSimulationResult(sessionId)`

**Parameters:** `RunSimulationParameters` data class contains: `startTime`, `endTime`, `initialStep`, `methodName`, `accuracy`, `isAccuracyInUse`, `isStabilityControlInUse`, `compiledModelId`, `eventDetectionGamma`, and `eventDetectionLowBorder`. See `RunSimulationParameters.kt` for the full data class definition.

---

#### IGetSimulationResultHandler / GetSimulationResultHandlerImpl

**Purpose:** Return an `InputStream` to the binary result file of a completed simulation.

**Dependencies:**
- `ISimulationSessionStore` — validates session state

**Flow:**
1. Get session by ID (throws `IllegalArgumentException` if not found)
2. Verify status is `COMPLETED` (throws `IllegalStateException` otherwise)
3. Verify `resultFilePath` is set (throws `IllegalStateException` if null)
4. Return `FileInputStream(resultFilePath)`

**Note:** The handler returns a raw file stream. The actual file download is served by the Ktor HTTP server via the `/simulation/{id}/download` route.

---

#### IMonitorSimulationHandler / MonitorSimulationHandlerImpl

**Purpose:** Poll simulation progress and deliver updates via callback.

**Dependencies:**
- `ISimulationSessionStore` — reads session state

**Flow:** See `MonitorSimulationHandlerImpl.kt` for the implementation. The handler runs an infinite polling loop that retrieves the current session from the store (throwing `IllegalStateException` if the session disappeared), calculates whether to report based on the time threshold or final status, calls the `onProgress` callback with a `SimulationProgress` object, and sleeps 100ms between polls. The loop breaks when the simulation reaches a terminal status.

**Threshold calculation:** `threshold = accuracy * (endTime - startTime)`. If accuracy is 0, only final status is reported.

**Input:** `accuracy` parameter controls reporting granularity — larger accuracy values mean fewer, more spaced-out reports.

---

#### IListSimulationMethodsHandler / ListSimulationMethodsHandlerImpl

**Purpose:** Return the list of available integration methods with human-readable titles.

**Dependencies:**
- `IIntegrationMethodsStore` — provides method names

**Flow:**
1. Get sorted method names from store
2. Map each name to a `SimulationMethodItem(name, formatTitle(name))`

**Title formatting:**

| name | title |
|------|-------|
| `euler` | `Euler` |
| `rk2` | `Runge-Kutta 2nd order` |
| `rk3` | `Runge-Kutta 3rd order` |
| `rk31` | `Runge-Kutta 3(1)` |
| `rkmerson` | `Merson's method` |
| `rkfehlberg` | `Runge-Kutta-Fehlberg` |
| other | Title-cased name |

---

#### ICancelSimulationHandler / CancelSimulationHandlerImpl

**Purpose:** Cancel a running simulation by setting its status to `CANCELLED`.

**Dependencies:**
- `ISimulationSessionStore` — updates session status
- `ISimulationProcessRegistry` — terminates the runner process

**Flow:**
1. Verify session exists (throws `IllegalArgumentException` if not)
2. Verify current status is `RUNNING` (throws `IllegalStateException` if not)
3. Call `sessionStore.updateStatus(id, CANCELLED)`
4. Call `processRegistry.kill(id)` to send SIGTERM to the runner process

**Cancellation propagation:** The daemon marks the session CANCELLED and sends SIGTERM to the `isma-solver:cli` runner process. The runner's shutdown hook stops its integration loop, and the process exits. The supervising `SimulationExecutorImpl` detects the exit and leaves the session CANCELLED (ignoring the exit code).

---

### LISMA Compiler Handlers

#### ICompileLismaHandler / CompileLismaHandlerImpl

**Purpose:** Compile LISMA source code to a compiled `.class` file (via the compiler CLI) and store it.

**Dependencies:**
- `ILismaCompiler` — compiles the LISMA source to a `.class` file via the standalone CLI
- `ICompiledModelStore` — stores the compiled model

**Flow:** See `CompileLismaHandlerImpl.kt` for the full implementation. The handler calls `compiler.compile(sourceCode)`, which launches the standalone LISMA compiler CLI as a separate process (LISMA source on stdin, compiled `.class` on stdout). On success it stores a `CompiledModel(artifact)` in `CompiledModelStore` and returns a `CompileLismaResult` with the model ID. On failure the CLI emits one error line per diagnostic (`<row>:<col>:<message>`), which the handler parses into `CompilationError`s and returns with an empty `compiledModelId`.

**Error mapping:** Each CLI error line is parsed into a `CompilationError(row, column, message)`; rows/columns are `-1` when the CLI does not report a position.

---

#### IValidateLismaHandler / ValidateLismaHandlerImpl

**Purpose:** Validate LISMA source code and return all errors without compiling.

**Dependencies:**
- `ILismaCompiler` — validates the LISMA source via the standalone CLI

**Flow:**
1. Call `compiler.validate(sourceCode)` → returns `List<CompilationError>`
2. Return `ValidateLismaResult(errors, warnings)`

**Note:** Warnings are always empty — the validator only produces errors.

---

#### IDeleteCompiledModelHandler / DeleteCompiledModelHandlerImpl

**Purpose:** Remove a compiled model from the store.

**Dependencies:**
- `ICompiledModelStore` — performs deletion

**Flow:** Direct pass-through — `compiledModelStore.delete(compiledModelId)`.

---

## Domain Entities

### SimulationSession

See `SimulationSession.kt` for the full data class definition. It contains: `simulationId` (auto-incremented `Long`), `startTime`, `endTime`, `currentTime`, `status` (enum), `resultFilePath` (nullable `String`), and `error` (nullable `String`).

**Lifecycle:**
1. **Created** with `RUNNING` status, `startTime`, `endTime`
2. **Progressed** — `currentTime` is updated on each simulation step
3. **Terminal states:**
   - `COMPLETED` — `resultFilePath` is set
   - `FAILED` — `error` message is set
   - `CANCELLED` — explicitly cancelled
   - `RUNNING` — still in progress

### SimulationStatus

See `SimulationStatus.kt` for the enum definition. It has four values: `RUNNING` (simulation is executing), `COMPLETED` (finished successfully), `FAILED` (execution failed with error), and `CANCELLED` (explicitly cancelled by client).

### Supporting Data Classes

See the respective source files for full definitions. The domain layer defines these data classes: `RunningSimulationResult` (holds `simulationId`), `SimulationProgress` (holds `startTime`, `endTime`, `currentTime`), `SimulationMethodItem` (holds `name` and `title`), `CompilationError` (holds `row`, `column`, `message`), `CompileLismaResult` (holds `compiledModelId`, `errors`, `warnings`), and `ValidateLismaResult` (holds `errors`, `warnings`).

---

## Domain Interfaces (Contracts)
### ICompiledModelStore

See `ICompiledModelStore.kt` for the interface definition. It declares methods: `create(model: CompiledModel): String` (stores model, returns UUID), `get(id: String): CompiledModel?` (retrieves model), `delete(id: String): Boolean` (removes model), and `exists(id: String): Boolean` (checks presence). Manages in-memory storage of compiled models keyed by UUID.

**CompiledModel:** A data class holding the compiled-model **artifact** (`artifact: ByteArray`) — the raw `.class` file of the analyzed hybrid-system class, produced by the standalone LISMA compiler CLI. The `.class` inlines the translated HSM as a serialized blob (`__ISMA_HSM__` field, exposed via `ismaHsmBytes()`). The daemon stores it opaquely; the `isma-solver:cli` runner loads the class in a fresh class loader, extracts and deserializes the HSM blob, and runs the simulation.

### ISimulationSessionStore

See `ISimulationSessionStore.kt` for the interface definition. It declares methods: `create(startTime, endTime)` (creates new session), `get(id)` (retrieves session), `getAll()` (returns all sessions), `update(id, session)` (replaces session), `updateStatus(id, status)` (updates status), `updateProgress(id, currentTime)` (updates progress), `completeSimulation(id, resultFilePath)` (marks completed), `failSimulation(id, error)` (marks failed), `delete(id)` (removes session), and `exists(id)` (checks presence). Manages simulation session state. All methods are thread-safe via `ConcurrentHashMap`.
### ISimulationExecutor

See `ISimulationExecutor.kt` for the interface definition. It declares a single method `execute(simulationId: Long, parameters: RunSimulationParameters, model: CompiledModel)`. Executes a simulation asynchronously. Implementation runs on a separate thread from the `ExecutorService`.

### IIntegrationMethodsStore

See `IIntegrationMethodsStore.kt` for the interface definition. It declares methods: `getMethodNames()` (returns sorted list of method names) and `getMethod(name)` (returns the factory for a named method). Provides access to available numerical integration methods.

### ILismaCompiler

**File:** `domain/compiler/ILismaCompiler.kt`

```kotlin
interface ILismaCompiler {
    fun compile(sourceCode: String): CompileOutcome
    fun validate(sourceCode: String): List<CompilationError>
}
```

**Purpose:** Compiles LISMA source code to a compiled `.class` file and validates LISMA source. Implementations run the standalone LISMA compiler CLI as a separate process (LISMA source on stdin; `.class` bytes or error lines on stdout). `CompileOutcome` is a sealed result: `Success(artifact: ByteArray)` or `Failure(errors: List<CompilationError>)`.

**Implementation:** `CliLismaCompiler` (infrastructure) — see [04-infrastructure-layer.md](04-infrastructure-layer.md).

### ISimulationProcessRegistry

**File:** `domain/simulation/ISimulationProcessRegistry.kt`

```kotlin
interface ISimulationProcessRegistry {
    fun register(simulationId: Long, process: Process)
    fun unregister(simulationId: Long)
    fun kill(simulationId: Long)
}
```

**Purpose:** Tracks the `isma-solver:cli` runner process launched for each running simulation so that `CancelSimulation` can terminate it. `kill` sends SIGTERM to the registered process.

