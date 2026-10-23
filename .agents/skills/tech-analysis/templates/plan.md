# <Initiative Name>

**Scope:** `<module / area>`
**Effort:** `<Low / Medium / High>`
**Risk:** `<Low / Medium / High>`
**Dependencies:** `<related initiatives or blocking work>`

## Problem

<One paragraph: what is wrong, why it needs fixing, impact on the codebase.>

## Scope Analysis

### Files to Modify

| File | Change |
|------|--------|
| `path/to/File` | `<what changes>` |
| `path/to/Module` | `<what changes>` |

### Files to Delete

| File | Reason |
|------|--------|
| `path/to/File` | `<reason>` |

### Files to Update (callers)

| File | Change |
|------|--------|
| `path/to/Caller` | `<what changes>` |

## Dependency Graph

```
Phase 0 (parallel):
  Task 1 ──┐
  Task 2 ──┼─→ Phase 1
  Task 3 ──┘

Phase 1 (sequential):
  Task 4 → Task 5 → Task 6
```

## Execution Phases

### Phase 0: <phase name>
- Task 1 — `<description>` — `<effort>`
- Task 2 — `<description>` — `<effort>`
- Task 3 — `<description>` — `<effort>`

### Phase 1: <phase name>
- Task 4 — `<description>` — `<effort>`
- Task 5 — `<description>` — `<effort>`

## Recommended Commit Sequence

1. `git commit -m "<task 1 descriptive message>"`
2. `git commit -m "<task 2 descriptive message>"`
3. ...

## Parallelization

- Tasks 1, 2, 3 can run in parallel (independent)
- Task 4 depends on Tasks 1–3

## Risk Assessment

| Phase | Risk | Mitigation |
|-------|------|------------|
| Phase 0 | Low | Build fails immediately if any ... |
| Phase 1 | Medium | ... |

## Tests

<What tests are needed at the initiative level. Which modules need new tests, existing tests that need updating.>

## Verification

```bash
# Build command
<build command for module>

# Test command
<test command>

# Pattern checks
grep -r "<pattern>" <path>
```
