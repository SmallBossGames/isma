# Blueprint Editor Refactoring — Execution Plan

## Dependency Graph

```
Task 1: Centralize Constants
    ↓
    ├─→ Task 2: EditorMode Sealed Class
    │       ↓
    │       ├─→ Task 3: Click Disambiguator ──→ Task 4: Arrow Geometry Extraction
    │       │                                           ↓
    │       │                                       Task 9: Unit Tests
    │       │                                           ↑
    │       ↓                                           │
    │   Task 5: Canvas ViewModel ───────────────────────┘
    │       ↓
    │       ├─→ Task 6: VM/View Split
    │       │       ↓
    │       │   Task 7: Main/Init Guards (also works without Task 6)
    │       │
    │       └─→ Task 9: Unit Tests (needs Task 2, 4, 5)
    ↓
Task 8: module-info.java (independent)
    ↓
Task 10: build.gradle.kts (independent, but enables Task 9)
```

## Execution Phases

### Phase 0 — Foundation (no dependencies, can run in parallel)

| Task | File | Effort | Why First |
|------|------|--------|-----------|
| 1 | `task-01-centralize-constants.md` | Low | All other tasks reference these constants |
| 8 | `task-08-module-info-java.md` | Low | Project convention, no code changes |
| 10 | `task-10-build-gradle-kts-update.md` | Low | Enables test task for Phase 2 |

**Run in any order.** Each is a small, isolated change. Commit separately.

---

### Phase 1 — Improve internal structure (depends on Phase 0)

| Task | File | Effort | Depends On |
|------|------|--------|------------|
| 2 | `task-02-editor-mode-sealed-class.md` | Low | 1 |
| 3 | `task-03-click-disambiguator.md` | Medium | 1 |
| 4 | `task-04-arrow-geometry-extraction.md` | Medium | 1 |

**Run 2, 3, 4 in any order** — they touch different files. All depend on Task 1 (constants file must exist first).

---

### Phase 2 — Architectural changes (depends on Phase 1)

| Task | File | Effort | Depends On |
|------|------|--------|------------|
| 5 | `task-05-canvas-viewmodel.md` | Medium | 2, 3, 4 |
| 9 | `task-09-unit-tests.md` | Medium | 2, 4 |

**Run 5 and 9 in parallel.** Task 5 refactors the data layer. Task 9 adds tests for the new and existing logic. Both need the EditorMode sealed class and arrow geometry extraction to be in place.

---

### Phase 3 — Major refactor (depends on Phase 2)

| Task | File | Effort | Depends On |
|------|------|--------|------------|
| 6 | `task-06-vm-view-split.md` | High | 2, 3, 4, 5 |

**This is the largest task.** It requires the CanvasViewModel (Task 5) to exist, because the ViewModel needs it. It also benefits from Tasks 2-4 being done (cleaner ViewModel code).

---

### Phase 4 — Bug fix and polish (depends on Phase 3)

| Task | File | Effort | Depends On |
|------|------|--------|------------|
| 7 | `task-07-main-init-removal-guards.md` | Low | 5 or 6 |

Task 7 can be done at any point after Task 5 (CanvasViewModel) exists, since the guard goes in the remove logic. It can also be done before Task 6 by modifying the monolithic class directly. Recommended to do it after Task 6 so the guard lives in the ViewModel where it belongs.

---

## Recommended Commit Sequence

```
1. task-01-centralize-constants      — "Extract magic numbers into BlueprintEditorConstants"
2. task-08-module-info-java          — "Add module-info.java for blueprint-editor"
3. task-10-build-gradle-kts-update   — "Add JUnit 5 test dependencies"
4. task-02-editor-mode-sealed-class  — "Replace boolean flags with EditorMode sealed class"
5. task-03-click-disambiguator       — "Extract reusable ClickDisambiguator"
6. task-04-arrow-geometry-extraction — "Extract arrow geometry to pure function + tests"
7. task-05-canvas-viewmodel          — "Introduce CanvasViewModel to decouple models from UI"
8. task-09-unit-tests                — "Add unit tests for NameChangingMonitor, serialization, etc."
9. task-06-vm-view-split             — "Split IsmaBlueprintEditor into ViewModel and View"
10. task-07-main-init-removal-guards — "Prevent removal of Main and Init states"
```

Each commit should pass `./gradlew :isma-ui:blueprint-editor:build` before pushing.

---

## Parallelization Opportunities

### Phase 0 — All three tasks can be done simultaneously:
- Task 1 creates constants (no test/config changes)
- Task 8 creates module-info.java (no code changes)
- Task 10 updates build.gradle.kts (no code changes)

### Phase 1 — All three tasks can be done simultaneously:
- Task 2 modifies `IsmaBlueprintEditor.kt` mode properties
- Task 3 creates `ClickDisambiguator.kt` and modifies `StateBox.kt`, `LoopTransactionArrow.kt`
- Task 4 creates `ArrowGeometry.kt` and modifies `TransactionArrow.kt`

These touch different files, so no merge conflicts.

### Phase 2 — Two independent tracks:
- Task 5 creates `CanvasViewModel.kt` and refactors `IsmaBlueprintEditor.kt`
- Task 9 creates test files (no production code changes)

### Phase 3 — Sequential only:
- Task 6 is the largest change and depends on Task 5's CanvasViewModel existing

### Phase 4 — Sequential after Phase 3:
- Task 7 is small but should go in the ViewModel after the split

---

## Risk Assessment

| Phase | Risk | Mitigation |
|-------|------|------------|
| 0 | Very low | Each task is a single file change, build passes immediately |
| 1 | Low | Refactorings are mechanical — find/replace with constants, sealed class swap, extraction |
| 2 | Medium | CanvasViewModel changes the data layer; run existing code through full interaction test |
| 3 | High | VM/View split is the biggest structural change; do it in small sub-steps within the task |
| 4 | Very low | Single if-guard, no behavioral change |

---

## Total Estimated Effort

| Phase | Tasks | Cumulative Effort |
|-------|-------|-------------------|
| 0 | 3 | Low (~1 hour) |
| 1 | 3 | Medium (~3 hours) |
| 2 | 2 | Medium (~3 hours) |
| 3 | 1 | High (~6 hours) |
| 4 | 1 | Low (~30 min) |
| **Total** | **10** | **~13 hours** |
