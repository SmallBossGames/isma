package ru.isma.next.editor.blueprint.views

import javafx.collections.ListChangeListener
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import ru.isma.next.editor.blueprint.viewmodels.CanvasViewModel
import ru.isma.next.editor.blueprint.viewmodels.IsmaBlueprintViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateKind
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow
import ru.isma.next.editor.blueprint.controls.LoopTransactionArrow
import ru.isma.next.editor.blueprint.controls.EditArrowPopOver
import ru.isma.next.editor.blueprint.viewmodels.LoopTransactionViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel
import ru.isma.next.editor.blueprint.viewmodels.TransactionViewModel
import kotlin.math.max

class CanvasView(
    private val canvas: Pane,
    private val canvasViewModel: CanvasViewModel,
    private val blueprintViewModel: IsmaBlueprintViewModel
) {
    private val stateNodeMap = mutableMapOf<StateViewModel, StateBox>()
    private val transactionNodeMap = mutableMapOf<TransactionViewModel, TransactionArrow>()
    private val loopTransactionNodeMap = mutableMapOf<LoopTransactionViewModel, LoopTransactionArrow>()

    private var dragState: DragState? = null

    data class DragState(
        val stateViewModel: StateViewModel,
        val initialSceneX: Double,
        val initialSceneY: Double,
        val clickOffsetX: Double,
        val clickOffsetY: Double
    )

    init {
        canvasViewModel.states.addListener(ListChangeListener { _ -> syncStates() })
        canvasViewModel.transactions.addListener(ListChangeListener { _ -> syncTransactions() })
        canvasViewModel.loopTransactions.addListener(ListChangeListener { _ -> syncLoopTransactions() })

        // Initial render
        syncStates()
        syncTransactions()
        syncLoopTransactions()
    }

    private fun syncStates() {
        // Add new states
        canvasViewModel.states.forEach { stateViewModel: StateViewModel ->
            if (!stateNodeMap.containsKey(stateViewModel)) {
                createStateNode(stateViewModel)
            }
        }
        // Remove deleted states
        stateNodeMap.keys.filter { !canvasViewModel.states.contains(it) }.forEach { removeState(it) }
    }

    private fun syncTransactions() {
        // Add new transactions
        canvasViewModel.transactions.forEach { txViewModel: TransactionViewModel ->
            if (!transactionNodeMap.containsKey(txViewModel)) {
                createTransactionNode(txViewModel)
            }
        }
        // Remove deleted transactions
        transactionNodeMap.keys.filter { !canvasViewModel.transactions.contains(it) }.forEach { removeTransaction(it) }
    }

    private fun syncLoopTransactions() {
        // Add new loop transactions
        canvasViewModel.loopTransactions.forEach { loopViewModel: LoopTransactionViewModel ->
            if (!loopTransactionNodeMap.containsKey(loopViewModel)) {
                createLoopTransactionNode(loopViewModel)
            }
        }
        // Remove deleted loop transactions
        loopTransactionNodeMap.keys.filter { !canvasViewModel.loopTransactions.contains(it) }.forEach { removeLoopTransaction(it) }
    }

    private fun createStateNode(stateViewModel: StateViewModel) {
        val stateBox = StateBox(
            viewModel = stateViewModel,
            fill = stateFill(stateViewModel.kind),
            onSingleClick = { vm -> blueprintViewModel.handleStateClick(vm) },
            onDoubleClick = { vm -> blueprintViewModel.handleStateDoubleClick(vm) },
            onNameCommitted = { vm, name -> blueprintViewModel.commitNameEdit(vm, name) }
        )

        setupStateDrag(stateBox, stateViewModel)

        stateNodeMap[stateViewModel] = stateBox
        canvas.children.add(stateBox)
    }

    private fun removeState(stateViewModel: StateViewModel) {
        val stateBox = stateNodeMap.remove(stateViewModel)
        stateBox?.let {
            canvas.children.remove(it)
        }
    }

    private fun createTransactionNode(txViewModel: TransactionViewModel) {
        val startState = canvasViewModel.stateByName(txViewModel.startStateName)
        val endState = canvasViewModel.stateByName(txViewModel.endStateName)

        if (startState == null || endState == null) return

        val arrow = TransactionArrow(
            viewModel = txViewModel,
            startViewModel = startState,
            endViewModel = endState,
            onArrowheadClicked = { _, event ->
                if (blueprintViewModel.handleArrowheadClick(txViewModel)) {
                    val canvasPos = canvas.sceneToLocal(event.sceneX, event.sceneY)
                    val popover = EditArrowPopOver(txViewModel, canvasPos.x, canvasPos.y)
                    canvas.children.add(popover)
                    popover.setOnMouseExited { canvas.children.remove(popover) }
                }
            },
            onBodyClicked = { _, _ -> blueprintViewModel.handleArrowBodyClick(txViewModel) }
        )

        transactionNodeMap[txViewModel] = arrow
        canvas.children.add(arrow)
    }

    private fun removeTransaction(txViewModel: TransactionViewModel) {
        val arrow = transactionNodeMap.remove(txViewModel)
        arrow?.let {
            canvas.children.remove(it)
        }
    }

    private fun createLoopTransactionNode(loopViewModel: LoopTransactionViewModel) {
        val stateViewModel = canvasViewModel.stateByName(loopViewModel.stateName)
        if (stateViewModel == null) return

        val arrow = LoopTransactionArrow(
            viewModel = loopViewModel,
            stateViewModel = stateViewModel,
            onBodyClicked = { _, _ -> blueprintViewModel.handleLoopArrowBodyClick(loopViewModel) },
            onArrowheadClicked = { _, _ -> },
            onArrowheadDoubleClicked = { _, _ ->
                blueprintViewModel.handleLoopArrowheadDoubleClick(loopViewModel, stateViewModel)
            }
        )

        loopTransactionNodeMap[loopViewModel] = arrow
        canvas.children.add(arrow)
    }

    private fun removeLoopTransaction(loopViewModel: LoopTransactionViewModel) {
        val arrow = loopTransactionNodeMap.remove(loopViewModel)
        arrow?.let {
            canvas.children.remove(it)
        }
    }

    private fun stateFill(kind: StateKind): Paint = when (kind) {
        StateKind.MAIN -> Color.LIGHTGREEN
        StateKind.INIT -> Color.LIGHTBLUE
        StateKind.USER -> Color.CORAL
    }

    private fun setupStateDrag(stateBox: StateBox, stateViewModel: StateViewModel) {
        stateBox.addEventHandler(MouseEvent.MOUSE_PRESSED) { event ->
            val scenePos = canvas.sceneToLocal(event.sceneX, event.sceneY)
            dragState = DragState(
                stateViewModel = stateViewModel,
                initialSceneX = scenePos.x,
                initialSceneY = scenePos.y,
                clickOffsetX = scenePos.x - stateViewModel.x,
                clickOffsetY = scenePos.y - stateViewModel.y
            )
            event.consume()
        }

        stateBox.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event ->
            dragState?.let { drag ->
                val scenePos = canvas.sceneToLocal(event.sceneX, event.sceneY)
                val newX = max(scenePos.x - drag.clickOffsetX, 0.0)
                val newY = max(scenePos.y - drag.clickOffsetY, 0.0)
                drag.stateViewModel.xProperty.value = newX
                drag.stateViewModel.yProperty.value = newY
            }
            event.consume()
        }

        stateBox.addEventHandler(MouseEvent.MOUSE_RELEASED) { _ ->
            dragState = null
        }
    }
}
