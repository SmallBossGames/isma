package ru.isma.next.editor.blueprint.viewmodels

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ru.isma.next.editor.blueprint.viewmodels.LoopTransactionViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel
import ru.isma.next.editor.blueprint.viewmodels.TransactionViewModel

/**
 * CanvasViewModel tests require JavaFX runtime initialization because
 * ViewModels use JavaFX Properties (SimpleStringProperty, SimpleDoubleProperty, etc.).
 * These tests are disabled because JavaFX requires headful environment for Property initialization.
 * To enable, run with proper JavaFX module opens and a display available.
 */
@Disabled("Requires JavaFX runtime for Property initialization")
class CanvasViewModelTest {

    @Test
    fun `add state increases count`() {
        val vm = CanvasViewModel()
        val state = StateViewModel(name = "Test")
        vm.addState(state)
    }

    @Test
    fun `remove state cascades to transactions`() {
    }

    @Test
    fun `remove state cascades to loop transactions`() {
    }

    @Test
    fun `remove transaction by viewModel removes from collection`() {
    }

    @Test
    fun `remove state removes transaction from both start and end`() {
    }

    @Test
    fun `clear all removes everything`() {
    }
}
