---
name: tech-analysis
description: Perform technical analysis and decompose features or issues into structured, atomic tasks with plan documents and task documents
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

See `templates/plan.md` for the full template.

### Task Document (`task-NN-<slug>.md`)

See `templates/task.md` for the full template.

## Templates

Templates are provided in the `templates/` directory:

- `templates/plan.md` — Plan document template
- `templates/task.md` — Task document template

Copy and fill in the appropriate template when creating new documents.

## Naming Conventions

- Directory: `docs/todo/<initiative-name>/`
- Plan: `plan.md`
- Tasks: `task-NN-<slug>.md` (zero-padded, kebab-case)

## Rules

### Plan Documents
- Scope analysis uses tables (files to modify/delete/update)
- Dependency graph shows parallelization opportunities
- Execution phases group tasks by build compatibility
- Risk assessment per phase with mitigations
- Test strategy — what tests are needed at the initiative level
- Verification includes exact build/test/grep commands

### Task Documents
- Each task is atomic — independently completable and compilable
- Problem description: what's wrong, why it matters
- Expectations: what the result looks like
- Implementation: high-level description only — design decisions, approach, data flow. No code snippets.
- Tests — test scenarios that must be covered (unit, integration, edge cases)
- Acceptance criteria: `[ ]` checklist, verifiable
- Include verification commands (build, test, grep)

### General
- Follow project architecture rules (separate domain from contracts, no UI in services, etc.)
- Use the `write-docs` skill conventions for diagrams and tables when appropriate
- Reference concrete file paths
- Each task should leave the codebase in a compilable state
- Every feature must include test coverage — no exceptions

## Flow

### When performing technical analysis

1. **Explore the codebase.** Use the explore subagent to understand the area in scope. Read build files, DI modules, key interfaces, and callers.

2. **Identify the problem.** What is wrong? What is the impact? What needs to change?

3. **Decompose into tasks.** Break work into atomic steps. Each step:
    - Is independently completable
    - Leaves the codebase compilable
    - Has clear acceptance criteria
    - Describes the implementation at a high level (no code snippets)

4. **Build dependency graph.** Which tasks are independent? Which are sequential? Group into phases.

5. **Write the plan document.** Create `docs/todo/<name>/plan.md` (or `execution-plan.md`).

6. **Write task documents.** Create `docs/todo/<name>/task-NN-<slug>.md` for each task.

7. **Review.** Verify:
    - [ ] Plan has scope analysis tables
    - [ ] Dependency graph shows parallelization
    - [ ] Each task is atomic and compilable
    - [ ] Each task has problem, expectations, high-level implementation, test scenarios, acceptance criteria
    - [ ] Verification commands are exact and runnable
    - [ ] Risk assessment included
    - [ ] Test coverage is specified for every task
