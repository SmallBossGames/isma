# ISMA UI Migration to C# Avalonia

## Overview

This document describes the migration of `isma-ui/` module from Kotlin/JavaFX to C# with Avalonia UI framework.

## Why Avalonia?

| Criteria | Avalonia | WPF | MAUI |
|----------|----------|-----|------|
| Cross-platform | ✅ Win/Mac/Linux | ❌ Windows only | ✅ but mobile-first |
| WPF-like syntax | ✅ Very similar | ✅ | ⚠️ Different |
| Desktop-focused | ✅ | ✅ | ⚠️ Mobile-first |
| Linux support | ✅ Excellent | ❌ | ⚠️ Limited |

## Technology Stack

- **Framework:** Avalonia 11.x
- **.NET:** 10 (latest)
- **gRPC:** grpc-dotnet with Unix socket support
- **DI:** Microsoft.Extensions.DependencyInjection
- **Protobuf:** NSwag or Grpc.Tools

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│           C# Avalonia Client (isma-ui-dotnet)           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐ │
│  │ Text Editor │  │Blueprint Edit│  │ Project Manager │ │
│  └─────────────┘  └─────────────┘  └─────────────────┘ │
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │  gRPC Client (Unix Socket)  →  Server          │   │
│  └─────────────────────────────────────────────────┘   │
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │  Launch grin/gui/app: --result-file --x-axis   │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                 Server (isma-server)                    │
│         Simulation logic, compilation, etc.           │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│              grin/gui/app (Kotlin/JavaFX)              │
│              Results visualization only                │
└─────────────────────────────────────────────────────────┘
```

## Migration Phases

1. **Phase 1:** Setup - Create project structure, add proto files
2. **Phase 2:** External Services - gRPC client, server manager
3. **Phase 3:** Domain Models - Port domain objects
4. **Phase 4:** App Module - Main application, views, viewmodels
5. **Phase 5:** Editors - Text editor, blueprint editor

## Communication Protocol

- **Server connection:** Unix sockets (same as current)
- **GrIn launch:** Command-line arguments via `--result-file`, `--x-axis`, `--charts`

## What Stays in Kotlin

- `grin/gui/app/` - Results visualization (remains JavaFX)
- `grin/gui/concatenation/` - Chart rendering (remains JavaFX)
- `grin/gui/common/` - Shared models (remains Kotlin)

## What Migrates to C#

See detailed mapping in phase documents.

## Files Location

- **Source proto files:** `protobuf-contracts/simulation/`
- **Kotlin source:** `isma-ui/` (all submodules)
- **GrIn launcher:** `grin/gui/app/`
