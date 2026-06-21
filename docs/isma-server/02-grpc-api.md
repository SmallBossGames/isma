# gRPC API Reference

## Overview

The server exposes two gRPC services defined in `protobuf-contracts/v1/`:

| Service | Purpose |
|---------|---------|
| `SimulationService` | Simulation lifecycle: run, monitor, cancel, retrieve results, list methods |
| `LismaCompilerService` | LISMA language processing: compile, validate, highlight, delete compiled models |

Both services use **unary RPCs** (request/response) except `MonitorSimulation` which returns a **server-streaming** response.

---

## Service 1: SimulationService

### RPC Methods

```protobuf
service SimulationService {
  rpc RunSimulation(RunSimulationRequest) returns (RunSimulationResponse);
  rpc GetSimulationResult(GetSimulationResultRequest) returns (GetSimulationResultResponse);
  rpc MonitorSimulation(MonitorSimulationRequest) returns (stream MonitorSimulationResponse);
  rpc ListSimulationMethods(ListSimulationMethodsRequest) returns (ListSimulationMethodsResponse);
  rpc CancelSimulation(CancelSimulationRequest) returns (CancelSimulationResponse);
}
```

### RunSimulation

Starts a numerical simulation asynchronously. Returns immediately with a `simulationId`.

**Request:** `RunSimulationRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `compiled_model_id` | `string` | Yes | ID of the pre-compiled HSM model |
| `start_time` | `double` | Yes | Simulation start time (t₀) |
| `end_time` | `double` | Yes | Simulation end time (tₙ) |
| `initial_step` | `double` | Yes | Initial integration step size |
| `method_name` | `string` | Yes | Name of the integration method (e.g., "euler", "rk2") |
| `accuracy_config` | `AccuracyConfig` | Optional | If present, enables adaptive step size control |
| `stability_config` | `StabilityConfig` | Optional | If present, enables stability control for RK methods |
| `event_detection` | `EventDetectionConfig` | Optional | If present, enables event detection |

**AccuracyConfig:**

| Field | Type | Description |
|-------|------|-------------|
| `accuracy` | `double` | Target accuracy value for adaptive step size |

**EventDetectionConfig:**

| Field | Type | Description |
|-------|------|-------------|
| `gamma` | `double` | Event detection sensitivity parameter |
| `low_border` | `double` | Minimum step size for event detection (Java getter: `lowBorder`) |

**Response:** `RunSimulationResponse`

| Field | Type | Description |
|-------|------|-------------|
| `simulation_id` | `int64` | Auto-incremented ID for the simulation |

**Implementation:** `SimulationServiceGrpcImpl.runSimulation()` → `IRunSimulationHandler.handle()` → creates session, retrieves HSM model, submits async execution.

---

### GetSimulationResult

Returns the download URL for a completed simulation's result file.

**Request:** `GetSimulationResultRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `simulation_id` | `int64` | Yes | ID of the completed simulation |

**Response:** `GetSimulationResultResponse`

| Field | Type | Description |
|-------|------|-------------|
| `download_url` | `string` | Path: `/simulation/{simulationId}/download` |

**Note:** The download URL is served by the embedded Ktor HTTP server on a separate Unix socket.

**Implementation:** `SimulationServiceGrpcImpl.getSimulationResult()` → opens `InputStream` via handler (used only for validation, immediately closed) → returns download URL string `/simulation/{simulationId}/download`. The actual binary file download is served by the Ktor HTTP server on a separate Unix socket.

---

### MonitorSimulation

Streams progress updates for a running simulation.

**Request:** `MonitorSimulationRequest`

| Field | Type | Description |
|-------|------|-------------|
| `simulation_id` | `int64` | ID of the simulation to monitor |
| `accuracy` | `double` | Accuracy threshold for progress report frequency |

**Response:** `stream MonitorSimulationResponse` (server-streaming)

| Field | Type | Description |
|-------|------|-------------|
| `start_time` | `double` | Simulation start time |
| `end_time` | `double` | Simulation end time |
| `current_time` | `double` | Current simulation time (advances toward end_time) |

**Polling behavior:** The server-side handler polls the session store every 100ms on the gRPC thread. Reports are sent when:
1. The simulation reaches a time delta of at least `accuracy * timeRange` since the last report, or
2. The simulation finishes (any terminal status: COMPLETED, FAILED, CANCELLED)

**Accuracy parameter:** Controls report granularity. With `accuracy = 0.0`, only the final status is reported. With `accuracy = 0.1` and a 10-second simulation, reports are sent approximately every 1 second of simulated time.

**Implementation:** `SimulationServiceGrpcImpl.monitorSimulation()` → `IMonitorSimulationHandler.handle()` → polling loop with callback.

---

### ListSimulationMethods

Lists all available numerical integration methods.

**Request:** `ListSimulationMethodsRequest` (empty)

**Response:** `ListSimulationMethodsResponse`

| Field | Type | Description |
|-------|------|-------------|
| `methods[]` | `SimulationMethodItem` | List of available methods |

**SimulationMethodItem:**

| Field | Type | Description |
|-------|------|-------------|
| `name` | `string` | Machine-readable method name (e.g., "euler", "rk2") |
| `title` | `string` | Human-readable title (e.g., "Euler", "Runge-Kutta 2nd order") |

**Available methods:**
- `euler` — Euler (forward) method
- `rk2` — Runge-Kutta 2nd order
- `rk3` — Runge-Kutta 3rd order
- `rk31` — Runge-Kutta 3(1) embedded pair
- `rkmerson` — Merson's method
- `rkfehlberg` — Runge-Kutta-Fehlberg (RKF45)

**Discovery:** Methods are discovered at startup via Java `ServiceLoader` loading `IIntegrationMethodFactory` implementations.

