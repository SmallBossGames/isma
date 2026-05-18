# Phase 1: Project Setup

## Create New Repository

```bash
# Create new .NET solution
dotnet new sln -n IsmaUi

# Create projects
dotnet new classlib -n IsmaUi.Domain -f net10.0
dotnet new classlib -n IsmaUi.External -f net10.0
dotnet new avalonia -n IsmaUi.App -f net10.0
dotnet new avalonia -n IsmaUi.TextEditor -f net10.0
dotnet new avalonia -n IsmaUi.BlueprintEditor -f net10.0
dotnet new avalonia -n IsmaUi.Toolkit -f net10.0

# Add to solution
dotnet sln add src/IsmaUi.Domain/IsmaUi.Domain.csproj
dotnet sln add src/IsmaUi.External/IsmaUi.External.csproj
dotnet sln add src/IsmaUi.App/IsmaUi.App.csproj
dotnet sln add src/IsmaUi.TextEditor/IsmaUi.TextEditor.csproj
dotnet sln add src/IsmaUi.BlueprintEditor/IsmaUi.BlueprintEditor.csproj
dotnet sln add src/IsmaUi.Toolkit/IsmaUi.Toolkit.csproj
```

## Add Dependencies

### IsmaUi.Domain
- No external dependencies (pure domain models)

### IsmaUi.External
- Grpc.Net.Client
- Google.Protobuf
- Microsoft.Extensions.Logging.Abstractions

### IsmaUi.App
- IsmaUi.Domain
- IsmaUi.External
- IsmaUi.TextEditor
- IsmaUi.BlueprintEditor
- IsmaUi.Toolkit
- Avalonia.Desktop
- Avalonia.Themes.Fluent
- CommunityToolkit.Mvvm
- Microsoft.Extensions.DependencyInjection

### All Projects
- Nullable enabled
- ImplicitUsings disabled

## Copy Proto Files

Copy all files from `protobuf-contracts/simulation/` to `protos/` in the new repository.

Generate C# code:
```bash
dotnet add package Grpc.Tools
# Add .proto files to project as <Protobuf Include="protos\*.proto">
```

## Project Structure

```
isma-ui-dotnet/
├── protos/
│   ├── simulation_service.proto
│   ├── compiler_service.proto
│   ├── run_simulation_request.proto
│   ├── run_simulation_response.proto
│   └── ... (all other proto files)
├── src/
│   ├── IsmaUi.Domain/
│   │   ├── IsmaUi.Domain.csproj
│   │   └── Models/
│   │       ├── SimulationResult.cs
│   │       ├── SimulationProgress.cs
│   │       └── ...
│   ├── IsmaUi.External/
│   │   ├── IsmaUi.External.csproj
│   │   └── Services/
│   │       ├── GrpcSimulationClient.cs
│   │       ├── SimulationServerManager.cs
│   │       └── ...
│   ├── IsmaUi.App/
│   │   ├── IsmaUi.App.csproj
│   │   ├── App.axaml
│   │   ├── App.axaml.cs
│   │   ├── ViewModels/
│   │   └── Views/
│   ├── IsmaUi.TextEditor/
│   ├── IsmaUi.BlueprintEditor/
│   └── IsmaUi.Toolkit/
├── IsmaUi.sln
└── README.md
```

## Configure .NET 10

Note: .NET 10 is not yet released (as of 2026). Use .NET 9 or .NET 8 LTS for now, then upgrade when .NET 10 is available.

```xml
<!-- IsmaUi.csproj -->
<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <TargetFramework>net10.0</TargetFramework>
    <LangVersion>14</LangVersion>
    <Nullable>enable</Nullable>
    <ImplicitUsings>disable</ImplicitUsings>
  </PropertyGroup>
</Project>
```

## References

- Original Kotlin source: `isma-ui/`
- Proto files: `protobuf-contracts/simulation/`
- GrIn launcher: `grin/gui/app/`
