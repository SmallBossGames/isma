---
name: tech-analysis
description: Perform technical analysis and decompose features or issues into structured, atomic tasks with plan documents and task documents following ISMA project conventions
license: MIT
compatibility: opencode
metadata:
  audience: developers
  workflow: technical-analysis
  tooling: markdown
---

## What I do

Analyze code and decompose work into structured technical documentation: plan documents (problem, scope, dependency graph, phases) and task documents (problem, expectations, implementation, acceptance criteria).

## Document Formats

### Plan Document (`plan.md` or `execution-plan.md`)

```markdown
# <Initiative Name>

**Scope:** `<module>` / `<area>`
**Effort:** `<Low / Medium / High>`
**Risk:** `<Low / Medium / High>`
**Dependencies:** `<related initiatives or blocking work>`

## Problem

<One paragraph: what is wrong, why it needs fixing, impact on the codebase.>

## Scope Analysis

### Files to Modify

| File | Change |
|------|--------|
| `path/to/File.kt` | `<what changes>` |
| `path/to/Module.kt` | `<what changes>` |

### Files to Delete

| File | Reason |
|------|--------|
| `path/to/File.kt` | `<reason>` |

### Files to Update (callers)

| File | Change |
|------|--------|
| `path/to/Caller.kt` | `<what changes>` |

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

## Verification

```bash
./gradlew :<module>:build
grep -r "<pattern>" <path>
```

```

### Task Document (`task-NN-<slug>.md`)

```markdown
# Task NN: <Title>

**Scope:** `<file path or module>`
**Effort:** `<Low / Medium / High>`
**Risk:** `<Very low / Low / Medium / High>`

## Problem Description

<What is wrong and why it needs changing. One short paragraph.>

## Expectations

<What the result should look like after this task is complete. What exists, what doesn't.>

## Implementation

### Key Design Decisions

- `<decision 1>`
- `<decision 2>`

### Implementation Steps

1. `<step 1>`
2. `<step 2>`

```kotlin
// Key code snippet — show signatures, not full implementations
interface ITarget {
    fun method(param: Type): ReturnType
}
```

### Before / After

**Before:**
```kotlin
// Show the existing code that needs to change
```

**After:**
```kotlin
// Show the target code
```

## Verification

- [ ] `./gradlew :<module>:compileKotlin` succeeds
- [ ] `grep "<pattern>"` returns expected results

## Acceptance Criteria

- [ ] `<criterion 1>`
- [ ] `<criterion 2>`
- [ ] `<criterion 3>`

```

## Naming Conventions

- Directory: `docs/todo/<initiative-name>/`
- Plan: `plan.md`
- Tasks: `task-NN-<slug>.md` (zero-padded, kebab-case)
- Example: `docs/todo/grin-tornadofx-removal/task-04-controllers-to-plain-classes.md`

## Rules

### Plan Documents
- Scope analysis uses tables (files to modify/delete/update)
- Dependency graph shows parallelization opportunities
- Execution phases group tasks by build compatibility
- Risk assessment per phase with mitigations
- Verification includes exact build/grep commands

### Task Documents
- Each task is atomic — independently completable and compilable
- Problem description: what's wrong, why it matters
- Expectations: what the result looks like
- Implementation: key design decisions + implementation patterns (not full code)
- Acceptance criteria: `[ ]` checklist, verifiable
- Show real code snippets (not pseudo-code) — include imports, full signatures
- Include verification commands (grep, build)

### General
- Follow AGENTS.md architecture rules (separate domain from gRPC, no JavaFX in services, etc.)
- Use the `write-docs` skill conventions for diagrams and tables when appropriate
- Reference concrete file paths
- Each task should leave the codebase in a compilable state

## Flow

### When performing technical analysis

1. **Explore the codebase.** Use the explore subagent to understand the area in scope. Read build files, DI modules, key interfaces, and callers.

2. **Identify the problem.** What is wrong? What is the impact? What needs to change?

3. **Decompose into tasks.** Break work into atomic steps. Each step:
   - Is independently completable
   - Leaves the codebase compilable
   - Has clear acceptance criteria
   - Shows before/after code

4. **Build dependency graph.** Which tasks are independent? Which are sequential? Group into phases.

5. **Write the plan document.** Create `docs/todo/<name>/plan.md` (or `execution-plan.md`).

6. **Write task documents.** Create `docs/todo/<name>/task-NN-<slug>.md` for each task.

7. **Review.** Verify:
   - [ ] Plan has scope analysis tables
   - [ ] Dependency graph shows parallelization
   - [ ] Each task is atomic and compilable
   - [ ] Each task has problem, expectations, implementation, acceptance criteria
   - [ ] Verification commands are exact and runnable
   - [ ] Risk assessment included

```
