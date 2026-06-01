# Domain Layer

## Overview

The `domain/` module implements the business logic using a **handler-based architecture** with interface-implementation separation. Each use case is encapsulated in a handler pair: an interface (contract) and an implementation.

The domain layer is unusual in that it contains **both interfaces and their implementations**, while infrastructure implementations (stores, executors) live in the `infrastructure/` module. This creates a two-level dependency pattern:

```
domain handlers ──depends on──► infrastructure stores/executors
```

## Dependency Injection

All domain bindings are defined in `DomainModule.kt`:

```kotlin
val domainModule = module {
    // Simulation handlers
    single<IRunSimulationHandler> { RunSimulationHandlerImpl(get(), get(), get()) }
    single<IGetSimulationResultHandler> { GetSimulationResultHandlerImpl(get()) }
    single<IMonitorSimulationHandler> { MonitorSimulationHandlerImpl(get()) }
    single<IListSimulationMethodsHandler> { ListSimulationMethodsHandlerImpl(get()) }
    single<ICancelSimulationHandler> { CancelSimulationHandlerImpl(get()) }

    // LISMA compiler handlers
    single<ICompileLismaHandler> { CompileLismaHandlerImpl(get(), get()) }
    single<IValidateLismaHandler> { ValidateLismaHandlerImpl(get()) }
    single<IDeleteCompiledModelHandler> { DeleteCompiledModelHandlerImpl(get()) }
    single<IHighlightLismaHandler> { get<IHighlightLismaHandler>() }  // resolved from infrastructure
}
```

All handlers are registered as **singletons** (`single`) — they are stateless and delegate to injected dependencies.

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

**Parameters:** `RunSimulationParameters` data class:

```kotlin
data class RunSimulationParameters(
    val startTime: Double,
    val endTime: Double,
    val initialStep: Double,
    val methodName: String,
    val accuracy: Double,
    val isAccuracyInUse: Boolean,
    val isStabilityControlInUse: Boolean,
    val compiledModelId: String,
    val eventDetectionGamma: Double? = null,
    val eventDetectionLowBorder: Double? = null,
)
```

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

**Flow:**
```kotlin
while (true) {
    val currentSession = sessionStore.get(simulationId)
    val currentTime = currentSession.currentTime

    // Determine whether to report
    val isFinal = currentSession.status != SimulationStatus.RUNNING
    val shouldReport = isFinal || (threshold > 0.0 && (currentTime - lastReportedTime) >= threshold)

    if (shouldReport) {
        onProgress(SimulationProgress(...))
        lastReportedTime = currentTime
    }

    if (isFinal) break
    Thread.sleep(100L)  // poll interval
}
```

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

**Flow:**
```kotlin
val translationResult = translator.translate(sourceCode)

translationResult.fold(
    onSuccess = { hsm ->
        val modelId = compiledModelStore.create(hsm)
        CompileLismaResult(compiledModelId = modelId, errors = [], warnings = [])
    },
    onFailure = { error ->
        // Extract errors from TranslationException or fallback to validation
        val ismaErrors = when (error) {
            is TranslationException -> error.errors
            else -> translator.validate(sourceCode)  // fallback
        }
        // Map IsmaSyntaxError/IsmaSemanticError → CompilationError
        CompileLismaResult(compiledModelId = "", errors = compilationErrors, warnings = [])
    }
)
```

**Error recovery:** On translation failure, the handler falls back to validation to extract detailed error positions if the original exception wasn't a `TranslationException`.

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

```kotlin
data class SimulationSession(
    val simulationId: Long = 0L,
    val startTime: Double,
    val endTime: Double,
    val currentTime: Double = 0.0,
    val status: SimulationStatus = SimulationStatus.RUNNING,
    val resultFilePath: String? = null,
    val error: String? = null,
)
```

**Lifecycle:**
1. **Created** with `RUNNING` status, `startTime`, `endTime`
2. **Progressed** — `currentTime` is updated on each simulation step
3. **Terminal states:**
   - `COMPLETED` — `resultFilePath` is set
   - `FAILED` — `error` message is set
   - `CANCELLED` — explicitly cancelled
   - `RUNNING` — still in progress

### SimulationStatus

```kotlin
enum class SimulationStatus {
    RUNNING,    // Simulation is executing
    COMPLETED,  // Finished successfully
    FAILED,     // Execution failed with error
    CANCELLED,  // Explicitly cancelled by client
}
```

### Supporting Data Classes

```kotlin
// Handler return types
data class RunningSimulationResult(val simulationId: Long)
data class SimulationProgress(val startTime: Double, val endTime: Double, val currentTime: Double)
data class SimulationMethodItem(val name: String, val title: String)
data class CompilationError(val row: Int, val column: Int, val message: String)
data class CompileLismaResult(val compiledModelId: String, val errors: List<CompilationError>, val warnings: List<String>)
data class ValidateLismaResult(val errors: List<CompilationError>, val warnings: List<String>)
data class HighlightLismaResult(val tokens: List<SyntaxToken>)
data class SyntaxToken(val start: Int, val length: Int, val kind: SyntaxKind)
```

---

## Domain Interfaces (Contracts)

### ICompiledModelStore

```kotlin
interface ICompiledModelStore {
    fun create(hsm: HSM): String
    fun get(id: String): HSM?
    fun delete(id: String): Boolean
    fun exists(id: String): Boolean
}
```

Manages in-memory storage of compiled HSM models keyed by UUID.

### ISimulationSessionStore

```kotlin
interface ISimulationSessionStore {
    fun create(startTime: Double, endTime: Double): SimulationSession
    fun get(id: Long): SimulationSession?
    fun getAll(): Map<Long, SimulationSession>
    fun update(id: Long, session: SimulationSession): SimulationSession
    fun updateStatus(id: Long, status: SimulationStatus)
    fun updateProgress(id: Long, currentTime: Double)
    fun completeSimulation(id: Long, resultFilePath: String)
    fun failSimulation(id: Long, error: String)
    fun delete(id: Long): Boolean
    fun exists(id: Long): Boolean
}
```

Manages simulation session state. All methods are thread-safe via `ConcurrentHashMap`.

### ISimulationExecutor

```kotlin
interface ISimulationExecutor {
    fun execute(simulationId: Long, parameters: RunSimulationParameters, hsm: HSM)
}
```

Executes a simulation asynchronously. Implementation runs on a separate thread from the `ExecutorService`.

### IIntegrationMethodsStore

```kotlin
interface IIntegrationMethodsStore {
    fun getMethodNames(): List<String>
    fun getMethod(name: String): IIntegrationMethodFactory
}
```

Provides access to available numerical integration methods.

### ILismaTranslator

```kotlin
interface ILismaTranslator {
    fun translate(sourceCode: String): Result<HSM>
    fun validate(sourceCode: String): IsmaErrorList
}

class TranslationException(val errors: IsmaErrorList) : Exception("Translation failed")
```

Translates LISMA source code to HSM models. The `translate()` method returns `Result<HSM>` — `success` with the model or `failure` with a `TranslationException`.

### IHighlightLismaHandler

```kotlin
interface IHighlightLismaHandler {
    fun handle(sourceCode: String): HighlightLismaResult
}
```

Lexical analysis contract for syntax highlighting.
