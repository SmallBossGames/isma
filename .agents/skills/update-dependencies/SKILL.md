---
name: update-dependencies
description: Update Gradle dependency versions in libs.versions.toml via Maven Central without downgrades
license: MIT
compatibility: opencode
metadata:
  audience: developers
  workflow: gradle
  tooling: maven central
---

## What I do
- Query Maven Central for latest stable versions of Gradle dependencies
- Update `gradle/libs.versions.toml` with only upgrades (never downgrades)
- Verify build compatibility after updates using `./gradlew build -x test`
- Document version changes in git diff before committing

## When to use me
Use this when you need to refresh dependency versions in a Gradle Kotlin DSL project while maintaining stability and avoiding breaking changes.

## How I work

### Step 1: Read current state
```bash
cat gradle/libs.versions.toml
```

### Step 2: Query Maven Central for latest versions
```bash
# Using Maven Search API (recommended)
curl -sL "https://search.maven.org/solrsearch/select?q=a:{artifact}&core=gav&rows=100&wt=json" \
  | python3 -c "import json,sys; d=json.load(sys.stdin); 
    docs = sorted(d['response']['docs'], 
      key=lambda x: [int(p) if p.isdigit() else 0 for p in x['v'].split('.')], reverse=True)"

# Or using Maven Central direct listing
curl -sL "https://repo1.maven.org/maven2/{group}/{artifact}/" \
  | grep '<a' | grep 'href=' | tail -5
```

### Step 3: Compare and filter for upgrades only
Create a comparison table before editing:

| Package | Current Version | Latest Version | Upgrade? | Reason |
|---------|-----------------|----------------|----------|--------|
| antlr4-runtime | 4.11.1 | 4.13.0 | ✓ | Newer version available |
| junit:junit | 4.13.2 | 4.15.1 | ✗ | Avoid downgrade issues |

### Step 4: Update versions safely
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
2. **Prefer `-jre` suffixes** over `-android` for consistency
3. **Prefer stable releases** over RC/beta versions when available
4. **Check Maven Central directly** to verify package existence and correct coordinates
5. **Preserve all dependencies** - never remove existing entries
6. **Maintain version references** - update `[versions]` section too if needed

## Common patterns

### Simple version upgrade
```toml
# Before
antlr4-runtime = { module = "org.antlr:antlr4-runtime", version = "4.11.1" }

# After  
antlr4-runtime = { module = "org.antlr:antlr4-runtime", version = "4.13.0" }
```

### Version reference (no change needed)
```toml
ikonli-javafx = { module = "org.kordamp.ikonli:ikonli-javafx", version.ref = "ikonli" }
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Unresolved reference: {package}" | Missing dependency in build files, keep original version |
| "Invalid TOML" | Duplicate entries or syntax error - check git diff |
| Test failures | May be pre-existing; run `-x test` flag to verify |

## Example workflow

```bash
# 1. Backup current state
git stash

# 2. Query all packages for latest versions
python3 << 'EOF'
import urllib.request, json, re

def get_latest(group, artifact):
    url = f"https://search.maven.org/solrsearch/select?q=a:{artifact}&core=gav&rows=100&wt=json"
    data = json.loads(urllib.request.urlopen(url).read().decode())
    docs = sorted(data['response']['docs'], 
        key=lambda x: [int(p) if p.isdigit() else 0 for p in x['v'].split('.')], reverse=True)
    return docs[0]['v'] if docs else None

packages = {
    "antlr4-runtime": ("org.antlr", "antlr4-runtime"),
    "guava": ("com.google.guava", "guava"),
}

for name, (group, artifact) in packages.items():
    latest = get_latest(group, artifact)
    print(f"{name}: {latest}")
EOF

# 3. Manually edit and review changes
vim gradle/libs.versions.toml
git diff gradle/libs.versions.toml

# 4. Test build
./gradlew build --no-daemon -x test

# 5. Commit if successful
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
- Check compatibility with kotlin-plugin version

### Apache Commons/Poi
- Multiple artifacts share same versions
- Upgrade all related artifacts together

## Verification checklist

- [ ] All original dependencies preserved
- [ ] No duplicate entries in TOML file
- [ ] Version references still valid
- [ ] Build succeeds without tests (`-x test`)
- [ ] Test failures are pre-existing (not caused by updates)
- [ ] Git diff shows only version changes
- [ ] No sensitive data or secrets affected

## References

- Maven Central: https://repo1.maven.org/maven2/
- Maven Search API: https://search.maven.org/
- Gradle Version Catalog: https://docs.gradle.org/current/userguide/platforms.html
- TOML Syntax: https://toml.io/

## Last updated

March 14, 2026
