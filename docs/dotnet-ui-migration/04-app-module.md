# Phase 4: App Module

This phase covers porting the main application module, including views, viewmodels, and services.

## Kotlin Source Files

Location: `isma-ui/app/src/main/kotlin/ru/isma/next/app/`

### Launchers
| File | Description |
|------|-------------|
| `launcher/IsmaApplication.kt` | Main application entry point |
| `launcher/Launcher.kt` | Entry point |
| `launcher/GrinProcessLauncher.kt` | Launch GrIn for visualization |
| `launcher/DependecyInjectionRootModule.kt` | Koin DI configuration |

### Views
| File | Description |
|------|-------------|
| `views/MainView.kt` | Main window view |
| `views/toolbars/IsmaToolBar.kt` | Main toolbar |
| `views/toolbars/IsmaMenuBar.kt` | Menu bar |
| `views/toolbars/TasksPopOver.kt` | Tasks popover |
| `views/toolbars/SimulationProcessBar.kt` | Simulation progress bar |
| `views/toolbars/IsmaErrorListTable.kt` | Error list |
| `views/tabpane/IsmaEditorTabPane.kt` | Tab container |
| `views/dialogs/ItemsPickerDialog.kt` | Item picker dialog |
| `views/layout/Drawer.kt` | Drawer component |
| `views/settings/SettingsPanelView.kt` | Settings panel |
| `views/settings/CauchyInitialsView.kt` | Initial conditions |
| `views/settings/MethodSettingsView.kt` | Integration method settings |
| `views/settings/EventDetectionView.kt` | Event detection |
| `views/settings/ResultProcessingView.kt` | Result processing |

### ViewModels
| File | Description |
|------|-------------|
| `viewmodels/CauchyInitialsViewModel.kt` | Initial conditions VM |
| `viewmodels/IntegrationMethodParametersViewModel.kt` | Method params VM |
| `viewmodels/EventDetectionParametersViewModel.kt` | Event detection VM |
| `viewmodels/ResultProcessingParametersViewModel.kt` | Result processing VM |
| `viewmodels/ResultSavingParametersViewModel.kt` | Save params VM |

### Services
| File | Description |
|------|-------------|
| `services/simulation/SimulationService.kt` | Simulation orchestration |
| `services/simulation/SimulationResultService.kt` | Result handling |
| `services/simulation/SimulationParametersService.kt` | Parameters management |
| `services/project/ProjectService.kt` | Project management |
| `services/project/ProjectFileService.kt` | File I/O |
| `services/project/LismaPdeService.kt` | PDE translation |
| `services/editors/TextEditorFactory.kt` | Editor factory |
| `services/editors/SyntaxHighlighterService.kt` | Syntax highlighting |
| `services/preferences/PreferencesProvider.kt` | User preferences |
| `services/ModelErrorService.kt` | Error handling |

### Models
| File | Description |
|------|-------------|
| `models/simulation/SimulationParametersModel.kt` | Simulation params |
| `models/simulation/SimulationParametersModel.kt` | Params model |
| `models/simulation/InProgressSimulationModel.kt` | In-progress sim |
| `models/simulation/CompletedSimulationModel.kt` | Completed sim |
| `models/simulation/CauchyInitialsModel.kt` | Initial conditions |
| `models/simulation/IntegrationMethodParametersModel.kt` | Method params |
| `models/simulation/EventDetectionParametersModel.kt` | Event params |
| `models/simulation/ResultSavingParametersModel.kt` | Save params |
| `models/simulation/SaveTarget.kt` | Save target |
| `models/projects/LismaProjectModel.kt` | Text project |
| `models/projects/BlueprintProjectModel.kt` | Blueprint project |
| `models/projects/LismaTextModel.kt` | Text document |
| `models/preferences/PreferencesModel.kt` | User prefs |
| `models/preferences/WindowPreferencesModel.kt` | Window prefs |
| `models/preferences/DefaultFilesPreferencesModel.kt` | Default files |
| `models/ErrorViewModel.kt` | Error model |

### Extensions
| File | Description |
|------|-------------|
| `extensions/FormsExtentions.kt` | Form helpers |
| `extensions/ButtonExtensions.kt` | Button helpers |
| `utilities/BlueprintModelExenstions.kt` | Blueprint helpers |
| `constants/FileExtentions.kt` | File extensions |

## C# Mapping

### IsmaUi.App Project Structure

