---
name: update-dependencies
description: Update Gradle dependency versions in libs.versions.toml using ben-manes-versions plugin without downgrades
license: MIT
compatibility: opencode
metadata:
  audience: developers
  workflow: gradle
  tooling: maven central, gradle plugin
---

## What I do
- Use `ben-manes-versions` plugin to detect outdated dependencies
- Update `gradle/libs.versions.toml` with only stable upgrades (never downgrades)
- Filter out alpha/beta/RC versions unless currently using one
- Verify build compatibility after updates using `./gradlew build -x test`
- Document version changes in git diff before committing

## Prerequisites

The project must have `ben-manes-versions` plugin configured globally:

```kotlin
// gradle/libs.versions.toml
[plugins]
ben-manes-versions = { id = "com.github.ben-manes.versions", version = "0.53.0" }

// build.gradle.kts (root)
plugins {
    alias(libs.plugins.ben-manes-versions) apply false
}

subprojects {
    apply(plugin = "com.github.ben-manes.versions")
}
```

## How I work

### Step 1: Check for outdated dependencies
```bash
./gradlew dependencyUpdates --no-parallel
```

The plugin generates a report at `build/dependencyUpdates/report.txt`.

### Step 2: Parse and filter upgrade candidates

Read the report and identify safe upgrades:

| Criteria | Action |
|----------|--------|
| Has newer milestone/stable version | ✅ Include |
| Has newer alpha/beta/RC only | ❌ Skip (unless current is RC/beta) |
| Major version jump (e.g., Netty 4→5) | ❌ Skip |
| Gradle itself | ❌ Skip |

### Step 3: Update libs.versions.toml

Only update:
- `[versions]` section entries
- Library `version` fields (NOT `version.ref`)

```toml
# Example updates
[versions]
kotlin-plugin = "2.3.20"           # was "2.3.20-RC3"
kotlinx-serialization-json = "1.10.0"  # was "1.10.0-RC"

[libraries]
kotlin-reflect = { module = "org.jetbrains.kotlin:kotlin-reflect", version.ref = "kotlin-plugin" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization-json" }
```

### Step 4: Verify changes
```bash
git diff gradle/libs.versions.toml
```

Critical checks before committing:
- ✅ No duplicate entries (TOML syntax error)
- ✅ All existing dependencies preserved
- ✅ Version references still valid (`version.ref = "xxx"`)
- ✅ Build compatibility maintained

### Step 5: Test the build
```bash
./gradlew build --no-daemon -x test
```

## Rules I follow

1. **Never downgrade** - only upgrade when a newer stable version exists
2. **Prefer stable releases** over RC/beta versions
3. **Skip major version jumps** (e.g., 4.x → 5.x)
4. **Skip Gradle itself** - update separately if needed
5. **Preserve all dependencies** - never remove existing entries
6. **Maintain version references** - update `[versions]` section when needed
7. **Group related artifacts** - upgrade all Apache POI, Netty, gRPC together

## Common patterns

### Simple version upgrade
```toml
# Before
logback-classic = { module = "ch.qos.logback:logback-classic", version = "1.5.23" }

# After  
logback-classic = { module = "ch.qos.logback:logback-classic", version = "1.5.32" }
```

### Version reference upgrade
```toml
# Update in [versions] section
[versions]
kotlin-plugin = "2.3.20"

# Libraries using it auto-update
kotlin-reflect = { module = "org.jetbrains.kotlin:kotlin-reflect", version.ref = "kotlin-plugin" }
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Unresolved reference: {package}" | Missing dependency in build files, keep original version |
| "Invalid TOML" | Duplicate entries or syntax error - check git diff |
| Test failures | May be pre-existing; run `-x test` flag to verify |
| Build errors after upgrade | Roll back and skip that dependency |

## Example workflow

```bash
# 1. Run dependency check
./gradlew dependencyUpdates --no-parallel

# 2. Review report
cat build/dependencyUpdates/report.txt

# 3. Identify safe upgrades from the "milestone" section
# Filter out: alpha/beta/RC, major version jumps, Gradle itself

# 4. Edit libs.versions.toml
# Update only the version numbers

# 5. Review changes
git diff gradle/libs.versions.toml

# 6. Test build
./gradlew build --no-daemon -x test

# 7. Commit if successful
git add gradle/libs.versions.toml
git commit -m "Update dependency versions (no downgrades)"
```

## Version compatibility notes

### JUnit Ecosystem
- `junit:junit` (v4) and `junit-jupiter-*` (v5) can coexist
- Keep versions compatible with existing test code
- Don't upgrade JUnit 4 unless explicitly needed

### Kotlin Libraries  
- Prefer stable releases over RC/beta
- RC → stable is a valid upgrade
- Check compatibility with kotlin-plugin version

### Apache Commons/Poi
- Multiple artifacts share same version reference
- Upgrade all related artifacts together (poi, poi-ooxml)

### Netty
- Major version jumps (4.x → 5.x) require code changes
- Always skip unless explicitly requested

### gRPC
- All grpc-* artifacts share same version
- Upgrade all together

## Skipped update categories

Always skip these unless user explicitly requests:

| Category | Example | Reason |
|----------|---------|--------|
| Alpha versions | netty 5.0.0.Alpha2 | Unstable |
| Beta versions | slf4j 2.1.0-alpha1 | Unstable |
| RC versions | junit-jupiter 6.1.0-M1 | Pre-release |
| Gradle | 9.4.0 → 9.4.1 | Separate process |
| Major jumps | netty 4.2 → 5.0 | Breaking changes |

## Verification checklist

- [ ] All original dependencies preserved
- [ ] No duplicate entries in TOML file
- [ ] Version references still valid
- [ ] Build succeeds without tests (`-x test`)
- [ ] Test failures are pre-existing (not caused by updates)
- [ ] Git diff shows only version changes
- [ ] No sensitive data or secrets affected

## References

- ben-manes-versions plugin: https://github.com/ben-manes/gradle-versions-plugin
- Maven Central: https://repo1.maven.org/maven2/
- Gradle Version Catalog: https://docs.gradle.org/current/userguide/platforms.html
- TOML Syntax: https://toml.io/

## Last updated

March 23, 2026
