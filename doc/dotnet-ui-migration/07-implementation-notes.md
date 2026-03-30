# Implementation Notes

## Build Configuration

### .csproj Template

```xml
<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <TargetFramework>net10.0</TargetFramework>
    <LangVersion>14</LangVersion>
    <Nullable>enable</Nullable>
    <ImplicitUsings>disable</ImplicitUsings>
    <RootNamespace>IsmaUi</RootNamespace>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="Avalonia" Version="11.2.0" />
    <PackageReference Include="Avalonia.Desktop" Version="11.2.0" />
    <PackageReference Include="Avalonia.Themes.Fluent" Version="11.2.0" />
    <PackageReference Include="CommunityToolkit.Mvvm" Version="8.3.2" />
    <PackageReference Include="Microsoft.Extensions.DependencyInjection" Version="8.0.1" />
    <PackageReference Include="Microsoft.Extensions.Logging" Version="8.0.1" />
    <PackageReference Include="Grpc.Net.Client" Version="2.66.0" />
    <PackageReference Include="Google.Protobuf" Version="3.28.2" />
    <PackageReference Include="Grpc.Tools" Version="2.66.0">
      <PrivateAssets>all</PrivateAssets>
      <IncludeAssets>runtime; build; native; contentfiles; analyzers; buildtransitive</IncludeAssets>
    </PackageReference>
  </ItemGroup>

  <ItemGroup>
    <Protobuf Include="protos\*.proto" />
  </ItemGroup>
</Project>
```

## gRPC with Unix Sockets

### Custom Channel Factory

```csharp
public static class UnixSocketChannelFactory
{
    public static Channel CreateChannel(string socketPath)
    {
        var socketsHttpHandler = new SocketsHttpHandler
        {
            EnableMultipleConnectors = false
        };
        
        var endPoint = new UnixDomainSocketEndPoint(socketPath);
        
        return new Channel(
            socketPath,
            new SelectorSocketsHttpTransportContext(socketsHttpHandler, endPoint),
            ChannelCredentials.Insecure,
            new List<ChannelOption>
            {
                new ChannelOption(ChannelOptions.MaxConcurrentStreams, 100)
            });
    }
    
    private class SelectorSocketsHttpTransportContext : SocketTransportContext
    {
        public SelectorSocketsHttpTransportContext(SocketsHttpHandler handler, EndPoint endPoint)
            : base(handler, endPoint, ConnectionKind.Unix)
        {
        }
    }
}
```

### Alternative: Using Grpc.Net.Client.Unix

```csharp
// Or use Grpc.Net.Client.Unix package
var channel = new Channel(
    "unix:/tmp/isma.sock",
    ChannelCredentials.Insecure);
```

## Process Management

### Starting Server Process

```csharp
public class SimulationServerManager
{
    private Process? _serverProcess;
    
    public void StartServer(string scriptPath)
    {
        var startInfo = new ProcessStartInfo
        {
            FileName = scriptPath,
            UseShellExecute = false,
            RedirectStandardOutput = true,
            RedirectStandardError = true,
            WorkingDirectory = Path.GetDirectoryName(scriptPath)
        };
        
        _serverProcess = Process.Start(startInfo);
    }
    
    public async Task<bool> WaitForServerReadyAsync(TimeSpan timeout)
    {
        var endTime = DateTime.UtcNow + timeout;
        
        while (DateTime.UtcNow < endTime)
        {
            if (File.Exists("/tmp/isma.sock"))
            {
                // Try to connect
                try
                {
                    using var channel = UnixSocketChannelFactory.CreateChannel("/tmp/isma.sock");
                    var client = new SimulationServiceClient(channel);
                    // Ping or simple RPC
                    return true;
                }
                catch
                {
                    await Task.Delay(100);
                }
            }
            await Task.Delay(100);
        }
        
        return false;
    }
}
```

## Logging

### Configuration

```csharp
public static class LoggingConfiguration
{
    public static ILoggingBuilder ConfigureLogging(this ILoggingBuilder builder)
    {
        builder.SetMinimumLevel(LogLevel.Information);
        builder.AddDebug();
        
        return builder;
    }
}
```

## Async Streams

### Kotlin to C# Mapping

Kotlin:
```kotlin
fun readAllPointsSequence(file: File): Sequence<SimulationPoint> = sequence {
    // yield points
}
```

C#:
```csharp
public async IAsyncEnumerable<SimulationPoint> ReadPointsAsync(
    [EnumeratorCancellation] CancellationToken cancellationToken = default)
{
    using var stream = File.OpenRead(_filePath);
    // read and yield
    await foreach (var point in reader.ReadPointsAsync(cancellationToken))
    {
        yield return point;
    }
}
```

## Project File Formats

### LISMA Project (.lisma)

JSON-based project file format:

```json
{
  "version": "1.0",
  "type": "lisma",
  "name": "My Project",
  "content": "...",
  "settings": {
    "method": "rk4",
    "stepSize": 0.01
  }
}
```

### Blueprint Project (.lismabp)

JSON-based blueprint:

```json
{
  "version": "1.0",
  "type": "blueprint",
  "name": "My Blueprint",
  "states": [...],
  "transitions": [...]
}
```

## Preferences Storage

### JSON File Location

```csharp
public class PreferencesService
{
    private readonly string _preferencesPath;
    
    public PreferencesService()
    {
        var appData = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
        _preferencesPath = Path.Combine(appData, "isma-ui", "preferences.json");
    }
    
    public async Task<Preferences> LoadAsync()
    {
        if (!File.Exists(_preferencesPath))
            return new Preferences();
            
        var json = await File.ReadAllTextAsync(_preferencesPath);
        return JsonSerializer.Deserialize<Preferences>(json) ?? new Preferences();
    }
}
```

## Error Handling

### Global Exception Handler

```csharp
public class Program
{
    public static void Main(string[] args)
    {
        AppDomain.CurrentDomain.UnhandledException += (sender, e) =>
        {
            LogException(e.ExceptionObject as Exception);
        };
        
        TaskScheduler.UnobservedTaskException += (sender, e) =>
        {
            LogException(e.Exception);
            e.SetObserved();
        };
        
        BuildAvaloniaApp().StartWithClassicDesktopLifetime(args);
    }
    
    private static void LogException(Exception? ex)
    {
        // Write to log file
    }
}
```

## Testing

### Unit Tests

```bash
dotnet new xunit -n IsmaUi.Domain.Tests
dotnet new xunit -n IsmaUi.External.Tests
```

### Integration Tests

Use test server or mock gRPC services:

```csharp
public class SimulationServiceTests
{
    [Fact]
    public async Task RunSimulation_ReturnsResult()
    {
        // Arrange
        var client = CreateMockClient();
        
        // Act
        var result = await client.RunSimulationAsync(request);
        
        // Assert
        Assert.True(result.Success);
    }
}
```