```
IsmaUi.App/
├── IsmaUi.App.csproj
├── App.axaml
├── App.axaml.cs
├── Program.cs
├── Views/
│   ├── MainWindow.axaml
│   ├── MainWindow.axaml.cs
│   ├── Controls/
│   │   ├── ToolBar.axaml
│   │   ├── MenuBar.axaml
│   │   ├── EditorTabPane.axaml
│   │   └── SettingsPanel.axaml
│   └── Dialogs/
├── ViewModels/
│   ├── MainWindowViewModel.cs
│   ├── SimulationViewModel.cs
│   ├── SettingsViewModel.cs
│   └── ProjectViewModel.cs
├── Services/
│   ├── SimulationService.cs
│   ├── ProjectService.cs
│   ├── PreferencesService.cs
│   └── GrinLauncherService.cs
├── Models/
│   ├── SimulationParameters.cs
│   ├── ProjectModel.cs
│   └── PreferencesModel.cs
├── Extensions/
│   └── ControlExtensions.cs
└── DependencyInjection/
    └── ServiceCollectionExtensions.cs
```

## Key Implementation Details

### Main Application

```csharp
// App.axaml.cs
public partial class App : Application
{
    public static IServiceProvider Services { get; private set; } = null!;
    
    public override void Initialize()
    {
        AvaloniaXamlLoader.Load(this);
    }
    
    public override void OnFrameworkInitializationCompleted()
    {
        if (ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
        {
            var services = new ServiceCollection();
            ConfigureServices(services);
            Services = services.BuildServiceProvider();
            
            desktop.MainWindow = new MainWindow
            {
                DataContext = Services.GetRequiredService<MainWindowViewModel>()
            };
        }
        
        base.OnFrameworkInitializationCompleted();
    }
}
```

### DI Configuration

```csharp
public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddIsmaUiServices(this IServiceCollection services)
    {
        // External services
        services.AddSingleton<SimulationServerManager>();
        services.AddSingleton<SimulationServerFacade>();
        services.AddSingleton<GrpcSimulationClient>();
        
        // App services
        services.AddSingleton<SimulationService>();
        services.AddSingleton<ProjectService>();
        services.AddSingleton<PreferencesService>();
        services.AddSingleton<GrinLauncherService>();
        
        // ViewModels
        services.AddTransient<MainWindowViewModel>();
        services.AddTransient<SimulationViewModel>();
        services.AddTransient<SettingsViewModel>();
        
        return services;
    }
}
```

### Main Window

```csharp
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
    }
}
```

```axaml
<!-- MainWindow.axaml -->
<Window xmlns="https://github.com/avaloniaui"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        Title="ISMA">
    <DockPanel>
        <MenuBar DockPanel.Dock="Top">
            <MenuItem Header="_File">
                <MenuItem Header="_New" Command="{Binding NewProjectCommand}"/>
                <MenuItem Header="_Open" Command="{Binding OpenProjectCommand}"/>
                <Separator/>
                <MenuItem Header="E_xit" Command="{Binding ExitCommand}"/>
            </MenuItem>
            <MenuItem Header="_Simulation">
                <MenuItem Header="_Run" Command="{Binding RunSimulationCommand}"/>
                <MenuItem Header="_Stop" Command="{Binding StopSimulationCommand}"/>
            </MenuItem>
        </MenuBar>
        
        <TabControl>
            <TabItem Header="Text Editor">
                <TextEditorView />
            </TabItem>
            <TabItem Header="Blueprint">
                <BlueprintEditorView />
            </TabItem>
        </TabControl>
    </DockPanel>
</Window>
```

## Dependencies

- IsmaUi.Domain
- IsmaUi.External
- IsmaUi.TextEditor
- IsmaUi.BlueprintEditor
- IsmaUi.Toolkit
- Avalonia.Desktop
- Avalonia.Themes.Fluent
- CommunityToolkit.Mvvm
- Microsoft.Extensions.DependencyInjection
- Microsoft.Extensions.Logging

## MVVM Pattern

Use CommunityToolkit.Mvvm:

```csharp
public partial class MainWindowViewModel : ObservableObject
{
    [ObservableProperty]
    private string _title = "ISMA";
    
    [ObservableProperty]
    private bool _isSimulationRunning;
    
    [RelayCommand]
    private async Task RunSimulationAsync()
    {
        IsSimulationRunning = true;
        try
        {
            await _simulationService.RunAsync();
        }
        finally
        {
            IsSimulationRunning = false;
        }
    }
}
```
