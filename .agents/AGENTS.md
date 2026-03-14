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

## 📜 Gradle Configuration Rules

### Rule 1: NO duplicate declarations in build.gradle.kts
- `group` and `version` must be declared **only once** at the top-level
- `repositories` block must appear **exactly once**
- No repetition of plugin declarations

### Rule 2: Correct application.mainClass syntax
- Use fully qualified class name with `.kotlin` extension if needed
- For Kotlin applications, use `mainClassName.set("ru.isma.server.ApplicationKt")`
- Do NOT mix Java and Kotlin conventions incorrectly

### Rule 3: Plugin declarations must be consistent
- All submodules should match the root project's plugin versions
- Use single quotes for strings in isma-server/ (`'ru.isma'`, `'1.0.0-SNAPSHOT'`)
- Double quotes are acceptable in root projects

### Rule 4: Repository configuration rules
- Declare repositories **once** at root level when possible
- Avoid duplicate repository blocks in child modules unless justified
- Always include `mavenCentral()` for standard dependencies

### Rule 5: Java/Kotlin version consistency
- `java.sourceCompatibility` and `java.targetCompatibility` must match
- `tasks.withType<KotlinCompile>` `jvmTarget` must align with Java versions
- Use `VERSION_<version number>` constant, not string "<version number>"

### Rule 6: Centralized dependencies & plugins (libs.versions.toml)
- All dependency versions MUST be defined in `gradle/libs.versions.toml` with aliases (`libs.<alias>`)
- All plugin versions MUST be defined in `gradle/libs.versions.toml` with aliases (`plugins.<alias>`)
- Use `{ version.ref = "..." }` to reference version variables from `[versions]` section
- NEVER hardcode version numbers directly in build.gradle.kts files
- Use aliases for readability: `implementation(libs.kotlin.reflect)` instead of full module names
- Apply plugins via alias: `plugins { id("libs.plugins.kotlin-jvm") version libs.plugins.kotlin-plugin }`

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
