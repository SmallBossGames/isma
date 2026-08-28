# Build & Deployment

## Module Build Configuration

All modules use the Kotlin JVM plugin (`alias(libs.plugins.kotlin.jvm)`) and Java module system plugin (`alias(libs.plugins.java.modules)`). The `app` module additionally uses the `application` plugin with `alias(libs.plugins.javafx)`.

### app Module

**File:** `app/build.gradle.kts`

The module applies plugins: `kotlin.jvm`, `kotlin.serialization`, `java.modules`, `javafx`, and `application`. Configuration sets `mainModule` to `isma.ui.app.main`, `mainClass` to `ru.isma.next.app.launcher.IsmaApplication`, and `applicationDefaultJvmArgs` to include `--enable-native-access=javafx.graphics` and `--enable-native-access=io.netty.common`. `JavaExec` tasks are configured with JVM args for native access and system properties pointing to server and Grin scripts. JavaFX version is 25.0.2 with modules `javafx.controls` and `javafx.fxml`. See `app/build.gradle.kts` for the full configuration.

Key configuration:
- JavaFX 25.0.2 with `controls` and `fxml` modules
- JVM args require `--enable-native-access` for JavaFX graphics and Netty native code
- `JavaExec` tasks auto-configure `isma.server.script` and `isma.grin.script` system properties
- Main class: `ru.isma.next.app.launcher.IsmaApplication`

### domain Module

**File:** `domain/build.gradle.kts`

The module applies the Kotlin JVM plugin and Java modules plugin. Its only dependency is `kotlinx-coroutines-core`. See `domain/build.gradle.kts` for the full configuration.

No JavaFX modules. Pure Kotlin module with only kotlinx-coroutines-core.

### external-services Module

**File:** `external-services/build.gradle.kts`

The module sets group `ru.isma.next.ui` and version `1.0.0-SNAPSHOT`. Applies the Kotlin JVM plugin and Java modules plugin. Dependencies include project references to `:isma-ui:grpc`, `:isma-ui:domain`, and `:isma-jvm-lib:exchange-format`, plus gRPC-Netty, gRPC-protobuf, gRPC-stub, protobuf-java, slf4j-api, ktor-client-core, ktor-client-cio, kotlinx-io-core, and Netty transport (with Linux x86_64 epoll classifier). See `external-services/build.gradle.kts` for the full configuration.

No JavaFX modules. Depends on gRPC-Netty, Ktor CIO, and Netty Epoll for Unix Domain Socket support.

### grpc Module

**File:** `grpc/build.gradle.kts`

The module sets group `ru.isma.next.ui` and version `1.0.0-SNAPSHOT`. Applies the Google protobuf plugin and Java modules plugin. Protobuf configuration uses `protoc` from the centralized version, generates gRPC Java stubs, and sources proto files from `../../protobuf-contracts`. Dependencies include gRPC-Netty, Netty transport (with Linux x86_64 epoll classifier), gRPC-stub, gRPC-protobuf, protobuf-java, grpc-java, and Guava. See `grpc/build.gradle.kts` for the full configuration.

Generates Java gRPC stubs from protobuf definitions in `../../protobuf-contracts`. Exports `ru.nstu.isma.contracts.simulation`.

### text-editor Module

**File:** `text-editor/build.gradle.kts`

The module applies the Kotlin JVM plugin, Java modules plugin, and JavaFX plugin. JavaFX version is 25.0.2 with modules `javafx.controls` and `javafx.fxml`. Dependencies include `fxmisc.richtext.core`, `kotlinx-coroutines-core`, and `kotlinx-coroutines-javafx`. See `text-editor/build.gradle.kts` for the full configuration.

JavaFX module using `fxmisc.richtext` for rich text editing.

### blueprint-editor Module

**File:** `blueprint-editor/build.gradle.kts`

The module applies the Kotlin JVM plugin, Kotlin serialization plugin, Java modules plugin, and JavaFX plugin. JavaFX version is 25.0.2 with modules `javafx.controls`, `javafx.fxml`, and `javafx.swing`. Dependencies include `kotlinx-serialization-json`, `kotlinx-coroutines-core`, and `kotlinx-coroutines-javafx`. See `blueprint-editor/build.gradle.kts` for the full configuration.

JavaFX module with kotlinx-serialization for `BlueprintModel` JSON persistence.

### toolkit Module

**File:** `toolkit/build.gradle.kts`

The module applies the Kotlin JVM plugin, Java plugin, Java modules plugin, and JavaFX plugin. JavaFX version is 25.0.2 with module `javafx.controls`. Dependencies include `kotlinx-coroutines-core` and `kotlinx-coroutines-javafx`. See `toolkit/build.gradle.kts` for the full configuration.

Provides shared JavaFX utilities: `PropertiesGrid` (reusable property grid component), `ComboBox` extension (custom cell factory), `ListView` cell factory, `CollectionsExtensions` (coroutine flow extensions for JavaFX collections: `addedAsFlow()`, `changeAsFlow()`), `Properties` helper, `UiThreadExecutor` hierarchy (UI thread execution abstraction), and `BaseViewModel` (abstract base VM with `StateFlow` lifecycle tracking).

**Source structure:**

