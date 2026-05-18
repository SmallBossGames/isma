# GrIn (Graphical Interactive Functions)

JavaFX-based interactive function visualization and analysis environment.

## Module Structure

| Submodule | Purpose |
|-----------|---------|
| `grin/analytic-fu/` | Analytical function parser, expression evaluator, and validator |
| `grin/math/` | Numerical math primitives — derivatives, integration, intersection search |
| `grin/gui/common/` | Shared UI models and draw elements (points, grid, chain drawing) |
| `grin/gui/concatenation/` | Main canvas application — functions, axes, cartesian spaces, transformations |
| `grin/gui/app/` | Application entry point, Koin DI root, command-line config |

## Quick Start

### Build

```bash
./gradlew :grin:build
```

### Run

```bash
./gradlew :grin:gui:app:run
```

### Run with data file

```bash
./gradlew :grin:gui:app:run --args="--result-file data.ismx --x-axis x --charts y1,y2"
```

Command-line arguments:

| Argument | Short | Description |
|----------|-------|-------------|
| `--result-file` | `-r` | Path to data file (ISMX format or any CSV/XLSX) |
| `--x-axis` | `-x` | Name of the column to use as X axis |
| `--charts` | `-c` | Comma-separated list of column names to plot |

## Key Files

| File | Path |
|------|------|
| Application entry point | `grin/gui/app/src/main/kotlin/.../launcher/Launcher.kt` |
| DI root module | `grin/gui/app/src/main/kotlin/.../launcher/DependecyInjectionRootModule.kt` |
| Koin modules | `grin/gui/concatenation/src/main/kotlin/.../KoinModules.kt` |
| Expression parser | `grin/analytic-fu/src/main/kotlin/.../parser/ExpressionParser.kt` |
| Calculator | `grin/analytic-fu/src/main/kotlin/.../calculation/Calculator.kt` |
| Intersection searcher | `grin/math/src/main/kotlin/.../math/IntersectionSearcher.kt` |
| Canvas controller | `grin/gui/concatenation/src/main/kotlin/.../canvas/controller/ConcatenationCanvasController.kt` |
| Function model | `grin/gui/concatenation/src/main/kotlin/.../function/model/ConcatenationFunction.kt` |

## Documentation

- [01-overview.md](./01-overview.md) — Architecture, module structure, design principles, communication flows
