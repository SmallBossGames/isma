# ISMA (Инструментальное моделирование)

Kotlin-based mathematical modeling and symbolic computation environment for educational and analytical purposes.

---

## Technology Stack

- **Language:** Kotlin
- **JVM Runtime:** Java Virtual Machine
- **Build System:** Gradle with Kotlin DSL (`build.gradle.kts`)
- **IDE:** IntelliJ IDEA

---

## Project Modules

### Core Math & Analysis
- **grin/** - GUI framework and analytical functions
  - `gui/` - Swing-based GUI components
  - `gui:app/`, `gui:common/`, `gui:concatenation/` - GUI submodules
  - `analytic-fu/` - Analytical function utilities
  - `math/` - Mathematical core

### Compiler Infrastructure
- **isma-compiler/** - HSM (High School Math) compiler
  - `hsm-core/` - Core compiler
  - `hsm-fdm/` - Finite difference method backend
  - `hsm-jvm/`, `hsm-jvm-calcmodel/` - JVM implementation
  - `lisma-translator-hsm/` - LISMA to HSM translator

### User Interface
- **isma-ui/** - UI components
  - `app/` - Main application
  - `text-editor/` - Text editing
  - `blueprint-editor/` - Blueprint editing
  - `grin-nested/` - Nested graphical representation
  - `toolkit/` - UI utilities

### Next-Generation Core
- **isma-next-core/** - Core functionality
- **isma-next-math-engine/** - Math engine
- **isma-next-math-common/** - Common math utilities

### Solver Library
- **isma-solver/** - Numerical integration methods
  - `api/`, `core/` - Core solver API and implementation
  - `lib-meta/` - SPI interface for integration method factories
  - `lib-utils/` - Utilities
  - `lib:euler/`, `lib:rk2/`, `lib:rk3/`, `lib:rk31/`, `lib:rkmerson/`, `lib:rkfehlberg/` - Integration methods

### Protocol Buffers
- **protobuf-contracts/** - Shared protobuf definitions
  - `simulation/` - Simulation service proto files

### Server (gRPC-based)
- **isma-server/** - Server with Netty transport and Koin DI
  - `app/` - Application entry point
  - `domain/` - Business logic with handlers
  - `grpc/` - Generated gRPC stubs
  - `infrastructure/` - Infrastructure implementations
  - *See `isma-server/AGENTS.md` for details*

---

## Gradle Configuration

- **Centralized versions:** `gradle/libs.versions.toml`
- **All dependency/plugin versions** must be defined in `libs.versions.toml` with aliases
- Use `{ version.ref = "..." }` for version references
- No duplicate `group`, `version`, or `repositories` declarations
- `java.sourceCompatibility` and `java.targetCompatibility` must match

## Build

```bash
./gradlew build          # Build all modules
./gradlew :module:build  # Build specific module
```
