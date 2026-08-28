# GrIn (Graphical Interactive Functions)

JavaFX-based interactive function visualization and analysis environment.

## Documentation

| Document | Description |
|----------|-------------|
| [01-overview.md](./01-overview.md) | Architecture, module structure, design principles, communication flows |

## Module Structure

| Submodule | Purpose |
|-----------|---------|
| `grin/analytic-fu/` | Analytical function parser, expression evaluator, validator — Litter AST, RPN calculator |
| `grin/math/` | Numerical math primitives — derivatives, integration, intersection search (coroutines) |
| `grin/gui/common/` | Shared UI models and draw elements — Point, ChainDrawer, Converter interface, SettingsProvider |
| `grin/gui/concatenation/` | Main canvas application — functions, axes, cartesian spaces, transformations, file I/O, project save/load |
| `grin/gui/app/` | Application entry point, Koin DI root, command-line config |

## Quick Start

### Build

Run `./gradlew :grin:build` to build all GrIn submodules.

### Run

Run `./gradlew :grin:gui:app:run` to launch the application.

### Run with data file

Run `./gradlew :grin:gui:app:run` with arguments like `--args="--result-file data.ismx --x-axis x --charts y1,y2"` to load a data file on startup.

Command-line arguments:

| Argument | Short | Description |
|----------|-------|-------------|
| `--result-file` | `-r` | Path to data file (ISMX format or any CSV/XLSX) |
| `--x-axis` | `-x` | Name of the column to use as X axis |
| `--charts` | `-c` | Comma-separated list of column names to plot |

## Key Files

### Application Entry

| File | Path |
|------|------|
| Application entry point | `grin/gui/app/src/main/kotlin/.../launcher/GrinApplication.kt` |
| Launcher | `grin/gui/app/src/main/kotlin/.../launcher/Launcher.kt` |
| DI root module | `grin/gui/app/src/main/kotlin/.../launcher/DependecyInjectionRootModule.kt` |
| Command-line config | `grin/gui/app/src/main/kotlin/.../launcher/Launcher.kt` (GrinCommandLineConfig) |

### Canvas Core

| File | Path |
|------|------|
| Canvas model | `grin/gui/concatenation/src/main/kotlin/.../canvas/model/ConcatenationCanvasModel.kt` |
| Canvas view model | `grin/gui/concatenation/src/main/kotlin/.../canvas/model/ConcatenationCanvasViewModel.kt` |
| Canvas view | `grin/gui/concatenation/src/main/kotlin/.../canvas/view/ConcatenationCanvas.kt` |
| Canvas controller | `grin/gui/concatenation/src/main/kotlin/.../canvas/controller/ConcatenationCanvasController.kt` |
| Chain drawer | `grin/gui/concatenation/src/main/kotlin/.../canvas/view/ConcatenationChainDrawer.kt` |
| Matrix transformer | `grin/gui/concatenation/src/main/kotlin/.../canvas/controller/MatrixTransformer.kt` |
| Mouse handlers | `grin/gui/concatenation/src/main/kotlin/.../canvas/handlers/PressedMouseHandler.kt`, `DraggedHandler.kt`, `ReleaseMouseHandler.kt`, `ScalableScrollHandler.kt` |
| Edit mode | `grin/gui/concatenation/src/main/kotlin/.../canvas/model/EditMode.kt` |

### Function System

| File | Path |
|------|------|
| Function model | `grin/gui/concatenation/src/main/kotlin/.../function/model/ConcatenationFunction.kt` |
| Function operations service | `grin/gui/concatenation/src/main/kotlin/.../function/service/FunctionOperationsService.kt` |
| Function canvas service | `grin/gui/concatenation/src/main/kotlin/.../function/service/FunctionCanvasService.kt` |
| Function list view | `grin/gui/concatenation/src/main/kotlin/.../function/view/FunctionListView.kt` |
| Function list controller | `grin/gui/concatenation/src/main/kotlin/.../function/controller/FunctionListViewController.kt` |
| Function draw element | `grin/gui/concatenation/src/main/kotlin/.../function/view/ConcatenationFunctionDrawElement.kt` |
| Function DTO | `grin/gui/concatenation/src/main/kotlin/.../function/dto/ConcatenationFunctionDTO.kt` |
| Function converter | `grin/gui/concatenation/src/main/kotlin/.../function/converter/ConcatenationFunctionConverter.kt` |
| Add function modal | `grin/gui/concatenation/src/main/kotlin/.../function/view/AddFunctionModalView.kt` |
| Add function controller | `grin/gui/concatenation/src/main/kotlin/.../function/controller/AddFunctionController.kt` |

