# UI Components

This subdirectory documents the `app` module and its editor modules (text-editor, blueprint-editor) — the component layer of ISMA-UI.

## Index

| File | Content |
|------|---------|
| [01-architecture](01-architecture.md) | Module structure, build config, DI wiring, component dependency graph, toolkit helpers |
| [02-services](02-services.md) | Service algorithms, data flows, component lifecycle, error propagation, defaults |
| [03-ui-layout](03-ui-layout.md) | ASCII visual layout of the running application, toolbar layout, settings accordion |
| [04-views](04-views.md) | View hierarchy, ViewModel bindings, settings panel structure, dialogs |
| [05-editors](05-editors.md) | Text editor module, Blueprint editor module (concise) |

## Cross-References

| Topic | Document |
|-------|----------|
| Module build config | [`05-build-and-deployment.md`](../05-build-and-deployment.md) |
| Domain models | [`02-domain-layer.md`](../02-domain-layer.md) |
| Server communication | [`03-external-services.md`](../03-external-services.md) |
| Blueprint editor deep spec | [`blueprint-editor/README.md`](../blueprint-editor/README.md) |
| UX specification | [`06-ux-reference.md`](../06-ux-reference.md) |
| Use cases | [`use-cases/`](../use-cases/) |
