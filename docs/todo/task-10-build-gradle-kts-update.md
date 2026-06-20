# Task: Update build.gradle.kts with Test Dependencies

## Problem

The current `build.gradle.kts` has no test dependencies, no test task configuration, and no Java module descriptor support. This prevents running tests and doesn't follow project conventions.

## Requirements

1. Add JUnit 5 test dependencies
2. Configure the `test` task to use JUnit Platform
3. Enable headless mode for JavaFX-compatible tests
4. Optionally: add a code quality plugin (ktlint or detekt)

## Implementation

### Update `build.gradle.kts`

**Before:**
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

**After:**
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

    testImplementation(libs.kotlin.test)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-Djava.awt.headless=true")
}
```

### Check if `libs.versions.toml` already has JUnit

Look at `gradle/libs.versions.toml` for existing JUnit entries:

```toml
[versions]
junit = "5.10.0"

[libraries]
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit" }
```

If present, use `libs.junit.jupiter` instead of the hardcoded coordinate.

### Optional: Add ktlint for code quality

If the project uses ktlint at the root level, no additional config is needed. Check:
```bash
grep -r "ktlint" gradle/
```

If ktlint exists at project level, this module inherits it automatically.

## Acceptance Criteria

- [ ] `build.gradle.kts` includes `testImplementation` for JUnit 5
- [ ] `tasks.test { useJUnitPlatform() }` is configured
- [ ] `jvmArgs("-Djava.awt.headless=true")` is set on the test task
- [ ] `./gradlew :isma-ui:blueprint-editor:test` runs successfully (even with 0 tests)
- [ ] No version conflicts with root-level `libs.versions.toml`
