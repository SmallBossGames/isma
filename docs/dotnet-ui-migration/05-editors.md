# Phase 5: Editors

This phase covers porting the text editor and blueprint editor components.

> **Note**: The IsmaUi.TextEditor project is a library (not a standalone app) that provides text editing components for use in the main IsmaUi.App application.

## Text Editor

### Kotlin Source Files

Location: `isma-ui/text-editor/src/main/kotlin/ru/isma/next/editor/text/`

| File | Description |
|------|-------------|
| `IsmaTextEditor.kt` | Main text editor component |
| `services/EditorPlatformService.kt` | Platform integration |
| `services/SyntaxHighlighterService.kt` | Syntax highlighting |
| `services/RemoteLismaHighlightingService.kt` | Remote highlighting |
| `services/contracts/IHighlightingService.kt` | Highlighting interface |
| `services/contracts/ISyntaxHighlighter.kt` | Syntax highlighter interface |
| `services/contracts/IEditorPlatformService.kt` | Platform service interface |

### C# Implementation

**Dependencies** (NuGet packages):
- `Avalonia.AvaloniaEdit` (11.4.1) - Rich text editor control
- `AvaloniaEdit.TextMate` (11.4.1) - TextMate grammar support (optional, for advanced highlighting)

**Project Structure**:
```
IsmaUi.TextEditor/
├── IsmaUi.TextEditor.csproj
├── Controls/
│   ├── IsmaTextEditor.axaml
│   └── IsmaTextEditor.axaml.cs
├── Services/
│   ├── RemoteHighlightingService.cs
│   └── EditorPlatformService.cs
├── Contracts/
│   ├── SyntaxTokenKind.cs
│   ├── SyntaxToken.cs
│   ├── ISyntaxHighlighter.cs
│   ├── IHighlightingService.cs
│   └── IEditorPlatformService.cs
├── ITextEditorFactory.cs
└── Assets/
```

### Syntax Highlighting Architecture

The text editor uses remote syntax highlighting via gRPC to communicate with the Lisma compiler service.

**Flow**:
1. User types in the text editor
2. Text changes trigger highlighting update
3. `RemoteHighlightingService` calls `ISyntaxHighlighter.Highlight()` 
4. gRPC client calls `LismaCompilerService.Highlight()` via Unix socket
5. Response tokens are converted to AvaloniaEdit styling spans
6. Spans are applied to the TextEditor

**Proto Contracts** (see `protos/simulation/highlight_request.proto` and `highlight_response.proto`):
```protobuf
message HighlightRequest {
  string source_code = 1;
}

message HighlightResponse {
  repeated SyntaxToken tokens = 1;
}

enum TokenKind {
  TOKEN_KIND_UNSPECIFIED = 0;
  KEYWORD = 1;
  COMMENT = 2;
  NUMBER = 3;
  TEXT = 4;
}

message SyntaxToken {
  int32 start = 1;
  int32 length = 2;
  TokenKind kind = 3;
}
```

**Contract Interfaces**:
```csharp
// SyntaxTokenKind.cs
public enum SyntaxTokenKind
{
    Unspecified,
    Keyword,
    Comment,
    Number,
    Text
}

// SyntaxToken.cs
public record SyntaxToken(int Start, int Length, SyntaxTokenKind Kind);

// ISyntaxHighlighter.cs
public interface ISyntaxHighlighter
{
    IReadOnlyList<SyntaxToken> Highlight(string sourceCode);
}

// IHighlightingService.cs
public interface IHighlightingService
{
    IReadOnlyList<TextSegment> CreateHighlightingStyleSpans(string source);
}
```

**Service Implementation**:
```csharp
public class RemoteHighlightingService : IHighlightingService
{
    private readonly ISyntaxHighlighter _syntaxHighlighter;

    public RemoteHighlightingService(ISyntaxHighlighter syntaxHighlighter)
    {
        _syntaxHighlighter = syntaxHighlighter;
    }

    public IReadOnlyList<TextSegment> CreateHighlightingStyleSpans(string source)
    {
        var tokens = _syntaxHighlighter.Highlight(source);
        var segments = new List<TextSegment>();
        
        foreach (var token in tokens.OrderBy(t => t.Start))
        {
            var styleClass = token.Kind switch
            {
                SyntaxTokenKind.Keyword => "syntax-keyword",
                SyntaxTokenKind.Comment => "syntax-comment",
                SyntaxTokenKind.Number => "syntax-decimal",
                _ => "syntax-default"
            };
            segments.Add(new TextSegment(token.Start, token.Length, styleClass));
        }
        
        return segments;
    }
}
```

### Editor Platform Service