### Function Transformers

| File | Path |
|------|------|
| Transformer interface | `grin/gui/concatenation/src/main/kotlin/.../function/transform/IAsyncPointsTransformer.kt` |
| Derivative transformer | `grin/gui/concatenation/src/main/kotlin/.../function/transform/DerivativeTransformer.kt` |
| Mirror transformer | `grin/gui/concatenation/src/main/kotlin/.../function/transform/MirrorTransformer.kt` |
| Translate transformer | `grin/gui/concatenation/src/main/kotlin/.../function/transform/TranslateTransformer.kt` |
| Log transformer | `grin/gui/concatenation/src/main/kotlin/.../function/transform/LogTransformer.kt` |
| Integrator transformer | `grin/gui/concatenation/src/main/kotlin/.../function/transform/IntegratorTransformer.kt` |
| Wavelet transformer | `grin/gui/concatenation/src/main/kotlin/.../function/transform/WaveletTransformer.kt` |

### Cartesian Space System

| File | Path |
|------|------|
| Cartesian space model | `grin/gui/concatenation/src/main/kotlin/.../cartesian/model/CartesianSpace.kt` |
| Cartesian list view | `grin/gui/concatenation/src/main/kotlin/.../cartesian/view/CartesianListView.kt` |
| Cartesian list controller | `grin/gui/concatenation/src/main/kotlin/.../cartesian/controller/CartesianListViewController.kt` |
| Cartesian canvas service | `grin/gui/concatenation/src/main/kotlin/.../cartesian/service/CartesianCanvasService.kt` |

### Axis System

| File | Path |
|------|------|
| Axis model | `grin/gui/concatenation/src/main/kotlin/.../axis/model/ConcatenationAxis.kt` |
| Axis scale properties | `grin/gui/concatenation/src/main/kotlin/.../axis/model/AxisScaleProperties.kt` |
| Axis style properties | `grin/gui/concatenation/src/main/kotlin/.../axis/model/AxisStyleProperties.kt` |
| Axis list view | `grin/gui/concatenation/src/main/kotlin/.../axis/view/AxisListView.kt` |
| Axis list controller | `grin/gui/concatenation/src/main/kotlin/.../axis/controller/AxisListViewController.kt` |
| Axis canvas service | `grin/gui/concatenation/src/main/kotlin/.../axis/service/AxisCanvasService.kt` |
| Axis draw element | `grin/gui/concatenation/src/main/kotlin/.../axis/view/AxisDrawElement.kt` |
| Axis draw strategies | `grin/gui/concatenation/src/main/kotlin/.../axis/view/HorizontalAxisDrawStrategy.kt`, `VerticalAxisDrawStrategy.kt` |
| Axis marks builders | `grin/gui/concatenation/src/main/kotlin/.../axis/view/HorizontalValueMarksArrayBuilder.kt`, `VerticalValueMarksArrayBuilder.kt`, `HorizontalPixelMarksArrayBuilder.kt`, `VerticalPixelMarksArrayBuilder.kt` |

### Description System

| File | Path |
|------|------|
| Description model | `grin/gui/concatenation/src/main/kotlin/.../description/model/Description.kt` |
| Description list view | `grin/gui/concatenation/src/main/kotlin/.../description/view/DescriptionListView.kt` |
| Description list controller | `grin/gui/concatenation/src/main/kotlin/.../description/controller/DescriptionListViewController.kt` |
| Description canvas service | `grin/gui/concatenation/src/main/kotlin/.../description/service/DescriptionCanvasService.kt` |
| Description draw element | `grin/gui/concatenation/src/main/kotlin/.../description/view/DescriptionDrawElement.kt` |

### File I/O

| File | Path |
|------|------|
| File recognizer | `grin/gui/concatenation/src/main/kotlin/.../file/readers/FileRecognizer.kt` |
| CSV reader | `grin/gui/concatenation/src/main/kotlin/.../file/readers/CsvReader.kt` |
| XLS reader | `grin/gui/concatenation/src/main/kotlin/.../file/readers/XLSReader.kt` |
| XLSX reader | `grin/gui/concatenation/src/main/kotlin/.../file/readers/XLSXReader.kt` |
| Project loader (save/load) | `grin/gui/concatenation/src/main/kotlin/.../file/CanvasProjectLoader.kt` |
| Project snapshot models | `grin/gui/concatenation/src/main/kotlin/.../canvas/model/project/ProjectSnapshotModels.kt` |