The toolkit source lives in `toolkit/src/main/kotlin/ru/isma/javafx/extensions/` with subdirectories: `controls/` (PropertiesGrid.kt - reusable property grid, ComboBox.kt - custom ComboBox extensions), `coroutines/` (UiThreadExecutor.kt - UI thread execution interface, JavaFxUiThreadExecutor.kt - JavaFX implementation, TestUiThreadExecutor.kt - test implementation, flow/CollectionsExtensions.kt - ObservableList/Set to Flow bridges), `helpers/` (Properties.kt - property utility helpers), and `viewmodel/` (BaseViewModel.kt - abstract base VM with StateFlow lifecycle tracking).

## Java Module System

Each module declares a `module-info.java` with appropriate `exports`:

| Module | Module Name | Exports |
| --- | --- | --- |
| `app` | `isma.ui.app.main` | `ru.isma.next.app.launcher` |
| `domain` | `isma.ui.domain` | `ru.isma.next.domain.models` |
| `external-services` | `isma.ui.external.services` | `ru.isma.next.external`, `ru.isma.next.external.dtos` |
| `grpc` | `isma.ui.grpc` | `ru.nstu.isma.contracts.v1.simulation_service`, `ru.nstu.isma.contracts.v1.compiler_service` |
| `text-editor` | `isma.ui.editor.text` | `ru.isma.next.editor.text`, `services`, `services.contracts` |
| `blueprint-editor` | `isma.ui.editor.blueprint` | `ru.isma.next.editor.blueprint`, `ru.isma.next.editor.blueprint.constants`, `ru.isma.next.editor.blueprint.controls`, `ru.isma.next.editor.blueprint.models`, `ru.isma.next.editor.blueprint.services`, `ru.isma.next.editor.blueprint.utilities`, `ru.isma.next.editor.blueprint.views` |
| `toolkit` | `isma.ui.toolkit` | `ru.isma.javafx.extensions.controls`, `ru.isma.javafx.extensions.coroutines`, `ru.isma.javafx.extensions.coroutines.flow`, `ru.isma.javafx.extensions.helpers`, `ru.isma.javafx.extensions.viewmodel` |

**Requires notes:**
- `text-editor` requires `javafx.graphics` (not `javafx.controls`)
- `external-services` requires `io.netty.transport.unix.common`, `io.netty.common`, `io.netty.buffer`, `io.netty.codec`

**Opens declarations (app module):**
The `app` module uses `opens` (not `exports`) for packages that need reflection access:
- `opens ru.isma.next.app.models.preferences to kotlinx.serialization` — required for JSON serialization of `WindowPreferencesModel` and `DefaultFilesPreferencesModel`
- `opens ru.isma.next.app.models to javafx.base` — required for JavaFX property binding reflection

## Build Commands

- `./gradlew :isma-ui:app:build` — Build all isma-ui modules
- `./gradlew :isma-ui:domain:build` — Build domain module
- `./gradlew :isma-ui:external-services:build` — Build external-services module
- `./gradlew :isma-ui:grpc:build` — Build grpc module
- `./gradlew :isma-ui:text-editor:build` — Build text-editor module
- `./gradlew :isma-ui:blueprint-editor:build` — Build blueprint-editor module
- `./gradlew :isma-ui:toolkit:build` — Build toolkit module
- `./gradlew build` — Build entire project (includes isma-ui)
- `./.ci-cd/build-bundle.sh` — Build bundle (UI + server)

## Dependency Resolution

All dependency versions are centralized in `gradle/libs.versions.toml` with aliases. The file defines `[versions]` sections for javafx (25.0.2), koin (4.x.x), and grpc (1.x.x), and `[libraries]` sections mapping aliases to module coordinates with version references. The `app` module imports `libs.koin.core` for DI and `libs.kotlinx.coroutines.*` for coroutine-based concurrency. No TornadoFX dependency. See `gradle/libs.versions.toml` for the full configuration.

## Running

### Via Gradle

The `JavaExec` task in `app/build.gradle.kts` auto-configures system properties `isma.server.script` and `isma.grin.script` pointing to the bundle paths. Run with: `./gradlew :isma-ui:app:run`. See `app/build.gradle.kts` for the full configuration.

### Via IDE

Set VM options in run configuration:
- `--enable-native-access=javafx.graphics`
- `--enable-native-access=io.netty.common`
- `-Disma.server.script=/path/to/isma-server-script`
- `-Disma.grin.script=/path/to/grin-script`

### Via Bundle

After `./.ci-cd/build-bundle.sh`, the bundle at `build/bundle/` contains both `isma-ui` and `isma-server` with a launcher script that resolves paths automatically.

## Server Script Resolution Priority

1. Environment variable `ISMA_SERVER_SCRIPT`
2. System property `isma.server.script`
3. Throws `IllegalStateException` if neither is set

Same pattern for Grin: `ISMA_GRIN_SCRIPT` → `isma.grin.script` → exception.

## Cache Directory

Simulation results are cached to `<java.io.tmpdir>/isma-simulation-cache/simulation_<id>.bin`. The directory is created automatically on first download if it doesn't exist.

## Version Alignment

All modules use `javafx.version = 25.0.2` and the same Kotlin version. The `java.sourceCompatibility` and `java.targetCompatibility` must match across all modules.
