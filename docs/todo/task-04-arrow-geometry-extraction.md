# Task: Extract Arrow Geometry Calculation to Pure Function

## Problem

`TransactionArrow.updateGeometry()` (lines 76-96) contains ~20 lines of trigonometric calculations that are tightly coupled to the control's internal state. The function mutates properties (`startX`, `startY`, `endX`, `endY`, `arrowhead.translateX`, etc.) as a side effect, making it hard to test and reason about.

There are no unit tests for the arrow geometry logic, so any change to the math risks visual regressions.

## Requirements

1. Extract the geometry calculation into a pure function `calculateArrowGeometry()` in a utilities package
2. The function must take raw coordinates and return a data class with all computed values
3. No side effects — the function must not mutate any properties
4. Add unit tests covering horizontal, vertical, and diagonal arrows
5. `TransactionArrow` must use the extracted function instead of inline math

## Implementation

### Create `utilities/ArrowGeometry.kt`

```kotlin
package ru.isma.next.editor.blueprint.utilities

import ru.isma.next.editor.blueprint.constants.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class ArrowGeometry(
    val lineStartX: Double,
    val lineStartY: Double,
    val lineEndX: Double,
    val lineEndY: Double,
    val arrowheadTranslateX: Double,
    val arrowheadTranslateY: Double,
    val arrowheadRotation: Double,
    val labelTextTranslateX: Double,
    val labelTextTranslateY: Double
)

fun calculateArrowGeometry(
    startX: Double,
    startY: Double,
    endX: Double,
    endY: Double,
    layoutX: Double,
    layoutY: Double,
    lineOffset: Double = ARROW_LINE_OFFSET,
    textXOffset: Double = ARROW_TEXT_X_OFFSET,
    textYOffset: Double = ARROW_TEXT_Y_OFFSET
): ArrowGeometry {
    val dx = endX - startX
    val dy = endY - startY
    val angle = atan2(dx, dy) + PI / 2

    val offsetX = lineOffset * sin(angle)
    val offsetY = lineOffset * cos(angle)

    return ArrowGeometry(
        lineStartX = startX - layoutX + offsetX,
        lineStartY = startY - layoutY + offsetY,
        lineEndX = endX - layoutX + offsetX,
        lineEndY = endY - layoutY + offsetY,
        arrowheadTranslateX = offsetX,
        arrowheadTranslateY = offsetY,
        arrowheadRotation = -angle / PI * 180.0,
        labelTextTranslateX = textXOffset * sin(angle),
        labelTextTranslateY = textYOffset * cos(angle)
    )
}
```

### Refactor `TransactionArrow.kt`

**Replace the `updateGeometry()` function body:**

Before (inline):
```kotlin
fun updateGeometry() {
    val x = this@TransactionArrow.endX - this@TransactionArrow.startX
    val y = this@TransactionArrow.endY - this@TransactionArrow.startY
    val angle = atan2(x, y) + PI / 2
    val offsetX = 10.0 * sin(angle)
    val offsetY = 10.0 * cos(angle)
    val textOffsetX = 75.0 * sin(angle)
    val textOffsetY = 50.0 * cos(angle)

    startX = this@TransactionArrow.startX - this@TransactionArrow.layoutX + offsetX
    startY = this@TransactionArrow.startY - this@TransactionArrow.layoutY + offsetY
    endX = this@TransactionArrow.endX - this@TransactionArrow.layoutX + offsetX
    endY = this@TransactionArrow.endY - this@TransactionArrow.layoutY + offsetY

    arrowhead.translateX = offsetX
    arrowhead.translateY = offsetY
    arrowhead.rotate = -angle / PI * 180.0

    predicateTextWrapped.translateX = textOffsetX
    predicateTextWrapped.translateY = textOffsetY
}
```

After (using extracted function):
```kotlin
fun updateGeometry() {
    val geometry = calculateArrowGeometry(
        startX = this@TransactionArrow.startX,
        startY = this@TransactionArrow.startY,
        endX = this@TransactionArrow.endX,
        endY = this@TransactionArrow.endY,
        layoutX = this@TransactionArrow.layoutX,
        layoutY = this@TransactionArrow.layoutY,
        lineOffset = ARROW_LINE_OFFSET,
        textXOffset = ARROW_TEXT_X_OFFSET,
        textYOffset = ARROW_TEXT_Y_OFFSET
    )

    startX = geometry.lineStartX
    startY = geometry.lineStartY
    endX = geometry.lineEndX
    endY = geometry.lineEndY

    arrowhead.translateX = geometry.arrowheadTranslateX
    arrowhead.translateY = geometry.arrowheadTranslateY
    arrowhead.rotate = geometry.arrowheadRotation

    predicateTextWrapped.translateX = geometry.labelTextTranslateX
    predicateTextWrapped.translateY = geometry.labelTextTranslateY
}
```

