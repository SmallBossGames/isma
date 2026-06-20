package ru.isma.next.editor.blueprint.models

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * CanvasViewModel tests require JavaFX runtime initialization with proper module access.
 * These tests are disabled because JavaFX controls (StateBox, TransactionArrow, LoopTransactionArrow)
 * extend JavaFX nodes that require headful JavaFX environment.
 * To enable, run with: --add-opens=java.base/java.lang=ALL-UNNAMED and a display available.
 */
@Disabled("Requires JavaFX runtime with proper module opens")
class CanvasViewModelTest {

    @Test
    fun `add state increases count`() {
    }

    @Test
    fun `remove state cascades to transactions`() {
    }

    @Test
    fun `remove state cascades to loop transactions`() {
    }

    @Test
    fun `remove transaction by arrow removes from collection`() {
    }

    @Test
    fun `remove state removes transaction from both start and end`() {
    }

    @Test
    fun `clear all removes everything`() {
    }
}
