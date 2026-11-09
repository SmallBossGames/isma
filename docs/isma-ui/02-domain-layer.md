# Domain Layer

## Purpose

The `domain` module provides pure Kotlin (no UI dependencies) data models and interfaces used across the UI layer for simulation results, progress tracking, and metadata. It serves as the contract between the server-returned binary data and the UI's visualization pipeline.

## Structure

The domain module source lives in `domain/src/main/kotlin/ru/isma/next/domain/models/` and contains: `IEquationIndexProvider.kt`, `MetricData.kt`, `SimulationMetadata.kt`, `SimulationPoint.kt`, `SimulationProgress.kt`, `SimulationResult.kt`, and `SimulationResultReader.kt`.

Note: `CodeRegion` is defined in the app module (`LismaTextModel.kt`) and `SaveTarget` is defined in the app module (`SaveTarget.kt`). Both are referenced by domain models but not part of this module.

## Module Configuration

**File:** `domain/build.gradle.kts`

The module applies the Kotlin JVM plugin and Java modules plugin. Its only dependency is `kotlinx-coroutines-core`. The `module-info.java` exports `ru.isma.next.domain.models`.

## Models

### SimulationResult

**File:** `SimulationResult.kt`

A data class holding `ByteArray` data and a `simulationId` (Long). Overrides `equals` and `hashCode` for content-based comparison of the byte array. See `SimulationResult.kt` for the full implementation. This model is defined in the domain layer but is largely superseded by `CompletedSimulationModel` in the app layer which wraps a cached file path.

### SimulationProgress

**File:** `SimulationProgress.kt`

A data class with `startTime`, `endTime`, and `currentTime` (all Double). Represents a snapshot of simulation timing sent from the server during `monitorSimulation()`. The UI normalizes `currentTime` to a 0.0–1.0 progress value. See `SimulationProgress.kt` for the full implementation.

### SimulationPoint

**File:** `SimulationPoint.kt`

A data class representing a single data point in the simulation output with `x` (independent variable/time), `yForDE` (dependent variables for differential equations as `DoubleArray`), and `rhs` (right-hand side values as `Array<DoubleArray>`, split into algebraic equation index 1 and differential equation index 0 parts). Uses `contentEquals` / `contentHashCode` for `DoubleArray` comparison. See `SimulationPoint.kt` for the full implementation.

### SimulationMetadata

**File:** `SimulationMetadata.kt`

A data class holding `columnNames: List<String>`. Column name metadata parsed from the binary result file. Column names use prefix conventions (`DE_`, `AE_`, `f`) that feed into `BinaryEquationIndexProvider`. See `SimulationMetadata.kt` for the full implementation.

### MetricData

**File:** `MetricData.kt`

A data class with `startTime` and `endTime` (both Long), providing a computed `simulationTime` property equal to `endTime - startTime`. Timing metadata for a simulation run. Currently used as a placeholder with default values. See `MetricData.kt` for the full implementation.

### SimulationResultReader

**File:** `SimulationResultReader.kt`

An interface declaring `results: Flow<SimulationPoint>` for coroutine-based streaming of simulation points. Implemented by `BinaryFilePointProvider` in the external-services module, which reads binary data via the exchange-format library. See `SimulationResultReader.kt` for the full interface.

### IEquationIndexProvider

**File:** `IEquationIndexProvider.kt`

An interface with methods `getDifferentialEquationCount()`, `getAlgebraicEquationCount()`, `getDifferentialEquationCode(index)`, and `getAlgebraicEquationCode(index)`. Used for deriving equation information from column name metadata. Implemented by `BinaryEquationIndexProvider` which parses column prefixes (`DE_`, `AE_`, `f`) to determine equation counts and codes. See `IEquationIndexProvider.kt` for the full interface.

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
