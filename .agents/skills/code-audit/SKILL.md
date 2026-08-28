---
name: code-audit
description: >
  Perform strict, systematic code audits on any requested component, module,
  or directory. Find bugs, architectural flaws, anti-patterns, and deviations
  from project conventions. Report each finding as a separate file under
  docs/todo/. Do not propose fixes — only describe problems.
license: MIT
metadata:
  audience: developers
  workflow: code-review
---

## What I do

- Audit a requested scope (file, directory, module, or feature)
- Identify bugs, logic errors, and missing error handling
- Flag architectural anti-patterns, tight coupling, and poor separation of concerns
- Detect deviations from project conventions and coding rules
- Review UI / view-model boundaries for proper separation of concerns
- Check for performance issues, memory leaks, and resource leaks
- Report each finding as a separate file in `docs/todo/code-audit-results/`

## When to use me

Use this skill when someone asks to:
- "Audit this component / module / feature"
- "Review this code for problems"
- "Find all issues in X"
- "Why doesn't this work?" (investigative audit)

## How I work

### 1. Understand the scope

- Confirm which files, classes, or features to audit
- Read the project's AGENTS.md or equivalent coding rules
- If the project has feature documentation, read it to understand intended behavior (but do not treat it as the architectural reference)

### 2. Build and test (catch compilation + runtime issues first)

- Run `./gradlew build` — report all build errors/warnings as findings
- Run `./gradlew test` — report failing tests as findings
- If the build fails, report the compiler errors as the first priority

### 3. Search for concrete bug patterns (active hunting, not passive reading)

Run these targeted searches across the requested scope. Each match is a potential finding:

**Kotlin / JVM correctness**
- `lateinit` usage without null checks before first access (UninitializedPropertyAccessException)
- `!!` (non-null assertion) usage — potential NullPointerException
- `as` (unsafe cast) without `is` check — potential ClassCastException
- `try-catch` catching `Exception` broadly instead of specific exceptions
- `catch` blocks that swallow exceptions without logging
- Mutable collections (`mutableListOf`, `mutableSetOf`, `mutableMapOf`) exposed publicly
- `Array<T>` used where `List<T>` or `Sequence<T>` is more appropriate (performance)
- `Any?` used instead of proper nullable types in public APIs

**Coroutines / concurrency bugs**
- `CoroutineScope` fields without structured concurrency (`SupervisorJob` or parent-child relationship)
- `launch` without `CoroutineExceptionHandler` (swallowed exceptions)
- `async` without corresponding `await` (fire-and-forget, results discarded)
- Shared mutable state accessed from multiple coroutines without `Mutex`, `AtomicReference`, or `Channel`
- `GlobalScope.launch` usage (breaks structured concurrency, leaks on cancellation)
- `runBlocking` in non-entry-point code (deadlock risk on limited thread pools)
- `Dispatchers.IO` used for CPU-bound work, `Dispatchers.Default` used for I/O
- Missing `cancel()` / `close()` on `CoroutineScope`-owning components (memory leaks)

**Null safety risks**
- Properties declared as non-nullable but assigned from nullable sources (gRPC, JSON, file I/O)
- `?:` operator used to suppress nulls silently instead of handling the absence case
- `var` fields that can be set to `null` — should be `val` with `T?` or properly initialized
- `requireNotNull()` / `checkNotNull()` used inappropriately (should be handled gracefully)

**Koin DI / dependency injection issues**
- Duplicate service registrations (same interface/type declared twice in Koin modules)
- `get<T>()` inside factory lambdas — check for circular dependencies
- Concrete types declared in `single { }` but resolved by interface elsewhere (inconsistent)
- Services used by ViewModels but not declared in any Koin module
- Lifetime mismatches: `single { }` resolving `factory { }` dependency (stale state)
- `factory { }` used for stateless services (unnecessary allocation)
- `single { }` used for per-request/per-operation services (memory leak)
- Missing `watch` / `startAsync` / `stopAsync` lifecycle management for Koin components

**Memory / resource leaks**
- JavaFX `AnimationTimer` or `Timeline` started but never stopped
- JavaFX `EventHandler` / `ChangeListener` / `InvalidationListener` subscriptions without unsubscription
- `FileInputStream` / `BufferedReader` / `PrintWriter` opened without `use {}` or explicit `close()`
- `ByteArrayOutputStream` / `StringWriter` accumulated without bounds (unbounded growth)
- `ObservableList` / `KeyValue` bindings not cleaned up
- Lambda captures holding references to large objects (view models, stages)

**gRPC / protobuf issues**
- Generated protobuf types (`*.GrpcKt`, `*.Protos`) used in domain/business logic (violates architecture rules)
- Missing `waitForReady()` or timeout configuration on gRPC calls (silent hangs)
- gRPC streaming (`streamOp`, `ClientCall`) without backpressure handling
- Proto field numbers referenced by magic numbers instead of named constants
- Missing `oneof` usage where one-of semantics are needed in proto definitions
- gRPC errors not mapped to application-level exceptions (raw `StatusException` leaks through)

