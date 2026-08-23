package ru.isma.next.editor.blueprint.viewmodels

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IsmaBlueprintViewModelTest {

    @Test
    fun `main and init states exist on creation`() {
        val vm = IsmaBlueprintViewModel()
        assertEquals(2, vm.canvasViewModel.states.size)
        assertEquals("Main", vm.canvasViewModel.states[0].name)
        assertEquals("init", vm.canvasViewModel.states[1].name)
        assertEquals(StateKind.MAIN, vm.canvasViewModel.states[0].kind)
        assertEquals(StateKind.INIT, vm.canvasViewModel.states[1].kind)
    }

    @Test
    fun `toggle add transition is idempotent`() {
        val vm = IsmaBlueprintViewModel()
        vm.toggleAddTransition()
        assertTrue(vm.editorMode is EditorMode.AddTransition)
        vm.toggleAddTransition()
        assertTrue(vm.editorMode is EditorMode.Idle)
    }

    @Test
    fun `toggle remove state is idempotent`() {
        val vm = IsmaBlueprintViewModel()
        vm.toggleRemoveState()
        assertTrue(vm.editorMode is EditorMode.RemoveState)
        vm.toggleRemoveState()
        assertTrue(vm.editorMode is EditorMode.Idle)
    }

    @Test
    fun `toggle remove transition is idempotent`() {
        val vm = IsmaBlueprintViewModel()
        vm.toggleRemoveTransition()
        assertTrue(vm.editorMode is EditorMode.RemoveTransition)
        vm.toggleRemoveTransition()
        assertTrue(vm.editorMode is EditorMode.Idle)
    }

    @Test
    fun `add state resets mode and creates user state with default name`() {
        val vm = IsmaBlueprintViewModel()
        vm.toggleAddTransition()

        val state = vm.addState()

        assertTrue(vm.editorMode is EditorMode.Idle)
        assertEquals(StateKind.USER, state.kind)
        assertEquals("State 1", state.name)
        assertTrue(vm.canvasViewModel.states.contains(state))
    }

    @Test
    fun `handle state click in idle mode starts edit for user state only`() {
        val vm = IsmaBlueprintViewModel()
        val user = vm.addState()
        val main = vm.canvasViewModel.stateByName("Main")!!

        vm.handleStateClick(user)
        vm.handleStateClick(main)

        assertTrue(user.editMode)
        assertFalse(main.editMode)
    }

    @Test
    fun `handle state click in remove transition mode starts edit for user state`() {
        val vm = IsmaBlueprintViewModel()
        val user = vm.addState()
        vm.toggleRemoveTransition()

        vm.handleStateClick(user)

        assertTrue(user.editMode)
    }

    @Test
    fun `handle state click in add transition mode records transition source`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        vm.toggleAddTransition()

        vm.handleStateClick(a)
        assertEquals(0, vm.canvasViewModel.transactions.size)

        vm.handleStateClick(b)
        assertEquals(1, vm.canvasViewModel.transactions.size)
        assertEquals(a.name, vm.canvasViewModel.transactions.first().startStateName)
        assertEquals(b.name, vm.canvasViewModel.transactions.first().endStateName)
        assertTrue(vm.editorMode is EditorMode.Idle)
    }

    @Test
    fun `handle state click in add transition mode with same state creates loop arrow`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        vm.toggleAddTransition()

        vm.handleStateClick(a)
        vm.handleStateClick(a)

        assertEquals(0, vm.canvasViewModel.transactions.size)
        assertEquals(1, vm.canvasViewModel.loopTransactions.size)
        assertEquals(a.name, vm.canvasViewModel.loopTransactions.first().stateName)
        assertTrue(vm.editorMode is EditorMode.Idle)
    }

    @Test
    fun `handle state click in remove state mode removes user state`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        vm.addTransactionArrow(a, b, "", "")
        vm.toggleRemoveState()

        vm.handleStateClick(a)

        assertFalse(vm.canvasViewModel.states.contains(a))
        assertTrue(vm.canvasViewModel.transactions.isEmpty())
    }

    @Test
    fun `remove state protects main and init`() {
        val vm = IsmaBlueprintViewModel()
        val main = vm.canvasViewModel.stateByName("Main")!!
        val init = vm.canvasViewModel.stateByName("init")!!

        vm.removeState(main)
        vm.removeState(init)

        assertTrue(vm.canvasViewModel.states.contains(main))
        assertTrue(vm.canvasViewModel.states.contains(init))
    }

    @Test
    fun `duplicate transaction between same states is not added`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()

        vm.addTransactionArrow(a, b, "p1", "")
        vm.addTransactionArrow(a, b, "p2", "")

        assertEquals(1, vm.canvasViewModel.transactions.size)
    }

    @Test
    fun `duplicate loop arrow for same state is not added`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()

        vm.addLoopArrow(a, "t1", "", "")
        vm.addLoopArrow(a, "t2", "", "")

        assertEquals(1, vm.canvasViewModel.loopTransactions.size)
    }

    @Test
    fun `handle arrow body click removes only in remove transition mode`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        vm.addTransactionArrow(a, b, "", "")
        val tx = vm.canvasViewModel.transactions.first()

        vm.handleArrowBodyClick(tx)
        assertTrue(vm.canvasViewModel.transactions.contains(tx))

        vm.toggleRemoveTransition()
        vm.handleArrowBodyClick(tx)
        assertFalse(vm.canvasViewModel.transactions.contains(tx))
    }

    @Test
    fun `handle arrowhead click returns true in normal mode`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        vm.addTransactionArrow(a, b, "", "")
        val tx = vm.canvasViewModel.transactions.first()

        assertTrue(vm.handleArrowheadClick(tx))
        assertTrue(vm.canvasViewModel.transactions.contains(tx))
    }

    @Test
    fun `handle arrowhead click removes and returns false in remove transition mode`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        vm.addTransactionArrow(a, b, "", "")
        val tx = vm.canvasViewModel.transactions.first()
        vm.toggleRemoveTransition()

        assertFalse(vm.handleArrowheadClick(tx))
        assertFalse(vm.canvasViewModel.transactions.contains(tx))
    }

    @Test
    fun `handle loop arrow body click removes only in remove transition mode`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        vm.addLoopArrow(a, "", "", "")
        val loop = vm.canvasViewModel.loopTransactions.first()

        vm.handleLoopArrowBodyClick(loop)
        assertTrue(vm.canvasViewModel.loopTransactions.contains(loop))

        vm.toggleRemoveTransition()
        vm.handleLoopArrowBodyClick(loop)
        assertFalse(vm.canvasViewModel.loopTransactions.contains(loop))
    }

    @Test
    fun `handle state double click fires open state editor event`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()

        vm.handleStateDoubleClick(a)

        assertEquals(BlueprintEvent.OpenStateEditor(a), vm.eventProperty.value)
    }

    @Test
    fun `handle loop arrowhead double click fires open loop editor event`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        vm.addLoopArrow(a, "", "", "")
        val loop = vm.canvasViewModel.loopTransactions.first()

        vm.handleLoopArrowheadDoubleClick(loop, a)

        assertEquals(BlueprintEvent.OpenLoopEditor(loop, a), vm.eventProperty.value)
    }

    @Test
    fun `commit name edit updates name and exits edit mode`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        a.startEdit()

        vm.commitNameEdit(a, "Renamed")

        assertEquals("Renamed", a.name)
        assertFalse(a.editMode)
    }

    @Test
    fun `commit name edit ignores duplicate name`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        b.startEdit()

        vm.commitNameEdit(b, a.name)

        assertEquals("State 2", b.name)
        assertFalse(b.editMode)
    }

    @Test
    fun `button texts follow editor mode`() {
        val vm = IsmaBlueprintViewModel()
        assertEquals("New transition", vm.addTransitionButtonText.value)
        vm.toggleAddTransition()
        assertEquals("Stop adding transaction", vm.addTransitionButtonText.value)
        vm.resetMode()
        assertEquals("New transition", vm.addTransitionButtonText.value)
    }

    @Test
    fun `blueprint model round trip preserves states and transactions`() {
        val vm = IsmaBlueprintViewModel()
        val a = vm.addState()
        val b = vm.addState()
        vm.addTransactionArrow(a, b, "p", "alias")
        vm.addLoopArrow(a, "loop text", "lp", "la")

        val model = vm.toBlueprintModel()

        val vm2 = IsmaBlueprintViewModel()
        vm2.fromBlueprintModel(model)

        assertEquals(4, vm2.canvasViewModel.states.size)
        assertEquals(1, vm2.canvasViewModel.transactions.size)
        assertEquals(1, vm2.canvasViewModel.loopTransactions.size)
        assertEquals("p", vm2.canvasViewModel.transactions.first().predicate)
        assertEquals("alias", vm2.canvasViewModel.transactions.first().alias)
        assertEquals("loop text", vm2.canvasViewModel.loopTransactions.first().text)
        assertEquals(StateKind.MAIN, vm2.canvasViewModel.stateByName("Main")!!.kind)
        assertEquals(StateKind.INIT, vm2.canvasViewModel.stateByName("init")!!.kind)
    }
}
