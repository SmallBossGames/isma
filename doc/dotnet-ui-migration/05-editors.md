# Phase 5: Editors

This phase covers porting the text editor and blueprint editor components.

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

### C# Mapping

```
IsmaUi.TextEditor/
├── IsmaUi.TextEditor.csproj
├── Controls/
│   ├── IsmaTextEditor.axaml
│   └── IsmaTextEditor.axaml.cs
├── Services/
│   ├── SyntaxHighlightingService.cs
│   ├── RemoteHighlightingService.cs
│   └── IHighlightingService.cs
└── Contracts/
    ├── ISyntaxHighlighter.cs
    └── IEditorPlatformService.cs
```

### Implementation

Avalonia doesn't have a built-in rich text editor like JavaFX's RichTextArea. Options:

1. **AvaloniaEdit** - Port of AvalonEdit (used in SharpDevelop)
2. **Custom TextBox with custom rendering**
3. **WebView2 with embedded editor**

```csharp
// Using AvaloniaEdit
public partial class IsmaTextEditor : TextEditor
{
    public static readonly StyledProperty<string> TextProperty =
        AvaloniaProperty.Register<IsmaTextEditor, string>(nameof(Text));
        
    public string Text
    {
        get => GetValue(TextProperty);
        set => SetValue(TextProperty, value);
    }
    
    public IsmaTextEditor()
    {
        SyntaxHighlighting = "Lisma";
        ShowLineNumbers = true;
    }
}
```

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

```
IsmaUi.BlueprintEditor/
├── IsmaUi.BlueprintEditor.csproj
├── Controls/
│   ├── IsmaBlueprintEditor.axaml
│   ├── IsmaBlueprintEditor.axaml.cs
│   ├── StateBox.axaml
│   ├── StateBox.axaml.cs
│   ├── TransactionArrow.axaml
│   ├── TransactionArrow.axaml.cs
│   ├── LoopArrow.axaml
│   └── LoopArrow.axaml.cs
├── Models/
│   ├── BlueprintModel.cs
│   ├── StateModel.cs
│   ├── TransactionModel.cs
│   └── ConnectionModel.cs
├── ViewModels/
│   ├── BlueprintEditorViewModel.cs
│   └── StateViewModel.cs
└── Services/
    └── ITextEditorFactory.cs
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

## Dependencies

- AvaloniaEdit (for text editor)
- CommunityToolkit.Mvvm

## Libraries to Consider

| Library | Purpose | Status |
|---------|---------|--------|
| AvaloniaEdit | Text editing with syntax highlighting | Stable |
| Destructible | Object destruction | Community |
