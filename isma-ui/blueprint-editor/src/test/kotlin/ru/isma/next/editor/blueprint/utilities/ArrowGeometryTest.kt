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
        assertEquals(-180.0, geo.arrowheadRotation, 0.001)
    }

    @Test
    fun `horizontal arrow to the left`() {
        val geo = calculateArrowGeometry(100.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        assertEquals(100.0, geo.lineStartX, 0.001)
        assertEquals(ARROW_LINE_OFFSET, geo.lineStartY, 0.001)
        assertEquals(0.0, geo.lineEndX, 0.001)
        assertEquals(ARROW_LINE_OFFSET, geo.lineEndY, 0.001)
        assertEquals(0.0, geo.arrowheadRotation, 0.001)
    }

    @Test
    fun `vertical arrow downward`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 0.0, 100.0, 0.0, 0.0)

        assertEquals(ARROW_LINE_OFFSET, geo.lineStartX, 0.001)
        assertEquals(0.0, geo.lineStartY, 0.001)
        assertEquals(ARROW_LINE_OFFSET, geo.lineEndX, 0.001)
        assertEquals(100.0, geo.lineEndY, 0.001)
    }

    @Test
    fun `vertical arrow upward`() {
        val geo = calculateArrowGeometry(0.0, 100.0, 0.0, 0.0, 0.0, 0.0)

        assertEquals(-ARROW_LINE_OFFSET, geo.lineStartX, 0.001)
        assertEquals(100.0, geo.lineStartY, 0.001)
    }

    @Test
    fun `diagonal arrow bottom-right`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 100.0, 100.0, 0.0, 0.0)

        assertNotNull(geo)
        assertTrue(geo.lineEndX > geo.lineStartX)
        assertTrue(geo.lineEndY > geo.lineStartY)
    }

    @Test
    fun `layout offset is applied correctly`() {
        val geo1 = calculateArrowGeometry(50.0, 50.0, 150.0, 50.0, 0.0, 0.0)
        val geo2 = calculateArrowGeometry(50.0, 50.0, 150.0, 50.0, 100.0, 0.0)

        assertEquals(geo1.lineStartX - 100.0, geo2.lineStartX, 0.001)
        assertEquals(geo1.lineEndX - 100.0, geo2.lineEndX, 0.001)
    }

    @Test
    fun `label position is perpendicular to line direction`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 100.0, 0.0, 0.0, 0.0)

        assertEquals(0.0, geo.labelTextTranslateX, 0.001)
        assertEquals(-ARROW_TEXT_Y_OFFSET, geo.labelTextTranslateY, 0.001)
    }

    @Test
    fun `label position for vertical downward arrow`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 0.0, 100.0, 0.0, 0.0)

        assertEquals(ARROW_TEXT_X_OFFSET, geo.labelTextTranslateX, 0.001)
        assertEquals(0.0, geo.labelTextTranslateY, 0.001)
    }
}