Handles cut/copy/paste operations through events:
```csharp
public interface IEditorPlatformService
{
    event Action? CutEvent;
    event Action? CopyEvent;
    event Action? PasteEvent;

    void Cut();
    void Copy();
    void Paste();
}
```

### Text Editor Control

```csharp
public partial class IsmaTextEditor : UserControl
{
    private AvaloniaEdit.TextEditor? _textEditor;

    public static readonly StyledProperty<string> TextProperty =
        AvaloniaProperty.Register<IsmaTextEditor, string>(nameof(Text), string.Empty);

    public string Text
    {
        get => GetValue(TextProperty);
        set => SetValue(TextProperty, value);
    }

    public IsmaTextEditor()
    {
    }

    protected override void OnLoaded(RoutedEventArgs e)
    {
        base.OnLoaded(e);

        _textEditor = this.FindControl<AvaloniaEdit.TextEditor>("TextEditor");
        if (_textEditor != null)
        {
            _textEditor.TextChanged += OnTextChanged;
            _textEditor.Text = Text;
        }
    }

    private void OnTextChanged(object? sender, EventArgs e)
    {
        if (_textEditor?.Text != Text)
        {
            Text = _textEditor?.Text ?? string.Empty;
        }
    }
}
```

**XAML:**
```xml
<UserControl xmlns="https://github.com/avaloniaui"
             xmlns:avaloniaEdit="using:AvaloniaEdit"
             x:Class="IsmaUi.TextEditor.Controls.IsmaTextEditor">
    <avaloniaEdit:TextEditor x:Name="TextEditor"
                            FontFamily="Consolas"
                            FontSize="12"
                            ShowLineNumbers="True"
                            WordWrap="False" />
</UserControl>
```

**Two-way binding with tabs:** When Text changes in the editor, it updates the ViewModel's `Content` property (setting `IsDirty = true`). Setting `Content` from ViewModel pushes text to the editor on load.

---

## Blueprint Editor

### Kotlin Source Files

Location: `isma-ui/blueprint-editor/src/main/kotlin/ru/isma/next/editor/blueprint/`

| File | Description |
|------|-------------|
| `IsmaBlueprintEditor.kt` | Main blueprint editor |
| `models/BlueprintModel.kt` | Blueprint data model |
| `models/BlueprintStateModel.kt` | State model |
| `models/BlueprintTransactionModel.kt` | Transaction model |
| `models/BlueprintLoopTransactionModel.kt` | Loop transaction |
| `models/BlueprintEditorTransactionModel.kt` | Editor transaction |
| `controls/StateBox.kt` | State block control |
| `controls/TransactionArrow.kt` | Connection arrow |
| `controls/LoopTransactionArrow.kt` | Loop arrow |
| `controls/EditArrowPopOver.kt` | Edit popover |
| `constants/StateNames.kt` | State names |
| `utilities/JavaFxExtensions.kt` | JavaFX helpers |
| `services/ITextEditorFactory.kt` | Text editor factory |

### C# Mapping

**Models** (in IsmaUi.Domain):
```
IsmaUi.Domain/Models/
├── BlueprintModel.cs
├── BlueprintStateModel.cs
├── BlueprintTransactionModel.cs
└── BlueprintLoopTransactionModel.cs
```

**Services** (in IsmaUi.TextEditor):
```
IsmaUi.TextEditor/
└── ITextEditorFactory.cs
```

**Controls** (in IsmaUi.BlueprintEditor):
```
IsmaUi.BlueprintEditor/
├── Controls/
│   ├── StateBox.axaml
│   ├── StateBox.axaml.cs
│   ├── TransactionArrow.axaml
│   ├── TransactionArrow.axaml.cs
│   ├── LoopArrow.axaml
│   └── LoopArrow.axaml.cs
├── IsmaBlueprintEditor.axaml
├── IsmaBlueprintEditor.axaml.cs
└── MainWindow.axaml (updated)
```

### Implementation

```csharp
public partial class IsmaBlueprintEditor : UserControl
{
    public static readonly StyledProperty<BlueprintModel> ModelProperty =
        AvaloniaProperty.Register<IsmaBlueprintEditor, BlueprintModel>(nameof(Model));
        
    public BlueprintModel Model
    {
        get => GetValue(ModelProperty);
        set => SetValue(ModelProperty, value);
    }
    
    private Canvas _canvas;
    private readonly Dictionary<Guid, StateBox> _stateBoxes = new();
    
    public IsmaBlueprintEditor()
    {
        _canvas = new Canvas();
        Content = _canvas;
    }
    
    public void Render()
    {
        _canvas.Children.Clear();
        foreach (var state in Model.States)
        {
            var box = new StateBox { DataContext = state };
            Canvas.SetLeft(box, state.X);
            Canvas.SetTop(box, state.Y);
            _canvas.Children.Add(box);
            _stateBoxes[state.Id] = box;
        }
        
        foreach (var transition in Model.Transitions)
        {
            var arrow = new TransactionArrow
            {
                StartPoint = new Point(transition.From.X, transition.From.Y),
                EndPoint = new Point(transition.To.X, transition.To.Y)
            };
            _canvas.Children.Add(arrow);
        }
    }
}
```