**API design bugs**
- `Pair<T, U>` or `Triple<T, U, V>` used as return types instead of proper data classes
- `MutableList<T>` / `MutableSet<T>` exposed in public APIs (should be `List<T>` / `Set<T>`)
- Functions with 5+ parameters — should be a data class or builder
- `var` properties that are only set once — should be `val`
- Inconsistent error reporting: some functions throw, others return `Result<T>` or nullable

**Kotlin code rule violations**
- `var` used where `val` would suffice (per immutability-first principle)
- Top-level functions/classes in non-extension files (per AGENTS.md: top-level classes not in separate files)
- Missing `@Suppress` for intentional `unused` warnings
- `when` expressions missing `else` branch on non-nullable, non-enums (exhaustiveness)
- `object` used where `class` with DI would be more testable
- Extension functions with side effects (misleading — extensions should be pure)

**JavaFX / UI issues**
- UI updates from background threads (must use `Platform.runLater`)
- `Platform.runLater` used for non-UI work (performance — should be offloaded first)
- Large data sets bound directly to JavaFX collections (UI freeze)
- `EventHandler` lambdas capturing `this` (memory leak — stage/scene holds reference)
- JavaFX `Binding` / `Bindings` created but never invalidated
- `Stage` / `Scene` references in non-UI layers ( ViewModel / Service)
- `Task` / `Service` without `onFailed` / `onCancelled` handlers

### 4. Trace critical execution paths

For each critical operation in the scope, follow the full call chain:

**Critical operations to trace:**
- Project open → save → close lifecycle
- Simulation start/stop/monitor (UI → gRPC client → server → gRPC → UI)
- Error handling flow (how errors propagate from gRPC → facade → service → ViewModel → UI)
- Settings/preferences save/load
- Server process lifecycle (start → health check → stop)

**For each path, check:**
- Missing null checks at each boundary (especially gRPC response parsing)
- Unhandled exceptions (no try/catch at coroutine entry points or handler methods)
- Error propagation — how does the caller know the operation failed?
- State consistency — is the object in a valid state if an exception occurs mid-operation?
- Coroutine cancellation — does the operation respect `Job.cancel()`?
- gRPC timeout — is there a timeout on server communication?

### 5. Audit DI registrations (Koin modules)

Read Koin module files (`*Module.kt`, `*Modules.kt`, `*DI.kt`) and check:

- Duplicate registrations (same service type declared more than once across modules)
- Lifetime mismatches (`single` resolving `factory` dependency — stale state)
- Factory lambdas using `get<T>()` — check for circular dependencies
- Services resolved by concrete type instead of interface
- ViewModels / services declared but their dependencies are not in any module
- Cross-module dependencies not properly imported (missing `modules()` call)
- `factory { }` vs `single { }` — verify correct lifecycle choice

### 6. Verify gRPC ↔ domain boundary separation

Per AGENTS.md architecture rules, **never use gRPC-generated protobuf types in business logic**:

- Server `domain/` — should contain NO gRPC stubs or protobuf imports
- Server `infrastructure/` — maps gRPC types to domain types (and vice versa)
- UI `external-services/` — maps gRPC protobuf types to UI domain models
- UI `domain/` — should contain NO gRPC types, NO JavaFX types
- Check that mapping layers exist at every gRPC boundary
- Check that proto changes only affect mapping code, not domain logic

### 7. Report findings

- Create one file per finding in `docs/todo/code-audit-results/`
- File naming: `<short-descriptive-name>.md`
- Each file must contain:
  1. **Title** — one-line summary
  2. **Location** — file path and line numbers
  3. **Category** — correctness / architecture / quality / performance / security
  4. **Description** — what the problem is and why it matters
  5. **Evidence** — relevant code snippet with line references
  6. **Impact** — what could go wrong if not fixed

### 8. Do NOT

- Propose fixes or solutions (only describe problems)
- Audit code outside the requested scope
- Use feature docs as the architectural reference — they describe intent, not the current implementation
- Add subjective opinions — be specific and evidence-based

## Output format example

```markdown
# Missing coroutine exception handler on launch

**Location:** `isma-ui/app/src/main/kotlin/ru/isma/next/app/services/simulation/SimulationService.kt:89`
**Category:** correctness

The `viewModelScope.launch { }` block performs a gRPC call without a `CoroutineExceptionHandler`.
If the call throws, the exception is swallowed and the UI never learns about the failure.

```kotlin
// Line 89
viewModelScope.launch {
    val result = grpcClient.runSimulation(request)
    updateResult(result)
}
```

**Impact:** Silent failure — simulation errors are not reported to the user.
```
