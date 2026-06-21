# Domain Layer

## Purpose

The `domain` module provides pure Kotlin (no UI dependencies) data models and interfaces used across the UI layer for simulation results, progress tracking, and metadata. It serves as the contract between the server-returned binary data and the UI's visualization pipeline.

## Structure

```
domain/src/main/kotlin/ru/isma/next/domain/models/
├── IEquationIndexProvider.kt
├── MetricData.kt
├── SimulationMetadata.kt
├── SimulationPoint.kt
├── SimulationProgress.kt
├── SimulationResult.kt
└── SimulationResultReader.kt
```

Note: `CodeRegion` is defined in the app module (`LismaTextModel.kt`) and `SaveTarget` is defined in the app module (`SaveTarget.kt`). Both are referenced by domain models but not part of this module.

## Module Configuration

**File:** `domain/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
```

Minimal module — only `kotlinx-coroutines-core` as a dependency. The `module-info.java` exports `ru.isma.next.domain.models`.

## Models

### SimulationResult

**File:** `SimulationResult.kt`

```kotlin
data class SimulationResult(
    val data: ByteArray,
    val simulationId: Long,
) {
    override fun equals(other: Any?): Boolean = other is SimulationResult && data.contentEquals(other.data)
    override fun hashCode(): Int = data.contentHashCode()
}
```

Container for simulation result byte data with content-based equality. Note: this model is defined in the domain layer but is largely superseded by `CompletedSimulationModel` in the app layer which wraps a cached file path.

### SimulationProgress

**File:** `SimulationProgress.kt`

```kotlin
data class SimulationProgress(
    val startTime: Double,
    val endTime: Double,
    val currentTime: Double,
)
```

Snapshot of simulation timing sent from the server during `monitorSimulation()`. The UI normalizes `currentTime` to a 0.0–1.0 progress value.

### SimulationPoint

**File:** `SimulationPoint.kt`

```kotlin
data class SimulationPoint(
    val x: Double,
    val yForDE: DoubleArray,
    val rhs: Array<DoubleArray>,
)
```

Represents a single data point in the simulation output:
- `x` — independent variable (time)
- `yForDE` — dependent variables for differential equations
- `rhs` — right-hand side values, split into algebraic equation (index 1) and differential equation (index 0) parts

Uses `contentEquals` / `contentHashCode` for `DoubleArray` comparison.

### SimulationMetadata

**File:** `SimulationMetadata.kt`

```kotlin
data class SimulationMetadata(
    val columnNames: List<String>,
)
```

Column name metadata parsed from the binary result file. Column names use prefix conventions (`DE_`, `AE_`, `f`) that feed into `BinaryEquationIndexProvider`.

### MetricData

**File:** `MetricData.kt`

```kotlin
data class MetricData(
    val startTime: Long,
    val endTime: Long,
) {
    val simulationTime: Long get() = endTime - startTime
}
```

Timing metadata for a simulation run. Currently used as a placeholder with default values.

### SimulationResultReader

**File:** `SimulationResultReader.kt`

```kotlin
interface SimulationResultReader {
    val results: Flow<SimulationPoint>
}
```

Coroutine flow interface for streaming simulation points. Implemented by `BinaryFilePointProvider` in the external-services module, which reads binary data via the exchange-format library.

### IEquationIndexProvider

**File:** `IEquationIndexProvider.kt`

```kotlin
interface IEquationIndexProvider {
    fun getDifferentialEquationCount(): Int
    fun getAlgebraicEquationCount(): Int
    fun getDifferentialEquationCode(index: Int): String
    fun getAlgebraicEquationCode(index: Int): String
}
```

Interface for deriving equation information from column name metadata. Implemented by `BinaryEquationIndexProvider` which parses column prefixes (`DE_`, `AE_`, `f`) to determine equation counts and codes.

## DI Configuration

The domain module has no DI registrations of its own — all domain models are created ad-hoc (data classes) or provided by the external-services module (`BinaryEquationIndexProvider`, `BinaryFilePointProvider`).

## Dependencies

| Module | Provides |
| --- | --- |
| `external-services` | `BinaryEquationIndexProvider` (implements `IEquationIndexProvider`) |
| `external-services` | `BinaryFilePointProvider` (implements `SimulationResultReader`) |
| `app` | `CompletedSimulationModel` — wraps `SimulationResultReader` + `IEquationIndexProvider` for UI |
| `app` | `CodeRegion` — line number tracking for LISMA conversion error mapping (`LismaTextModel.kt`) |
| `app` | `SaveTarget` — enum (`MEMORY` / `FILE`) for result storage destination (`SaveTarget.kt`) |
