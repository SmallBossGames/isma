# .agents/Agents.md - Project Structure Description

## ISMA (Инструментальное моделирование) 22

A Kotlin-based mathematical modeling environment with the following main components:

### Root Modules

- **grin/** - Analytical functions and GUI framework
  - `analytic-fu/` - Analytical function utilities
  - `gui/` - Graphical user interface components

- **isma-compiler/** - HSM (High School Math) compiler with multiple backends:
  - `hsm-core/` - Core compiler functionality
  - `hsm-fdm/` - Finite difference method backend
  - `hsm-jvm/` - JVM-specific implementation
  - `hsm-jvm-calcmodel/` - Calculation model support
  - `lisma-translator-hsm/` - LISMA to HSM translator

- **isma-ui/** - User interface components:
  - `app/` - Main application logic
  - `blueprint-editor/` - Blueprint editing tools
  - `grin-nested/` - Nested graphical representation
  - `text-editor/` - Text editing capabilities
  - `toolkit/` - UI toolkit utilities

- **isma-next-core/** - Next-generation core functionality
- **isma-next-math-common/** - Common math utilities for next-gen
- **isma-next-math-engine/** - Math engine for next-gen

- **isma-solver/** - Mathematical solver components

- **legacy/** - Legacy code and compatibility layer

- **models/** - Data models and serialization

### Build System

- Gradle-based build (build.gradle.kts)
- Multi-module project structure
- Settings defined in settings.gradle.kts

### Documentation

- `doc/` - Project documentation

### Tooling

- `.gradle/` - Gradle cache and build state
- `build/` - Build output directory
- `.idea/` - IntelliJ IDEA configuration