## Toolkit Module

### Kotlin Source Files

Location: `isma-ui/toolkit/src/main/kotlin/ru/isma/javafx/extensions/`

| File | Description |
|------|-------------|
| `extensions/helpers/Properties.kt` | Property helpers |
| `extensions/coroutines/flow/CollectionsExtensions.kt` | Collection helpers |
| `extensions/controls/PropertiesGrid.kt` | Properties grid |
| `extensions/controls/ListViewExtensions.kt` | ListView helpers |
| `extensions/controls/ComboBox.kt` | ComboBox helpers |

### C# Mapping

```
IsmaUi.Toolkit/
├── IsmaUi.Toolkit.csproj
├── Controls/
│   ├── PropertiesGrid.axaml
│   └── PropertiesGrid.axaml.cs
└── Extensions/
    ├── ControlExtensions.cs
    └── ObservableCollectionExtensions.cs
```

## Libraries to Consider

| Library | Purpose | Status |
|---------|---------|--------|
| AvaloniaEdit | Text editing with syntax highlighting | Stable |
| AvaloniaEdit.TextMate | TextMate grammar support | Stable |
| Destructible | Object destruction | Community |

---

## Phase 4: App Module

### Kotlin Source Files

Location: `isma-ui/app/src/main/kotlin/ru/isma/next/app/`

| File | Description |
|------|-------------|
| `views/MainView.kt` | Main application layout |
| `views/toolbars/IsmaToolBar.kt` | Toolbar with simulation controls |
| `views/toolbars/IsmaMenuBar.kt` | Menu bar (File, Edit, View, Run, Help) |
| `views/tabpane/IsmaEditorTabPane.kt` | Tab container for editors |
| `services/project/ProjectService.kt` | Project management |
| `services/project/ProjectFileService.kt` | File I/O for .lisma/.lismabp |

### C# Implementation

```
IsmaUi.App/
├── IsmaUi.App.csproj
├── App.axaml.cs                    # DI setup
├── ViewModels/
│   ├── MainViewModel.cs            # Main coordinator
│   ├── ToolbarViewModel.cs         # Toolbar state
│   └── TabViewModel.cs             # Base + TextEditor/Blueprint tabs
├── Services/
│   ├── ProjectService.cs           # Project management
│   └── ProjectFileService.cs       # File I/O for .lisma/.lismabp
├── Views/
│   ├── MainWindow.axaml            # Hosts MainView
│   ├── MainView.axaml              # Main layout
│   ├── MainView.axaml.cs
│   └── TabPane/
│       ├── IsmaEditorTabPane.axaml
│       └── IsmaEditorTabPane.axaml.cs
└── Domain/Models/
    ├── IProjectModel.cs            # Project interface
    ├── ProjectType.cs              # Enum: Lisma, Blueprint
    ├── LismaProjectModel.cs        # .lisma file model
    └── BlueprintProjectModel.cs    # .lismabp file model
```

### Implementation Notes

**Main Layout:**
- Menu bar: File, Edit, View, Run, Help
- Toolbar: New, Open, Save, Run/Stop, Settings
- Tab pane: Hosts IsmaTextEditor and IsmaBlueprintEditor
- Status bar: Shows progress and messages

**DI Registration:**
```csharp
private static void ConfigureServices(IServiceCollection services)
{
    services.AddSingleton<MainWindowViewModel>();
    services.AddSingleton<MainViewModel>();
}
```

**Project File Service:**
- Opens .lisma/.isma files as LismaProjectModel
- Opens .lismabp files as BlueprintProjectModel (JSON deserialized)
- Save/SaveAs dialogs with extension filters
- Two-way binding with editors (Text syncs to Content, Blueprint syncs to Blueprint property)

**Current Editor Implementation:**
- Uses ViewLocator to create editors by tab type
- Text editor tabs use TextBox with monospace font
- Blueprint editor tabs show canvas with Main/Init state blocks (IsmaBlueprintEditor control has issues with TabControl)

### Remaining to Implement

- Settings panels (Method, Event detection, Cauchy initials)
- Simulation integration with gRPC client
- Complete IsmaBlueprintEditor integration
