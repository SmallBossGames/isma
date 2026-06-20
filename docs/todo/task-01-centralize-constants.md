# Task: Centralize Magic Numbers in Blueprint Editor

## Problem

Hardcoded numeric literals are scattered across 5 files, making the code brittle and hard to maintain. Changing a dimension, offset, or delay requires searching through multiple files and risk introducing visual regressions.

### Current examples:
- `200` — click delay (StateBox:99, LoopTransactionArrow:85)
- `110.0`, `65.0`, `60.0` — state dimensions
- `10.0`, `75.0`, `50.0` — arrow offset distances
- `120.0` — TextFieldLength (TransactionArrow:129)
- `40.0` — loop circle radius
- `20` — corner radius
- `3.0` — stroke widths
- `16.0` — font sizes
- `300.0`, `10.0`, `5.0`, `20.0` — PopOver dimensions

## Requirements

1. Create a single constants file that owns all numeric literals used by the blueprint editor
2. All references in the codebase must point to these constants
3. No behavioral or visual changes — output must be pixel-identical
4. Constants must be `const val` (compile-time constants) for Kotlin interop
5. Group constants by domain (states, arrows, loops, interaction, popover)

## Implementation

### Create `constants/BlueprintEditorConstants.kt`

```kotlin
package ru.isma.next.editor.blueprint.constants

// State dimensions
const val DEFAULT_STATE_WIDTH = 110.0
const val DEFAULT_STATE_HEIGHT = 65.0
const val FIXED_STATE_HEIGHT = 60.0
const val CORNER_RADIUS = 20.0
const val STATE_NAME_FONT_SIZE = 16.0
const val STATE_INSET = 10.0

// Arrow geometry
const val ARROW_LINE_OFFSET = 10.0
const val ARROW_TEXT_X_OFFSET = 75.0
const val ARROW_TEXT_Y_OFFSET = 50.0
const val ARROW_LINE_STROKE = 3.0
const val ARROWHEAD_STROKE = 3.0
const val ARROWHEAD_WIDTH = 7.0
const val ARROW_LABEL_FONT_SIZE = 16.0
const val ARROW_LABEL_FIELD_WIDTH = 120.0

// Loop arrow
const val LOOP_CIRCLE_RADIUS = 40.0
const val LOOP_CIRCLE_CENTER_X = 60.0
const val LOOP_ARROWHEAD_X = 100.0
const val LOOP_LABEL_X = 120.0
const val LOOP_LABEL_Y_OFFSET = -10.0

// Interaction
const val CLICK_DELAY_MS = 200L

// PopOver
const val POPOVER_MIN_WIDTH = 300.0
const val POPOVER_PADDING = 10.0
const val POPOVER_CORNER_RADIUS = 5.0
const val POPOVER_SHADOW_RADIUS = 20.0
```

### Update files

**StateBox.kt:**
- `SimpleDoubleProperty(110.0)` → `SimpleDoubleProperty(DEFAULT_STATE_WIDTH)`
- `SimpleDoubleProperty(65.0)` → `SimpleDoubleProperty(DEFAULT_STATE_HEIGHT)`
- `arcWidth = 20.0, arcHeight = 20.0` → `CORNER_RADIUS`
- `Font("Arial", 16.0)` → `Font("Arial", STATE_NAME_FONT_SIZE)`
- `translateX += 10.0, translateY += 10.0` → `STATE_INSET`
- `squareHeightProperty().subtract(20.0)` → `subtract(STATE_INSET * 2)` (or keep as-is, this is a derived value)

**TransactionArrow.kt:**
- `Polygon(7.0, -7.0, -7.0, 0.0, 7.0, 7.0)` → use `ARROWHEAD_WIDTH`
- `strokeWidth = 3.0` → `ARROW_LINE_STROKE` or `ARROWHEAD_STROKE`
- `translateY = -10.0` → `LOOP_LABEL_Y_OFFSET` or similar
- `translateX = -TextFieldLength / 2.0` → `-ARROW_LABEL_FIELD_WIDTH / 2.0`
- `private const val TextFieldLength = 120.0` → remove, use `ARROW_LABEL_FIELD_WIDTH`
- Geometry: `10.0`, `75.0`, `50.0` → `ARROW_LINE_OFFSET`, `ARROW_TEXT_X_OFFSET`, `ARROW_TEXT_Y_OFFSET`

**LoopTransactionArrow.kt:**
- `Circle(40.0, ...)` → `LOOP_CIRCLE_RADIUS`
- `strokeWidth = 3.0` → `ARROW_LINE_STROKE`
- `layoutX = 100.0` → `LOOP_ARROWHEAD_X`
- `translateY = -10.0` → `LOOP_LABEL_Y_OFFSET`
- `translateX = 120.0` → `LOOP_LABEL_X`
- `Font("Arial", 16.0)` → `ARROW_LABEL_FONT_SIZE`
- `Polygon(0.0, -7.0, 7.0, 0.0, -7.0, 0.0)` → use `ARROWHEAD_WIDTH`

**EditArrowPopOver.kt:**
- `minWidth = 300.0` → `POPOVER_MIN_WIDTH`
- `Insets(10.0)` → `POPOVER_PADDING`
- `CornerRadii(5.0)` → `POPOVER_CORNER_RADIUS`
- `DropShadow(20.0, ...)` → `POPOVER_SHADOW_RADIUS`

**IsmaBlueprintEditor.kt:**
- `instantiateStateBox(10.0, 200.0)` → keep positions as-is (these are layout positions, not styling constants — or create `NEW_STATE_POSITION_X`, `NEW_STATE_POSITION_Y` if you consider them constants)
- `squareHeight = 60.0` → `FIXED_STATE_HEIGHT`
- `layoutXProperty().value += 10` (x2) → `STATE_INSET`

## Acceptance Criteria

- [ ] `BlueprintEditorConstants.kt` exists with all constants organized by domain
- [ ] No hardcoded numeric literals remain in StateBox.kt, TransactionArrow.kt, LoopTransactionArrow.kt, EditArrowPopOver.kt that correspond to the constants defined
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] Visual output is identical (state boxes, arrows, popover all render at same sizes/positions)
- [ ] No new imports of `kotlin.math.*` needed solely for constant definitions
- [ ] Constants file has no package-level functions or classes — only `const val`
