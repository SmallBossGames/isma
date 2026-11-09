# ISMA (Инструментальное моделирование)

Kotlin-based mathematical modeling and symbolic computation environment for educational and analytical purposes.

---

## Technology Stack

- **Language:** Kotlin
- **JVM Runtime:** Java Virtual Machine
- **Build System:** Gradle with Kotlin DSL (`build.gradle.kts`)
- **IDE:** IntelliJ IDEA

---

## Working Order — Read Docs Before Code

**Always read `docs/` before reading any source code.** When investigating or modifying a component:

1. Check if `docs/<module>/` exists (e.g. `docs/isma-ui/`)
2. Read the documentation first — it provides context, architecture, and API contracts
3. Only then read the source code files

---

## Project Modules

### Core Math & Analysis
- **grin/** - JavaFX-based interactive function visualization and analysis environment *(git submodule → [grin-javafx](https://github.com/SmallBossGames/grin-javafx), standalone Gradle project)*
  - `gui/` - JavaFX GUI components
  - `gui:app/`, `gui:common/`, `gui:concatenation/` - GUI submodules
  - `analytic-fu/` - Analytical function parser, expression evaluator, validator
  - `math/` - Numerical math primitives — derivatives, integration, intersection search

### Compiler Infrastructure
- **lisma-compiler/** - LISMA compiler
  - `lib/` - Minimal public compiler API (`LismaCompiler`: compile LISMA source to a self-contained `.class`, validate) for embedding into other JVM projects
  - `runtime/` - Minimal public API (`LismaModelRuntime.load`) to launch a model from a compiled `.class` file (fresh classloader per model, HSM blob deserialization)
  - `hsm-core/` - Core compiler
  - `hsm-fdm/` - Finite difference method backend
  - `hsm-jvm/`, `hsm-jvm-calcmodel/` - JVM implementation
  - `lisma-translator-hsm/` - LISMA → HSM translator (ANTLR4 grammar)
  - `cli/` - Standalone LISMA compiler CLI: thin wrapper over `lib`; reads LISMA from stdin, writes the compiled `.class` file (analyzed hybrid-system class with the translated HSM inlined as a serialized blob) to stdout (`lisma-compiler-cli`); also supports `--validate`; launched by the UI as a separate process

### User Interface
- **isma-ui/** - JavaFX-based UI components *(git submodule → [isma-ui-javafx](https://github.com/SmallBossGames/isma-ui-javafx), standalone Gradle project; see `isma-ui/AGENTS.md` for details)*
  - `app/` - Main application entry point
  - `domain/` - Domain models and business logic
  - `external-services/` - Direct CLI integration layer (compiler/runner CLIs) + LSP client
  - `text-editor/` - Text editing component
  - `blueprint-editor/` - Blueprint/visual editing component
  - `toolkit/` - Shared UI utilities

### Next-Generation Core
- **isma-next-math-engine/** - Math engine
- **isma-next-math-common/** - Common math utilities

### Solver Library
- **isma-solver/** - Numerical integration methods
  - `api/`, `core/` - Core solver API and implementation
  - `lib-meta/` - SPI interface for integration method factories
  - `lib-utils/` - Utilities
  - `lib:euler/`, `lib:rk2/`, `lib:rk3/`, `lib:rk31/`, `lib:rkmerson/`, `lib:rkfehlberg/` - Integration methods
  - `cli/` - Batteries-included simulation runner CLI. Accepts a compiled `.class` file + simulation parameters, loads the model via `lisma-compiler:runtime`, runs the simulation (equation index provider, initials), and writes the result file in the existing binary format. Ships the integration-method libs itself; launched by the UI as a separate process per simulation.

### Language Server (LSP)
- **lisma-compiler/lisma-lsp/** - Standalone LISMA language server: LSP over stdio (JSON-RPC 2.0, Content-Length framing), semantic tokens via ANTLR4 lexer
  - `protocol/` - LSP framing + request dispatch
  - `lsp/` - LISMA language service (semantic tokens) + token codec
  - `smoke.py` - stdio smoke test
  - *See `docs/isma-lsp/` for details*

### Documentation
- **docs/isma-ui/** - UI module documentation
- **docs/isma-lsp/** - Language server documentation
- **docs/grin/** - Grin (Graphical Interactive Functions) documentation
- **docs/dotnet-ui-migration/** - WPF to Avalonia migration documentation and guides
- **docs/legacy/** - Legacy system documentation and historical context
- **docs/todo/** - TODO templates and planning

### Out-of-Process Architecture (UI + CLIs)

The UI launches the two standalone CLIs as separate processes, and the `lisma-compiler/lisma-lsp` language server as a separate process communicating via LSP over stdio (syntax highlighting): the LISMA compiler CLI (`lisma-compiler:cli`, a thin wrapper over the `lisma-compiler:lib` public API) to compile LISMA sources to a compiled `.class` file (LISMA on stdin, `.class` on stdout), and the simulation runner CLI (`isma-solver:cli`, batteries-included) to run each simulation, streaming the result on stdout in the ISMA exchange binary format.

- **isma-ui/external-services/** - Direct CLI integration layer
  - `cli/` - CLI process layer: `CliProcess` (stdin/stdout/stderr + graceful termination), `CliLauncher` (path resolution + launch), `CliToolLaunchers` (compiler/runner launchers), `LismaCompilerCli` (compile/validate via stdin), `SimulationRunnerCli` (run + `--list-methods`), `ActiveSimulation` (streams the runner's binary stdout into a result file, exposes TIME values for progress)
  - `SimulationServerFacade` - High-level facade: compile (model ID = local `.class` path), validate, run/monitor/cancel (local simulation IDs), result download, method listing
  - `lsp/` - LSP client layer (`LspProcessManager`, `LspClient`, `LspTransport`) driving `isma-lsp`
- **Compiler CLI launch** — `ISMA_COMPILER_CLI` env var (bundle) or `isma.compiler.cli` sysprop (IDE/Gradle); path to the launch script
- **Runner CLI launch** — `ISMA_RUNNER_CLI` env var (bundle) or `isma.runner.cli` sysprop (IDE/Gradle); path to the launch script
- **LSP launch** — `ISMA_LSP_SCRIPT` env var (bundle) or `isma.lsp.script` sysprop (IDE/Gradle)
- **Bundle** — `.ci-cd/build-bundle.sh` creates `build/bundle/` with all apps (UI, Grin, LSP, plus standalone `lisma-compiler-cli-app` and `simulation-runner-cli-app` distributions) + `run-ui.sh` launcher that wires all script paths via env vars

---

## Gradle Configuration

- **Centralized versions:** `gradle/libs.versions.toml`
- **All dependency/plugin versions** must be defined in `libs.versions.toml` with aliases
- Use `{ version.ref = "..." }` for version references
- No duplicate `group`, `version`, or `repositories` declarations
- `java.sourceCompatibility` and `java.targetCompatibility` must match

## Java Modules

All applications in this repository should use `module-info.java` for proper module system configuration.


## Git Submodules

- `isma-ui/` → https://github.com/SmallBossGames/isma-ui-javafx
- `grin/` → https://github.com/SmallBossGames/grin-javafx

Fresh clones need `git submodule update --init --recursive` before building.
`isma-ui` and `grin` are standalone Gradle projects — build them with their own `./gradlew`, not the root one.

## Build

```bash
./gradlew build          # Build all modules
./gradlew :module:build  # Build specific module
./.ci-cd/build-bundle.sh # Build application distribution bundle (UI + CLIs)
```

## Architecture Rules

### Keep CLI Process Details Out of Business Logic

**Never launch or inspect CLI tool processes outside `external-services/cli/`.** The UI's business logic talks to the tools only through the `SimulationServerFacade` and its DTOs.

- **UI `external-services/cli/`** — owns process launching, stdin/stdout/stderr plumbing, exit-code handling, and the CLI protocols (compiler: source on stdin, `.class` bytes or `row:col:message` diagnostics on stdout; runner: binary exchange format on stdout).
- **UI `external-services/`** — `SimulationServerFacade` maps CLI outcomes to UI DTOs (`CompileResult`, `ValidationResult`, `RunSimulationParams`, `CachedSimulationResult`). No process handles leak into `app/`, `domain/`, or UI components.
- **UI `domain/`** — pure domain models and interfaces. No process/CLI types, no JavaFX types.

This keeps the CLI protocols (arguments, streams, exit codes) confined to the integration layer, so the tools can evolve or be rewritten independently.
