# isma-lsp

Standalone LISMA language server. A CLI process speaking the [Language Server Protocol](https://microsoft.github.io/language-server-protocol/) (JSON-RPC 2.0, `Content-Length` framing) over stdio, providing syntax highlighting for LISMA source code to UI clients.

## Quick Navigation

| Document | Contents |
|----------|----------|
| [01-overview.md](01-overview.md) | Architecture, module structure, token model, dependency graph |
| [02-protocol.md](02-protocol.md) | LSP protocol details: handshake, document sync, semantic token wire format, smoke test |

## Quick Start

```bash
./gradlew :isma-compiler:lisma-lsp:build            # compile + tests (17 tests)
./gradlew :isma-compiler:lisma-lsp:installDist      # -> isma-compiler/lisma-lsp/build/install/isma-lsp/bin/isma-lsp
python3 isma-compiler/lisma-lsp/smoke.py isma-compiler/lisma-lsp/build/install/isma-lsp/bin/isma-lsp   # stdio smoke test
```

## Key Files

| File | Purpose |
|------|---------|
| `isma-compiler/lisma-lsp/src/main/kotlin/ru/nstu/isma/lsp/Main.kt` | Entry point — `LspServer(System.in, System.out, LismaLanguageService()).run()` |
| `isma-compiler/lisma-lsp/src/main/kotlin/ru/nstu/isma/lsp/protocol/LspServer.kt` | JSON-RPC dispatch loop, document store, request handlers |
| `isma-compiler/lisma-lsp/src/main/kotlin/ru/nstu/isma/lsp/protocol/LspMessages.kt` | `Content-Length` framing: read/write LSP messages |
| `isma-compiler/lisma-lsp/src/main/kotlin/ru/nstu/isma/lsp/lsp/LismaLanguageService.kt` | LISMA language features — semantic tokens via ANTLR4 lexer |
| `isma-compiler/lisma-lsp/src/main/kotlin/ru/nstu/isma/lsp/lsp/SemanticTokens.kt` | Token types, legend, LSP relative-format encoder/decoder |
| `isma-compiler/lisma-lsp/smoke.py` | End-to-end stdio smoke test (initialize → tokens → didChange → shutdown) |
| `isma-compiler/lisma-lsp/build.gradle.kts` | Build config (application plugin, JPMS module `isma.lsp`) |

## Who Uses It

Both UIs launch `isma-lsp` as a background process and talk to it over stdio:

- **JavaFX UI** (`isma-ui` submodule) — `LspProcessManager` + `LspClient` in `external-services/src/main/kotlin/ru/isma/next/external/lsp/`
- **Avalonia UI** (`isma-ui-avalonia` repo) — `LspProcessManager` + `LspClient` in `src/ISMA.ExternalServices/Lsp/`

The script path is resolved from the `ISMA_LSP_SCRIPT` environment variable or the `isma.lsp.script` config key (JavaFX: system property; Avalonia: `AppContext` data / `Lsp:ScriptPath` in `appsettings.json`).
