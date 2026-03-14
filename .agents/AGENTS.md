# .agents/AGENTS.md - Project Structure Description

## ISMA (Инструментальное моделирование)

A Kotlin-based mathematical modeling and symbolic computation environment for educational and analytical purposes.

---

## 🛠️ Technology Stack

- **Language:** Kotlin
- **JVM Runtime:** Java Virtual Machine
- **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`)
- **IDE Support:** IntelliJ IDEA (`.idea/` configuration)
- **Architecture:** Multi-module Gradle project

---

## 📦 Root Modules

### Core Mathematics & Analysis
- **grin/** - Analytical functions and GUI framework
  - `analytic-fu/` - Analytical function utilities and helpers
  - `gui/` - Graphical user interface components

### Compiler Infrastructure
- **isma-compiler/** - HSM (High School Math) compiler with multiple backends:
  - `hsm-core/` - Core compiler functionality
  - `hsm-fdm/` - Finite difference method backend
  - `hsm-jvm/` - JVM-specific implementation
  - `hsm-jvm-calcmodel/` - Calculation model support
  - `lisma-translator-hsm/` - LISMA to HSM translator

### User Interface
- **isma-ui/** - User interface components:
  - `app/` - Main application logic
  - `blueprint-editor/` - Blueprint editing tools
  - `grin-nested/` - Nested graphical representation
  - `text-editor/` - Text editing capabilities
  - `toolkit/` - UI toolkit utilities

### Next-Generation Core
- **isma-next-core/** - Next-generation core functionality
- **isma-next-math-common/** - Common math utilities for next-gen
- **isma-next-math-engine/** - Math engine for next-gen

### Solvers & Models
- **isma-solver/** - Mathematical solver components
- **models/** - Data models and serialization

### Compatibility Layer
- **legacy/** - Legacy code and compatibility layer

---

## 🔧 Build System

- **Build Tool:** Gradle with Kotlin DSL (`build.gradle.kts`)
- **Project Type:** Multi-module Gradle project
- **Configuration:** Module settings in `settings.gradle.kts`
- **Cache Directory:** `.gradle/`
- **Build Output:** `build/`

---

## 📚 Documentation & Tooling

- **doc/** - Project documentation
- **.idea/** - IntelliJ IDEA configuration and project settings
