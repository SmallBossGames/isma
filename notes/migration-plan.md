# Migration Plan: Simulation Execution into `RunSimulationHandler`

## Context

`RunSimulationHandlerImpl` currently only generates a UUID. The simulation execution logic exists in `isma-ui/app` via `SimulationService`, `LismaPdeService`, and `isma-next-core` components. This plan migrates that logic into the domain handler, enabling the gRPC server to run simulations.

## Architecture

- **Fire-and-forget**: `runSimulation` returns immediately with `simulationId`. Client uses `monitorSimulation` for progress and `getSimulationResult` for results.
- **Threading**: `ExecutorService` for background simulation execution
- **Result format**: CSV (matches existing proto `bytes result_data`)

## Phase 1: Dependencies & Domain Model

### 1.1 Add Dependencies

Add to `isma-server/infrastructure/build.gradle.kts`:
```kotlin
implementation(project(":isma-next-core"))
implementation(project(":isma-compiler:hsm-fdm"))
implementation(project(":isma-solver:lib-utils"))
```

### 1.2 Update `SimulationSession`

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/simulation/SimulationSession.kt`

```kotlin
data class SimulationSession(
    val simulationId: String,
    val startTime: Double,
    val endTime: Double,
    val currentTime: Double = 0.0,
    val status: SimulationStatus = SimulationStatus.RUNNING,
    val integrationResult: HybridSystemIntegrationResult? = null,
    val error: String? = null,
)
```

### 1.3 Update `ISimulationSessionStore`

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/simulation/ISimulationSessionStore.kt`

Add:
```kotlin
fun getBySimulationId(simulationId: String): SimulationSession?
fun updateStatus(simulationId: String, status: SimulationStatus)
fun updateProgress(simulationId: String, currentTime: Double)
fun completeSimulation(simulationId: String, result: HybridSystemIntegrationResult)
fun failSimulation(simulationId: String, error: String)
```

### 1.4 Create `ISimulationExecutor` in Domain

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/simulation/ISimulationExecutor.kt`

```kotlin
interface ISimulationExecutor {
    fun execute(
        simulationId: String,
        parameters: RunSimulationParameters,
        translatedHsm: HSM,
    )
}
```

This interface abstracts the execution logic. Implementation lives in infrastructure.

## Phase 2: Infrastructure Wiring

### 2.1 Update `InfrastructureModule`

Location: `isma-server/infrastructure/src/main/kotlin/ru/nstu/isma/server/infrastructure/InfrastructureModule.kt`

Add singletons:
- `IntegrationMethodsLibrary` → `IntegrationMethodLibraryLoader.load()`
- `IHsmCompiler` → `HsmCompiler()`
- `ISimulationExecutor` → `SimulationExecutorImpl(...)`

### 2.2 Create `SimulationExecutorImpl`

Location: `isma-server/infrastructure/src/main/kotlin/ru/nstu/isma/server/infrastructure/simulation/SimulationExecutorImpl.kt`

Responsibilities:
1. Create `IntegrationMethodProvider` from `methodName`, `accuracy`, `isAccuracyInUse` using `IntegrationMethodsLibrary`
2. Create `DaeSystemStepSolverFactory`
3. Create `HybridSystemSimulator`
4. Create `InMemorySimulationRunner` (or `InFileSimulationRunner`)
5. Create `SimulationCoreController`
6. Submit simulation to `ExecutorService`

Flow:
```
HSM + CauchyInitials
       ↓
SimulationCoreController.simulateAsync()
       ↓
HsmCompiler.compile() → HybridSystem
       ↓
SimulationRunner.run() → HybridSystemSimulator.runAsync()
       ↓
HybridSystemIntegrationResult
       ↓
Store in SimulationSessionStore (status=COMPLETED)
```

Progress callback chain:
```
HybridSystemSimulator
  → stepChangeHandlers(currentTime)
    → SimulationSessionStore.updateProgress(simulationId, currentTime)
```

### 2.3 Update `SimulationSessionStore`

Location: `isma-server/infrastructure/src/main/kotlin/ru/nstu/isma/server/infrastructure/stores/simulationSessions/SimulationSessionStore.kt`

Implement new methods from `ISimulationSessionStore`:
- `getBySimulationId` — search by `simulationId` field
- `updateStatus` — update status field
- `updateProgress` — update `currentTime` field
- `completeSimulation` — set status=COMPLETED, store `HybridSystemIntegrationResult`
- `failSimulation` — set status=FAILED, store error message

### 2.4 Enhance `LismaTranslatorImpl`

Location: `isma-server/infrastructure/src/main/kotlin/ru/nstu/isma/server/infrastructure/translation/LismaTranslatorImpl.kt`

Current: only calls `InputTranslator.translate()`

Update: handle PDE→FDM conversion (mirrors `LismaPdeService.translateLisma()`):
```kotlin
override fun translate(sourceCode: String): Result<HSM> {
    val errors = IsmaErrorList()
    val model = translator.translate(sourceCode, errors)
    
    if (model == null || errors.isNotEmpty()) {
        return Result.failure(...)
    }
    
    val processedModel = if (model.isPDE) {
        FDMConverter(model).convert() ?: return Result.failure(...)
    } else {
        model
    }
    
    return Result.success(processedModel)
}
```

## Phase 3: Handler Implementation

### 3.1 Update `RunSimulationHandlerImpl`

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/handlers/runSimulation/RunSimulationHandlerImpl.kt`

