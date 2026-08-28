# Domain Layer

## Overview

The `domain/` module implements the business logic using a **handler-based architecture** with interface-implementation separation. Each use case is encapsulated in a handler pair: an interface (contract) and an implementation.

The domain layer is unusual in that it contains **both interfaces and their implementations**, while infrastructure implementations (stores, executors) live in the `infrastructure/` module. This creates a two-level dependency pattern: domain handlers depend on infrastructure stores and executors.

## Dependency Injection

All domain bindings are defined in `DomainModule.kt`. Each handler interface is registered as a singleton via `single<Interface> { Implementation(...) }`. Simulation handlers include `IRunSimulationHandler`, `IGetSimulationResultHandler`, `IMonitorSimulationHandler`, `IListSimulationMethodsHandler`, and `ICancelSimulationHandler`. LISMA compiler handlers include `ICompileLismaHandler`, `IValidateLismaHandler`, `IDeleteCompiledModelHandler`, and `IHighlightLismaHandler` (which is resolved from the infrastructure module). All handlers are registered as **singletons** (`single`) — they are stateless and delegate to injected dependencies.

---

## Handler Reference

### Simulation Handlers

#### IRunSimulationHandler / RunSimulationHandlerImpl

**Purpose:** Start a numerical simulation asynchronously.

**Dependencies:**
- `ICompiledModelStore` — retrieves the pre-compiled HSM model
- `ISimulationSessionStore` — creates a new simulation session
- `ISimulationExecutor` — submits the simulation for async execution

**Flow:**
1. Retrieve HSM model from store (throws `IllegalArgumentException` if not found)
2. Create a new `SimulationSession` with start/end times
3. Call `simulationExecutor.execute(sessionId, parameters, hsm)`
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

**Flow:**
1. Verify session exists (throws `IllegalArgumentException` if not)
2. Verify current status is `RUNNING` (throws `IllegalStateException` if not)
3. Call `sessionStore.updateStatus(id, CANCELLED)`

**Cancellation propagation:** The `SimulationExecutorImpl` checks for `CANCELLED` status on each simulation step and throws `InterruptedException` to halt execution.

---

### LISMA Compiler Handlers

#### ICompileLismaHandler / CompileLismaHandlerImpl

**Purpose:** Translate LISMA source code to a compiled HSM model and store it.

**Dependencies:**
- `ILismaTranslator` — performs source-to-HSM translation
- `ICompiledModelStore` — stores the compiled model

**Flow:** See `CompileLismaHandlerImpl.kt` for the full implementation. The handler calls `translator.translate(sourceCode)` and uses `fold()` to handle success and failure branches. On success, it stores the HSM model in `CompiledModelStore` and returns a `CompileLismaResult` with the model ID. On failure, it extracts errors from `TranslationException` or falls back to validation to get detailed error positions, maps `IsmaSyntaxError`/`IsmaSemanticError` to `CompilationError`, and returns a result with an empty `compiledModelId` and the error list. If the fallback validation also returns empty errors, a generic `CompilationError(-1, -1, "Unknown error")` is returned.

**Error recovery:** On translation failure, the handler falls back to validation to extract detailed error positions. If the fallback validation also returns empty errors, a generic `CompilationError(-1, -1, "Unknown error")` is returned via null-coalescing fallback.

---

#### IValidateLismaHandler / ValidateLismaHandlerImpl

**Purpose:** Validate LISMA source code and return all errors without compiling.

**Dependencies:**
- `ILismaTranslator` — performs validation

**Flow:**
1. Call `translator.validate(sourceCode)` → returns `IsmaErrorList`
2. Map each error:
   - `IsmaSyntaxError` → `CompilationError(row, col, msg)` with position info
   - `IsmaSemanticError` → `CompilationError(-1, -1, msg)` without position
3. Return `ValidateLismaResult(errors, warnings)`

**Note:** Warnings are always empty — the validator only produces errors.

---

#### IDeleteCompiledModelHandler / DeleteCompiledModelHandlerImpl

**Purpose:** Remove a compiled model from the store.

**Dependencies:**
- `ICompiledModelStore` — performs deletion

