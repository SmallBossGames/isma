# Phase 3: Domain Models

This phase covers porting pure domain models from Kotlin to C#. These models have no UI dependencies.

## Kotlin Source Files

Location: `isma-ui/domain/src/main/kotlin/ru/isma/next/domain/models/`

| File | Description |
|------|-------------|
| `SimulationResult.kt` | Simulation result data |
| `SimulationPoint.kt` | Single data point |
| `SimulationMetadata.kt` | Metadata about simulation |
| `SimulationProgress.kt` | Progress information |
| `MetricData.kt` | Metric data |
| `SimulationResultReader.kt` | Result file reader |
| `IEquationIndexProvider.kt` | Equation index interface |

## C# Mapping

### IsmaUi.Domain Project Structure

```
IsmaUi.Domain/
├── IsmaUi.Domain.csproj
├── Models/
│   ├── SimulationResult.cs
│   ├── SimulationPoint.cs
│   ├── SimulationMetadata.cs
│   ├── SimulationProgress.cs
│   └── MetricData.cs
├── Readers/
│   ├── ISimulationResultReader.cs
│   └── BinarySimulationResultReader.cs
└── Providers/
    └── IEquationIndexProvider.cs
```

## Model Definitions

### SimulationResult

```csharp
public record SimulationResult(
    string Id,
    SimulationMetadata Metadata,
    string ResultFilePath,
    DateTime StartTime,
    DateTime? EndTime,
    SimulationStatus Status
);
```

### SimulationPoint

```csharp
public readonly struct SimulationPoint
{
    public double[] Values { get; }
    
    public double this[int index] => Values[index];
    
    public SimulationPoint(double[] values)
    {
        Values = values;
    }
}
```

### SimulationMetadata

```csharp
public record SimulationMetadata(
    string Name,
    IReadOnlyList<string> ColumnNames,
    IReadOnlyList<string> EquationNames,
    int TotalSteps,
    double StepSize,
    DateTime CreatedAt
);
```

### SimulationProgress

```csharp
public record SimulationProgress(
    string SimulationId,
    int CurrentStep,
    int TotalSteps,
    double PercentComplete,
    string? CurrentEquation,
    SimulationStatus Status
);
```

### MetricData

```csharp
public record MetricData(
    string Name,
    double Value,
    MetricType Type,
    string? Unit
);

public enum MetricType
{
    Time,
    Value,
    Error,
    Custom
}
```

### SimulationStatus

```csharp
public enum SimulationStatus
{
    Pending,
    Running,
    Completed,
    Failed,
    Cancelled
}
```

## Reader Interfaces

### ISimulationResultReader

```csharp
public interface ISimulationResultReader
{
    SimulationMetadata ReadMetadata(string filePath);
    IAsyncEnumerable<SimulationPoint> ReadPointsAsync(
        string filePath,
        CancellationToken cancellationToken = default);
    IAsyncEnumerable<SimulationPoint> ReadPointsAsync(
        string filePath,
        int[] columnIndices,
        CancellationToken cancellationToken = default);
}
```

### IEquationIndexProvider

```csharp
public interface IEquationIndexProvider
{
    int GetEquationCount();
    int GetColumnCount();
    int GetColumnIndex(string equationName);
    string GetEquationName(int index);
}
```

## Implementation Notes

- Use C# `record` types for immutable data classes
- Use `IAsyncEnumerable<T>` for streaming data (like Kotlin's `Sequence<T>`)
- Follow C# naming conventions (PascalCase)
- No external dependencies in this project

## Serialization

If JSON serialization is needed, use System.Text.Json:

```csharp
public record SimulationMetadata(
    string Name,
    [property: JsonPropertyName("columnNames")] IReadOnlyList<string> ColumnNames,
    // ...
);
```