```kotlin
class RunSimulationHandlerImpl(
    private val lismaTranslator: ILismaTranslator,
    private val simulationSessionStore: ISimulationSessionStore,
    private val simulationExecutor: ISimulationExecutor,
) : IRunSimulationHandler {
    
    override fun handle(parameters: RunSimulationParameters): RunningSimulationResult {
        // 1. Translate LISMA → HSM
        val hsmResult = lismaTranslator.translate(parameters.lismaSourceCode)
        val hsm = hsmResult.getOrElse {
            throw IllegalArgumentException("LISMA translation failed: ${it.message}")
        }
        
        // 2. Create session (RUNNING)
        val simulationId = UUID.randomUUID().toString()
        val session = SimulationSession(
            simulationId = simulationId,
            startTime = parameters.startTime,
            endTime = parameters.endTime,
        )
        simulationSessionStore.create(session)
        
        // 3. Fire-and-forget execution
        simulationExecutor.execute(simulationId, parameters, hsm)
        
        // 4. Return immediately
        return RunningSimulationResult(simulationId)
    }
}
```

### 3.2 Update `MonitorSimulationHandlerImpl`

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/handlers/monitorSimulation/MonitorSimulationHandlerImpl.kt`

```kotlin
class MonitorSimulationHandlerImpl(
    private val sessionStore: ISimulationSessionStore,
) : IMonitorSimulationHandler {
    
    override fun handle(simulationId: String, onProgress: (SimulationProgress) -> Unit) {
        val session = sessionStore.getBySimulationId(simulationId)
            ?: throw IllegalArgumentException("Simulation not found: $simulationId")
        
        onProgress(SimulationProgress(
            startTime = session.startTime,
            endTime = session.endTime,
            currentTime = session.currentTime,
        ))
    }
}
```

### 3.3 Update `GetSimulationResultHandlerImpl`

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/handlers/getSimulationResult/GetSimulationResultHandlerImpl.kt`

```kotlin
class GetSimulationResultHandlerImpl(
    private val sessionStore: ISimulationSessionStore,
) : IGetSimulationResultHandler {
    
    override fun handle(simulationId: String): InputStream {
        val session = sessionStore.getBySimulationId(simulationId)
            ?: throw IllegalArgumentException("Simulation not found: $simulationId")
        
        if (session.status == SimulationStatus.FAILED) {
            throw IllegalStateException("Simulation failed: ${session.error}")
        }
        
        if (session.status != SimulationStatus.COMPLETED) {
            throw IllegalStateException("Simulation not completed yet")
        }
        
        val result = session.integrationResult
            ?: throw IllegalStateException("No integration result available")
        
        return serializeToCsv(result)
    }
    
    private fun serializeToCsv(result: HybridSystemIntegrationResult): InputStream {
        // Reuse logic from SimulationResultService:
        // - buildHeader(): x, DE variables, AE variables, RHS
        // - iterate resultPointProvider.results, write toCsvLine()
        // Return as ByteArrayInputStream
    }
}
```

### 3.4 Update `DomainModule`

Location: `isma-server/domain/src/main/kotlin/ru/nstu/isma/domain/DomainModule.kt`

```kotlin
val domainModule = module {
    single<IRunSimulationHandler> { RunSimulationHandlerImpl(get(), get(), get()) }
    single<IGetSimulationResultHandler> { GetSimulationResultHandlerImpl(get()) }
    single<IMonitorSimulationHandler> { MonitorSimulationHandlerImpl(get()) }
    single<IListSimulationMethodsHandler> { ListSimulationMethodsHandlerImpl(get()) }
}
```

## Phase 4: gRPC Layer Cleanup

### 4.1 Update `SimulationServiceGrpcImpl`

Location: `isma-server/app/src/main/kotlin/ru/nstu/isma/server/app/grpc/SimulationServiceGrpcImpl.kt`

Keep only:
- Validation that `lismaSourceCode` is not empty
- Parameter creation
- Handler call
- Response mapping

Move validation logic to handler if needed (optional).

### 4.2 Update `AppModule`

Remove `lismaTranslator` injection if still present.

## Implementation Order

1. Add dependencies (`isma-next-core`, `hsm-fdm`, `lib-utils`) → verify build
2. Update `SimulationSession` + `ISimulationSessionStore` + implementation
3. Create `ISimulationExecutor` in domain
4. Wire `IHsmCompiler`, `IntegrationMethodsLibrary` in `InfrastructureModule`
5. Implement `SimulationExecutorImpl`
6. Enhance `LismaTranslatorImpl` with FDM conversion
7. Implement `RunSimulationHandlerImpl`
8. Implement `MonitorSimulationHandlerImpl`
9. Implement `GetSimulationResultHandlerImpl`
10. Update `DomainModule` and wire everything
11. Clean up gRPC layer
12. Build and test end-to-end
