# Build & Deployment

## Module Build Configuration

All modules use the Kotlin JVM plugin (`alias(libs.plugins.kotlin.jvm)`) and Java module system plugin (`alias(libs.plugins.java.modules)`). The `app` module additionally uses the `application` plugin with `alias(libs.plugins.javafx)`.

### app Module

**File:** `app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
    application
}

application {
    mainModule.set("isma.ui.app.main")
    mainClass.set("ru.isma.next.app.launcher.IsmaApplication")
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics",
        "--enable-native-access=io.netty.common",
    )
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs(
        "--enable-native-access=javafx.graphics",
        "--enable-native-access=io.netty.common",
        "-Disma.server.script=$rootDir/build/bundle/isma-server-app/bin/app",
        "-Disma.grin.script=$rootDir/build/bundle/grin-app/bin/app"
    )
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}
```

Key configuration:
- JavaFX 25.0.2 with `controls` and `fxml` modules
- JVM args require `--enable-native-access` for JavaFX graphics and Netty native code
- `JavaExec` tasks auto-configure `isma.server.script` and `isma.grin.script` system properties
- Main class: `ru.isma.next.app.launcher.IsmaApplication`

### domain Module

**File:** `domain/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
```

No JavaFX modules. Pure Kotlin module with only kotlinx-coroutines-core.

### external-services Module

**File:** `external-services/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-ui:grpc"))
    implementation(project(":isma-ui:domain"))
    implementation(project(":isma-jvm-lib:exchange-format"))

    implementation(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.protobuf.java)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.io.core)

    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact { classifier = "linux-x86_64" }
    }
}
```

No JavaFX modules. Depends on gRPC-Netty, Ktor CIO, and Netty Epoll for Unix Domain Socket support.

### grpc Module

**File:** `grpc/build.gradle.kts`

```kotlin
import com.google.protobuf.gradle.*

plugins {
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.java.modules)
}

group = "ru.isma.next.ui"
version = "1.0.0-SNAPSHOT"

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.protobuf.java.get().version}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.grpc.java.get().version}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins { id("grpc") {} }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("../../protobuf-contracts")
        }
    }
}

dependencies {
    implementation(libs.grpc.netty)
    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact {
            classifier = "linux-x86_64"
        }
    }
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)
    implementation(libs.com.google.guava)
}
```

Generates Java gRPC stubs from protobuf definitions in `../../protobuf-contracts`. Exports `ru.nstu.isma.contracts.simulation`.

### text-editor Module

**File:** `text-editor/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(libs.fxmisc.richtext.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
}
```

JavaFX module using `fxmisc.richtext` for rich text editing.

### blueprint-editor Module

**File:** `blueprint-editor/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
}
```

JavaFX module with kotlinx-serialization for `BlueprintModel` JSON persistence.

### toolkit Module

**File:** `toolkit/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    java
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
}
```

Provides shared JavaFX utilities: `PropertiesGrid`, `ComboBox` extension, `ListView` cell factory, coroutine flow extensions for JavaFX collections (`addedAsFlow()`, `changeAsFlow()`).

## Java Module System

Each module declares a `module-info.java` with appropriate `exports`:

| Module | Module Name | Exports |
| --- | --- | --- |
| `app` | `isma.ui.app.main` | `ru.isma.next.app.launcher` |
| `domain` | `isma.ui.domain` | `ru.isma.next.domain.models` |
| `external-services` | `isma.ui.external.services` | `ru.isma.next.external` |
| `grpc` | `isma.ui.grpc` | `ru.nstu.isma.contracts.simulation` |
| `text-editor` | `isma.ui.editor.text` | `ru.isma.next.editor.text`, `services`, `services.contracts` |
| `blueprint-editor` | `isma.ui.editor.blueprint` | `ru.isma.next.editor.blueprint`, `services`, `models` |
| `toolkit` | `isma.ui.toolkit` | `ru.isma.javafx.extensions.controls`, `coroutines.flow`, `helpers` |

**Requires notes:**
- `text-editor` requires `javafx.graphics` (not `javafx.controls`)
- `external-services` requires `io.netty.transport.unix.common`, `io.netty.common`, `io.netty.buffer`, `io.netty.codec`

## Build Commands

```bash
# Build all isma-ui modules
./gradlew :isma-ui:app:build

# Build specific module
./gradlew :isma-ui:domain:build
./gradlew :isma-ui:external-services:build
./gradlew :isma-ui:grpc:build
./gradlew :isma-ui:text-editor:build
./gradlew :isma-ui:blueprint-editor:build
./gradlew :isma-ui:toolkit:build

# Build entire project (includes isma-ui)
./gradlew build

# Build bundle (UI + server)
./.ci-cd/build-bundle.sh
```

## Dependency Resolution

All dependency versions are centralized in `gradle/libs.versions.toml` with aliases:

```toml
[versions]
javafx = "25.0.2"
koin = "3.x.x"
tornadofx = "1.7.x"
grpc = "1.x.x"

[libraries]
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
tornadofx-core = { module = "com.github.jamesmortensen.kotlin-tornadofx:tornadofx", version.ref = "tornadofx" }
grpc-netty = { module = "io.grpc:grpc-netty", version.ref = "grpc" }
```

The `app` module imports `libs.tornadofx.core` and `libs.koin.core` for DI and UI framework support.

## Running

### Via Gradle

The `JavaExec` task in `app/build.gradle.kts` auto-configures system properties:

```kotlin
tasks.withType<JavaExec>().configureEach {
    jvmArgs(
        "-Disma.server.script=$rootDir/build/bundle/isma-server-app/bin/app",
        "-Disma.grin.script=$rootDir/build/bundle/grin-app/bin/app"
    )
}
```

Run with:
```bash
./gradlew :isma-ui:app:run
```

### Via IDE

Set VM options in run configuration:
```
--enable-native-access=javafx.graphics
--enable-native-access=io.netty.common
-Disma.server.script=/path/to/isma-server-script
-Disma.grin.script=/path/to/grin-script
```

### Via Bundle

After `./.ci-cd/build-bundle.sh`, the bundle at `build/bundle/` contains both `isma-ui` and `isma-server` with a launcher script that resolves paths automatically.

## Server Script Resolution Priority

1. Environment variable `ISMA_SERVER_SCRIPT`
2. System property `isma.server.script`
3. Throws `IllegalStateException` if neither is set

Same pattern for Grin: `ISMA_GRIN_SCRIPT` → `isma.grin.script` → exception.

## Cache Directory

Simulation results are cached to:
```
<java.io.tmpdir>/isma-simulation-cache/simulation_<id>.bin
```

The directory is created automatically on first download if it doesn't exist.

## Version Alignment

All modules use `javafx.version = 25.0.2` and the same Kotlin version. The `java.sourceCompatibility` and `java.targetCompatibility` must match across all modules.