### Analytical Expression Parser

| File | Path |
|------|------|
| Expression parser | `grin/analytic-fu/src/main/kotlin/.../parser/ExpressionParser.kt` |
| Expression converter (to RPN) | `grin/analytic-fu/src/main/kotlin/.../parser/ExpressionConverter.kt` |
| Litter AST | `grin/analytic-fu/src/main/kotlin/.../parser/Litter.kt` |
| Calculator (RPN evaluator) | `grin/analytic-fu/src/main/kotlin/.../calculation/Calculator.kt` |
| Function validator | `grin/analytic-fu/src/main/kotlin/.../validators/FunctionValidator.kt` |
| Binary operators | `grin/analytic-fu/src/main/kotlin/.../operators/BinaryOperator.kt` |

### Numerical Math

| File | Path |
|------|------|
| Derivatives | `grin/math/src/main/kotlin/.../math/Derivatives.kt` |
| Integration | `grin/math/src/main/kotlin/.../math/Integration.kt` |
| Intersection searcher | `grin/math/src/main/kotlin/.../math/IntersectionSearcher.kt` |

### DI Configuration

| File | Path |
|------|------|
| Concatenation Koin modules | `grin/gui/concatenation/src/main/kotlin/.../KoinModules.kt` |
| Koin scope extensions | `grin/gui/concatenation/src/main/kotlin/.../koin/KoinExtensions.kt` |

### Common Utilities

| File | Path |
|------|------|
| Converter interface | `grin/gui/common/src/main/kotlin/.../converters/Converter.kt` |
| Chain draw element | `grin/gui/common/src/main/kotlin/.../view/ChainDrawElement.kt` |
| Chain drawer | `grin/gui/common/src/main/kotlin/.../view/ChainDrawer.kt` |
| Point model | `grin/gui/common/src/main/kotlin/.../model/Point.kt` |
| Settings provider | `grin/gui/common/src/main/kotlin/.../common/SettingsProvider.kt` |
| Points builder | `grin/gui/common/src/main/kotlin/.../controller/PointsBuilder.kt` |

## Build Configuration

### Submodule Dependencies

| Submodule | Plugin | Key Dependencies |
|-----------|--------|-----------------|
| `analytic-fu` | `java-modules` | `kotlin-stdlib` |
| `math` | `java-modules` | `kotlin-stdlib`, `kotlinx-coroutines-core` |
| `gui/common` | `java-modules` | `:grin:analytic-fu`, `javafx.graphics` |
| `gui/concatenation` | `java-modules` + `kotlinx-serialization` | `TornadoFX`, `Koin`, `POI`, `JWave`, coroutines, `:grin:math`, `:isma-ui:toolkit`, `javafx.graphics`, `javafx.controls` |
| `gui/app` | `java-modules` + `application` | `:grin:gui:concatenation`, `isma-jvm-lib:exchange-format`, `koin-core`, `javafx.graphics` |

### Module-Info Declarations

| Module | Exports |
|--------|---------|
| `isma.grin.analytic.fu.main` | `ru.nstu.grin.model`, `parser`, `operators`, `validators`, `calculation` |
| `isma.grin.math.main` | `ru.nstu.grin.math` |
| `isma.grin.gui.common.main` | `ru.nstu.grin.common.common`, `controller`, `converters`, `draw.elements`, `extensions`, `model`, `view` |
| `isma.grin.gui.concatenation.main` | `axis.controller/service/view/model`, `canvas.controller/view/model/handlers`, `cartesian.controller/service/view/model`, `description.controller/service/view/model`, `file`, `file.options.view`, `function.controller/service/view/model`, `koin` |
| `isma.grin.gui.app` | `ru.nstu.isma.grin.launcher` |

### Tests

| Module | Test Files |
|--------|-----------|
| `analytic-fu` | `ExpressionParserTest.kt`, `ExpressionConverterTest.kt`, `CalculatorTest.kt` |
| `gui/app` | JUnit Jupiter (via build.gradle.kts) |
| `gui/concatenation` | `kfixture`, `mockito-kotlin` (test dependencies) |
