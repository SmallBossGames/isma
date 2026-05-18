# Proto Files Reference

All protobuf definitions are located in `protobuf-contracts/simulation/`.

## Files to Copy

Copy all `.proto` files from `protobuf-contracts/simulation/` to the new C# project.

## Proto Files List

| File | Description |
|------|-------------|
| `simulation_service.proto` | Main simulation service RPC definitions |
| `compiler_service.proto` | LISMA compiler service |
| `run_simulation_request.proto` | Request to run simulation |
| `run_simulation_response.proto` | Response from run simulation |
| `cancel_simulation_request.proto` | Request to cancel simulation |
| `cancel_simulation_response.proto` | Response from cancel |
| `get_simulation_result_request.proto` | Request result |
| `get_simulation_result_response.proto` | Result response |
| `monitor_simulation_request.proto` | Monitor progress request |
| `monitor_simulation_response.proto` | Progress response |
| `list_simulation_methods_request.proto` | List methods request |
| `list_simulation_methods_response.proto` | Methods list |
| `compile_request.proto` | Compilation request |
| `compile_response.proto` | Compilation response |
| `highlight_request.proto` | Syntax highlighting request |
| `highlight_response.proto` | Syntax highlighting response |
| `validate_request.proto` | Validation request |
| `simulation_method_item.proto` | Method description |
| `delete_compiled_model_request.proto` | Delete compiled model |

## Generating C# Code

### Option 1: Grpc.Tools

Add to `.csproj`:

```xml
<ItemGroup>
  <Protobuf Include="protos\*.proto" />
</ItemGroup>
```

### Option 2: NSwag

```bash
dotnet tool install --global NSwag
nswag proto /input:protos\simulation_service.proto /output:Generated.cs
```

## Service Definitions

### SimulationService

```protobuf
service SimulationService {
    rpc RunSimulation(RunSimulationRequest) returns (RunSimulationResponse);
    rpc CancelSimulation(CancelSimulationRequest) returns (CancelSimulationResponse);
    rpc GetSimulationResult(GetSimulationResultRequest) returns (GetSimulationResultResponse);
    rpc MonitorSimulation(MonitorSimulationRequest) returns (stream MonitorSimulationResponse);
    rpc ListSimulationMethods(ListSimulationMethodsRequest) returns (ListSimulationMethodsResponse);
}
```

### CompilerService

```protobuf
service CompilerService {
    rpc Compile(CompileRequest) returns (CompileResponse);
    rpc Validate(ValidateRequest) returns (ValidateResponse);
    rpc Highlight(HighlightRequest) returns (HighlightResponse);
    rpc DeleteCompiledModel(DeleteCompiledModelRequest) returns (DeleteCompiledModelResponse);
}
```

## Key Message Types

### RunSimulationRequest

```protobuf
message RunSimulationRequest {
    string model_id = 1;
    string method_name = 2;
    int32 iterations = 3;
    double step_size = 4;
    // ... other fields
}
```

### RunSimulationResponse

```protobuf
message RunSimulationResponse {
    string simulation_id = 1;
    bool success = 2;
    string error_message = 3;
    string result_file_path = 4;
}
```

## C# Generated Types

After generating, you'll get:

```csharp
public class SimulationServiceClient : ClientBase<SimulationServiceClient>
{
    public AsyncServerStreamingCall<MonitorSimulationResponse> MonitorSimulation(
        MonitorSimulationRequest request,
        CallOptions options);
        
    public AsyncUnaryCall<RunSimulationResponse> RunSimulation(
        RunSimulationRequest request,
        CallOptions options);
}

public class CompilerServiceClient : ClientBase<CompilerServiceClient>
{
    public AsyncUnaryCall<CompileResponse> Compile(
        CompileRequest request,
        CallOptions options);
}
```

## Unix Socket Connection

```csharp
var channel = new Channel(
    "unix:/tmp/isma.sock",
    ChannelCredentials.Insecure);

var simulationClient = new SimulationServiceClient(channel);
var compilerClient = new CompilerServiceClient(channel);
```

## References

- Original proto files: `protobuf-contracts/simulation/`
- gRPC Java implementation: `isma-ui/grpc/`
- Server implementation: `isma-server/grpc/`