**Remove unused imports:**
- `kotlin.math.atan2`, `kotlin.math.cos`, `kotlin.math.sin`, `kotlin.math.PI` — no longer needed in this file

### Create `src/test/kotlin/.../ArrowGeometryTest.kt`

```kotlin
package ru.isma.next.editor.blueprint.utilities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.isma.next.editor.blueprint.constants.*

class ArrowGeometryTest {

    @Test
    fun `horizontal arrow to the right`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 100.0, 0.0, 0.0, 0.0)

        assertEquals(0.0, geo.lineStartX, 0.001)
        assertEquals(-ARROW_LINE_OFFSET, geo.lineStartY, 0.001)
        assertEquals(100.0, geo.lineEndX, 0.001)
        assertEquals(-ARROW_LINE_OFFSET, geo.lineEndY, 0.001)
        assertEquals(0.0, geo.arrowheadRotation, 0.001)
    }

    @Test
    fun `horizontal arrow to the left`() {
        val geo = calculateArrowGeometry(100.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        assertEquals(0.0, geo.lineStartX, 0.001)
        assertEquals(ARROW_LINE_OFFSET, geo.lineStartY, 0.001)
        assertEquals(100.0, geo.lineEndX, 0.001)
        assertEquals(ARROW_LINE_OFFSET, geo.lineEndY, 0.001)
        assertEquals(180.0, geo.arrowheadRotation, 0.001)
    }

    @Test
    fun `vertical arrow downward`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 0.0, 100.0, 0.0, 0.0)

        assertEquals(-ARROW_LINE_OFFSET, geo.lineStartX, 0.001)
        assertEquals(0.0, geo.lineStartY, 0.001)
        assertEquals(-ARROW_LINE_OFFSET, geo.lineEndX, 0.001)
        assertEquals(100.0, geo.lineEndY, 0.001)
    }

    @Test
    fun `vertical arrow upward`() {
        val geo = calculateArrowGeometry(0.0, 100.0, 0.0, 0.0, 0.0, 0.0)

        assertEquals(ARROW_LINE_OFFSET, geo.lineStartX, 0.001)
        assertEquals(0.0, geo.lineStartY, 0.001)
    }

    @Test
    fun `diagonal arrow bottom-right`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 100.0, 100.0, 0.0, 0.0)

        // Verify geometry is computed without errors
        assertNotNull(geo)
        // Arrow should point toward end state
        assertTrue(geo.lineEndX > geo.lineStartX)
        assertTrue(geo.lineEndY > geo.lineStartY)
    }

    @Test
    fun `layout offset is applied correctly`() {
        val geo1 = calculateArrowGeometry(50.0, 50.0, 150.0, 50.0, 0.0, 0.0)
        val geo2 = calculateArrowGeometry(50.0, 50.0, 150.0, 50.0, 100.0, 0.0)

        // Shifting layoutX by 100 should shift all line coordinates by -100
        assertEquals(geo1.lineStartX - 100.0, geo2.lineStartX, 0.001)
        assertEquals(geo1.lineEndX - 100.0, geo2.lineEndX, 0.001)
    }

    @Test
    fun `label position is perpendicular to line direction`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 100.0, 0.0, 0.0, 0.0)

        // For horizontal arrow, label should be offset vertically
        assertEquals(0.0, geo.labelTextTranslateX, 0.001)
        assertEquals(-ARROW_TEXT_Y_OFFSET, geo.labelTextTranslateY, 0.001)
    }
}
```

## Acceptance Criteria

- [ ] `ArrowGeometry.kt` exists with `calculateArrowGeometry()` function and `ArrowGeometry` data class
- [ ] `TransactionArrow.kt` no longer contains `atan2`, `sin`, `cos`, or `PI` imports
- [ ] `TransactionArrow.kt` calls `calculateArrowGeometry()` instead of computing inline
- [ ] `ArrowGeometryTest.kt` exists with at least 5 test methods
- [ ] `./gradlew :isma-ui:blueprint-editor:test` passes all tests
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] Arrow rendering is visually identical (arrows point in the same directions, same offsets)
