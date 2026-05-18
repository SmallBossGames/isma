# Phase 2: External Services

This phase covers the gRPC client layer for communicating with the ISMA server.

## Kotlin Source Files

Location: `isma-ui/external-services/src/main/kotlin/`

| File | Description |
|------|-------------|
| `GrpcSimulationClient.kt` | Netty-based gRPC client with Unix socket transport |
| `SimulationServerManager.kt` | Manages server process lifecycle (start/stop) |
| `SimulationServerFacade.kt` | High-level facade for simulation operations |
| `GrpcLismaCompilerClient.kt` | gRPC client for compiler service |
| `HttpSimulationClient.kt` | HTTP client (if needed) |
| `BinaryFilePointProvider.kt` | Binary file point reader |
| `BinaryEquationIndexProvider.kt` | Equation index provider |
| `RunSimulationParams.kt` | DTO for simulation parameters |

## C# Mapping

### IsmaUi.External Project Structure

```
IsmaUi.External/
├── IsmaUi.External.csproj
├── Grpc/
│   ├── GrpcSimulationClient.cs
│   ├── GrpcLismaCompilerClient.cs
│   └── UnixSocketChannelFactory.cs
├── Services/
│   ├── SimulationServerManager.cs
│   ├── SimulationServerFacade.cs
│   └── ServerLauncher.cs
├── Models/
│   └── RunSimulationParams.cs
└── Readers/
    ├── BinaryPointReader.cs
    └── EquationIndexProvider.cs
```

## Key Implementations

### Unix Socket Channel (C#)

```csharp
public class UnixSocketChannelFactory
{
    public static ChannelBase CreateChannel(string socketPath)
    {
        var socketAddress = new UnixDomainSocketAddress(socketPath);
        var endpoint = new UnixDomainSocketEndPoint(socketAddress);
        
        var connectionString = $"unix:{socketPath}";
        return new Channel(
            connectionString,
            ChannelCredentials.Insecure,
            new List<ChannelOption>
            {
                new ChannelOption(ChannelOptions.MaxConcurrentStreams, 100)
            });
    }
}
```

### SimulationServerManager

```csharp
public class SimulationServerManager
{
    private Process? _serverProcess;
    private readonly string _serverScriptPath;
    
    public void StartServer()
    {
        // Resolve script path from env var or system property
        var scriptPath = ResolveServerScriptPath();
        if (scriptPath == null)
            throw new InvalidOperationException("Server script not configured");
            
        _serverProcess = Process.Start(new ProcessStartInfo
        {
            FileName = scriptPath,
            UseShellExecute = false,
            RedirectStandardOutput = true
        });
    }
    
    public void StopServer()
    {
        _serverProcess?.Kill();
        _serverProcess = null;
    }
    
    public bool IsServerRunning()
    {
        // Check if socket file exists and server responds
    }
}
```

### SimulationServerFacade

```csharp
public class SimulationServerFacade
{
    private readonly GrpcSimulationClient _grpcClient;
    private readonly SimulationServerManager _serverManager;
    
    public async Task<RunSimulationResponse> RunSimulationAsync(
        RunSimulationParams parameters,
        IProgress<SimulationProgress>? progress = null,
        CancellationToken cancellationToken = default)
    {
        // Ensure server is running
        await _serverManager.EnsureRunningAsync();
        
        // Start simulation
        var response = await _grpcClient.RunSimulationAsync(parameters);
        
        // Monitor progress
        if (progress != null)
        {
            await MonitorProgressAsync(response.SimulationId, progress, cancellationToken);
        }
        
        return response;
    }
}
```

## Dependencies

- Grpc.Net.Client
- Google.Protobuf
- Microsoft.Extensions.Logging.Abstractions

## Environment Variables

| Variable | Description |
|----------|-------------|
| `ISMA_SERVER_SCRIPT` | Path to server launch script |
| `ISMA_GRIN_SCRIPT` | Path to GrIn launch script |

## System Properties (fallback)

| Property | Description |
|----------|-------------|
| `isma.server.script` | Path to server launch script |
| `isma.grin.script` | Path to GrIn launch script |

## GrIn Process Launcher

The C# client must also be able to launch GrIn for results visualization:

```csharp
public class GrinProcessLauncher
{
    private Process? _grinProcess;
    
    public void Launch(string resultFile, string xAxisColumn, List<string> chartColumns)
    {
        var grinScriptPath = ResolveGrinScriptPath();
        
        var args = new[]
        {
            "--result-file", resultFile,
            "--x-axis", xAxisColumn,
            "--charts", string.Join(",", chartColumns)
        };
        
        _grinProcess = Process.Start(new ProcessStartInfo
        {
            FileName = grinScriptPath,
            Arguments = string.Join(" ", args),
            UseShellExecute = false
        });
    }
    
    public void Stop()
    {
        _grinProcess?.Kill();
        _grinProcess = null;
    }
}
```

This mirrors the Kotlin implementation in `isma-ui/app/src/main/kotlin/ru/isma/next/app/launcher/GrinProcessLauncher.kt`.