**Flow:** Direct pass-through — `compiledModelStore.delete(compiledModelId)`.

---

#### IHighlightLismaHandler / HighlightLismaHandlerImpl

**Purpose:** Lexical analysis for IDE syntax highlighting.

**Dependencies:** None (uses ANTLR4 runtime directly).

**Flow:**
1. Tokenize source with `LismaLexer`
2. Filter tokens to only: KEYWORD, COMMENT, NUMBER
3. Return `HighlightLismaResult(tokens)`

**Token categories returned:**

| ANTLR token type | SyntaxKind |
|-----------------|------------|
| CONST_KEYWORD, STATE_KEYWORD, FOR_KEYWORD, IF_KEYWORD, ELSE_KEYWORD, FROM_KEYWORD, MACRO_KEYWORD, SET_KEYWORD | `KEYWORD` |
| COMMENT, SL_COMMENT | `COMMENT` |
| FloatingPointLiteral, DecimalLiteral | `NUMBER` |

All other token types are silently filtered out. The `SyntaxKind` enum also includes `TEXT` (never produced by this handler — reserved for future use).

**Implementation file:** `infrastructure/src/main/kotlin/.../HighlightLismaHandlerImpl.kt` — uses `LismaLexer` from the LISMA compiler module.

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

See the respective source files for full definitions. The domain layer defines these data classes: `RunningSimulationResult` (holds `simulationId`), `SimulationProgress` (holds `startTime`, `endTime`, `currentTime`), `SimulationMethodItem` (holds `name` and `title`), `CompilationError` (holds `row`, `column`, `message`), `CompileLismaResult` (holds `compiledModelId`, `errors`, `warnings`), `ValidateLismaResult` (holds `errors`, `warnings`), `HighlightLismaResult` (holds `tokens`), and `SyntaxToken` (holds `start`, `length`, `kind`).

---

## Domain Interfaces (Contracts)

### ICompiledModelStore

See `ICompiledModelStore.kt` for the interface definition. It declares methods: `create(hsm: HSM): String` (stores model, returns UUID), `get(id: String): HSM?` (retrieves model), `delete(id: String): Boolean` (removes model), and `exists(id: String): Boolean` (checks presence). Manages in-memory storage of compiled HSM models keyed by UUID.

### ISimulationSessionStore

See `ISimulationSessionStore.kt` for the interface definition. It declares methods: `create(startTime, endTime)` (creates new session), `get(id)` (retrieves session), `getAll()` (returns all sessions), `update(id, session)` (replaces session), `updateStatus(id, status)` (updates status), `updateProgress(id, currentTime)` (updates progress), `completeSimulation(id, resultFilePath)` (marks completed), `failSimulation(id, error)` (marks failed), `delete(id)` (removes session), and `exists(id)` (checks presence). Manages simulation session state. All methods are thread-safe via `ConcurrentHashMap`.

### ISimulationExecutor

See `ISimulationExecutor.kt` for the interface definition. It declares a single method `execute(simulationId: Long, parameters: RunSimulationParameters, hsm: HSM)`. Executes a simulation asynchronously. Implementation runs on a separate thread from the `ExecutorService`.

### IIntegrationMethodsStore

See `IIntegrationMethodsStore.kt` for the interface definition. It declares methods: `getMethodNames()` (returns sorted list of method names) and `getMethod(name)` (returns the factory for a named method). Provides access to available numerical integration methods.

### ILismaTranslator

**File:** `domain/handlers/runSimulation/ILismaTranslator.kt`

See `ILismaTranslator.kt` for the interface and exception class definition. The interface declares `translate(sourceCode: String): Result<HSM>` and `validate(sourceCode: String): IsmaErrorList`. The `TranslationException` class holds an `IsmaErrorList` and extends `Exception`. Translates LISMA source code to HSM models. The `translate()` method returns `Result<HSM>` — `success` with the model or `failure` with a `TranslationException`.

### IHighlightLismaHandler

See `IHighlightLismaHandler.kt` for the interface definition. It declares `handle(sourceCode: String): HighlightLismaResult`. Lexical analysis contract for syntax highlighting.
