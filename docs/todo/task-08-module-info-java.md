# Task: Add module-info.java

## Problem

The project AGENTS.md states: "All applications in this repository should use `module-info.java` for proper module system configuration." The `blueprint-editor` module currently lacks a `module-info.java` file, which means it relies on the classpath variant of the Java Module System.

## Requirements

1. Create `module-info.java` for the blueprint-editor module
2. Declare all required JavaFX and third-party modules
3. Export all public packages
4. Build must pass with the module descriptor

## Implementation

### Create `src/main/java/module-info.java`

```java
module isma.ui.editor.blueprint {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlinx.serialization.json;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;

    exports ru.isma.next.editor.blueprint;
    exports ru.isma.next.editor.blueprint.constants;
    exports ru.isma.next.editor.blueprint.controls;
    exports ru.isma.next.editor.blueprint.models;
    exports ru.isma.next.editor.blueprint.services;
    exports ru.isma.next.editor.blueprint.utilities;
}
```

### Verify package structure

Ensure all packages listed in the exports exist:

```bash
find isma-ui/blueprint-editor/src/main/kotlin -type d | sort
```

Expected output:
```
.../ru/isma/next/editor/blueprint
.../ru/isma/next/editor/blueprint/constants
.../ru/isma/next/editor/blueprint/controls
.../ru/isma/next/editor/blueprint/models
.../ru/isma/next/editor/blueprint/services
.../ru/isma/next/editor/blueprint/utilities
```

## Acceptance Criteria

- [ ] `src/main/java/module-info.java` exists
- [ ] All six packages are exported
- [ ] All required JavaFX and third-party modules are declared
- [ ] `./gradlew :isma-ui:blueprint-editor:compileJava` passes
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] No `module-info.java` compilation errors about missing requires
