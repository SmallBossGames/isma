package ru.isma.next.editor.blueprint.viewmodels

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CanvasViewModelTest {

    @Test
    fun `add state increases count`() {
        val vm = CanvasViewModel()
        val state = StateViewModel(name = "Test")
        vm.addState(state)
        assertEquals(1, vm.states.size)
        assertEquals(state, vm.states.first())
    }

    @Test
    fun `remove state cascades to transactions`() {
        val vm = CanvasViewModel()
        val a = vm.createState(name = "A")
        val b = vm.createState(name = "B")
        vm.addState(a)
        vm.addState(b)
        vm.addTransaction(TransactionViewModel(startStateName = "A", endStateName = "B"))

        vm.removeState(a)

        assertTrue(vm.transactions.isEmpty())
    }

    @Test
    fun `remove state cascades to loop transactions`() {
        val vm = CanvasViewModel()
        val a = vm.createState(name = "A")
        vm.addState(a)
        vm.addLoopTransaction(LoopTransactionViewModel(stateName = "A"))

        vm.removeState(a)

        assertTrue(vm.loopTransactions.isEmpty())
    }

    @Test
    fun `remove transaction by viewModel removes from collection`() {
        val vm = CanvasViewModel()
        val tx = TransactionViewModel(startStateName = "A", endStateName = "B")
        vm.addTransaction(tx)

        vm.removeTransaction(tx)

        assertTrue(vm.transactions.isEmpty())
    }

    @Test
    fun `remove state removes transaction from both start and end`() {
        val vm = CanvasViewModel()
        val a = vm.createState(name = "A")
        val b = vm.createState(name = "B")
        val c = vm.createState(name = "C")
        vm.addState(a)
        vm.addState(b)
        vm.addState(c)
        vm.addTransaction(TransactionViewModel(startStateName = "A", endStateName = "B"))
        vm.addTransaction(TransactionViewModel(startStateName = "C", endStateName = "A"))

        vm.removeState(a)

        assertTrue(vm.transactions.isEmpty())
    }

    @Test
    fun `clear all removes everything`() {
        val vm = CanvasViewModel()
        vm.addState(vm.createState(name = "A"))
        vm.addTransaction(TransactionViewModel(startStateName = "A", endStateName = "B"))
        vm.addLoopTransaction(LoopTransactionViewModel(stateName = "A"))

        vm.clearAll()

        assertTrue(vm.states.isEmpty())
        assertTrue(vm.transactions.isEmpty())
        assertTrue(vm.loopTransactions.isEmpty())
    }

    @Test
    fun `state name uniqueness is enforced`() {
        val vm = CanvasViewModel()
        val a = vm.createState(name = "A")
        vm.addState(a)
        val b = vm.createState(name = "B")

        b.name = "A"

        assertEquals("B", b.name)
    }

    @Test
    fun `state can keep its own name`() {
        val vm = CanvasViewModel()
        val a = vm.createState(name = "A")
        vm.addState(a)

        a.name = "A"

        assertEquals("A", a.name)
    }

    @Test
    fun `default state names are sequential`() {
        val vm = CanvasViewModel()
        vm.addState(vm.createState(name = vm.createNextDefaultStateName()))
        vm.addState(vm.createState(name = vm.createNextDefaultStateName()))

        assertEquals("State 1", vm.states[0].name)
        assertEquals("State 2", vm.states[1].name)
    }

    @Test
    fun `name counter recovers from removed high-numbered state`() {
        val vm = CanvasViewModel()
        vm.addState(vm.createState(name = "State 5"))
        vm.removeState(vm.states.first())

        assertEquals("State 6", vm.createNextDefaultStateName())
    }

    @Test
    fun `stateByName finds registered state`() {
        val vm = CanvasViewModel()
        val a = vm.createState(name = "A")
        vm.addState(a)

        assertEquals(a, vm.stateByName("A"))
        assertNull(vm.stateByName("B"))
    }
}