**Implementation:** `SimulationServiceGrpcImpl.listSimulationMethods()` → `IListSimulationMethodsHandler.handle()` → `IntegrationMethodsStore.getMethodNames()`.

---

### CancelSimulation

Cancels a running simulation.

**Request:** `CancelSimulationRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `simulation_id` | `int64` | Yes | ID of the simulation to cancel |

**Response:** `CancelSimulationResponse` (empty)

**Behavior:** Sets session status to `CANCELLED`. The running simulation checks this status on each step and throws `InterruptedException` to halt execution.

**Error cases:**
- Simulation not found → gRPC `NOT_FOUND`
- Simulation not running → gRPC `FAILED_PRECONDITION`

**Implementation:** `SimulationServiceGrpcImpl.cancelSimulation()` → validates state → `ISimulationSessionStore.updateStatus(CANCELLED)`.

---

## Service 2: LismaCompilerService

### RPC Methods

```protobuf
service LismaCompilerService {
  rpc Compile(CompileRequest) returns (CompileResponse);
  rpc Validate(ValidateRequest) returns (ValidateResponse);
  rpc Delete(DeleteCompiledModelRequest) returns (DeleteCompiledModelResponse);
  rpc Highlight(HighlightRequest) returns (HighlightResponse);
}
```

### Compile

Translates LISMA source code to a compiled HSM model. The model is stored in-memory and a persistent ID is returned.

**Request:** `CompileRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `lisma_source_code` | `string` | Yes | Full LISMA source code text |

**Response:** `CompileResponse`

| Field | Type | Description |
|-------|------|-------------|
| `compiled_model_id` | `string` | UUID for the compiled model (empty string if compilation failed) |
| `errors[]` | `CompilationError` | List of compilation errors |
| `warnings[]` | `string` | List of warnings |

**Compilation pipeline:**
1. `ILismaTranslator.translate(sourceCode)` → produces `HSM` model or `TranslationException`
2. On success: `ICompiledModelStore.create(hsm)` → returns UUID
3. On failure: Extracts syntax/semantic errors from translator and returns them

**Translation stages:**
- LISMA source → HSM model (via `LismaTranslator`)
- If PDE model → `FDMConverter(model).convert()` → finite difference discretization

**Implementation:** `LismaCompilerServiceGrpcImpl.compile()` → `ICompileLismaHandler.handle()`.

---

### Validate

Validates LISMA source code without producing a compiled model. Returns all errors and warnings.

**Request:** `ValidateRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `lisma_source_code` | `string` | Yes | Full LISMA source code text |

**Response:** `ValidateResponse`

| Field | Type | Description |
|-------|------|-------------|
| `errors[]` | `CompilationError` | List of validation errors |
| `warnings[]` | `string` | List of warnings |

**Difference from Compile:** Does not produce a stored model. Only runs the translator in validation mode (populates `IsmaErrorList`).

**Implementation:** `LismaCompilerServiceGrpcImpl.validate()` → `IValidateLismaHandler.handle()` → `ILismaTranslator.validate()` → maps `IsmaSyntaxError`/`IsmaSemanticError` to `CompilationError`.

---

### Delete

Removes a previously compiled model from the in-memory store.

**Request:** `DeleteCompiledModelRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `compiled_model_id` | `string` | Yes | UUID of the compiled model |

**Response:** `DeleteCompiledModelResponse`

| Field | Type | Description |
|-------|------|-------------|
| `success` | `bool` | Whether the model was found and deleted |

**Implementation:** `LismaCompilerServiceGrpcImpl.delete()` → `IDeleteCompiledModelHandler.handle()` → `ICompiledModelStore.delete()`.

---

### Highlight

Performs lexical analysis on LISMA source code, returning syntax tokens for IDE highlighting.

**Request:** `HighlightRequest`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `source_code` | `string` | Yes | Source code to analyze |

**Response:** `HighlightResponse`

| Field | Type | Description |
|-------|------|-------------|
| `tokens[]` | `SyntaxToken` | List of syntax tokens |

**SyntaxToken:**

| Field | Type | Description |
|-------|------|-------------|
| `start` | `int32` | Character offset in source |
| `length` | `int32` | Token length |
| `kind` | `TokenKind` | Token category |

**TokenKind enum:**

| Value | Description |
|-------|-------------|
| `KEYWORD` | LISMA keywords (const, state, for, if, else, from, macro, set) |
| `COMMENT` | Single-line and block comments |
| `NUMBER` | Floating-point and decimal literals |
| `TEXT` | Reserved — never produced by the handler |

Note: `TEXT` is defined in the proto but the handler silently filters all non-keyword/non-comment/non-number tokens.

**Implementation:** Uses ANTLR4 `LismaLexer` to tokenize source code. Only keywords, comments, and numbers are returned (other token types are filtered out).

**Empty input:** Returns an empty token list (no error).

---

## Error Mapping

Both services use the same exception-to-gRPC-status mapping in their private `toStatusException()` method:

| Kotlin Exception | gRPC Status | Meaning |
|------------------|-------------|---------|
| `IllegalArgumentException` | `NOT_FOUND` | Resource (model/session) not found |
| `IllegalStateException` | `FAILED_PRECONDITION` | Operation not valid in current state |
| Other `Exception` | `INTERNAL` | Unexpected server error |

Validation (blank input) returns `INVALID_ARGUMENT` before reaching the handler:

```kotlin
if (request.lismaSourceCode.isBlank()) {
    responseObserver.onError(
        Status.INVALID_ARGUMENT.withDescription("LISMA source code is required").asException()
    )
    return
}
```

**Note:** `low_border` is the proto field name; the Java getter is `lowBorder`.
```
