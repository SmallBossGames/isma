package ru.isma.next.editor.blueprint.views

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.viewmodels.BlueprintEvent
import ru.isma.next.editor.blueprint.viewmodels.IsmaBlueprintViewModel
import ru.isma.next.editor.blueprint.viewmodels.LoopTransactionViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel

class IsmaBlueprintEditor(
    private val editorFactory: ITextEditorFactory,
    private val viewModel: IsmaBlueprintViewModel = IsmaBlueprintViewModel()
) : BorderPane() {

    private val canvas = javafx.scene.layout.Pane()
    private val diagramTab = Tab("Diagram", javafx.scene.control.ScrollPane(canvas)).apply {
        isClosable = false
    }
    private val tabs = TabPane(diagramTab)
    private val openEditorTabs = mutableMapOf<Tab, Node>()

    init {
        viewModel.eventProperty.addListener { _, _, event ->
            when (event) {
                is BlueprintEvent.OpenStateEditor -> openStateEditorTab(event.state)
                is BlueprintEvent.OpenLoopEditor -> openLoopEditorTab(event.loop, event.state)
            }
        }

        val canvasView = CanvasView(canvas, viewModel.canvasViewModel, viewModel)

        center = tabs
        bottom = buildToolbar()
    }

    private fun openStateEditorTab(stateVm: StateViewModel) {
        val editor = editorFactory.createTextEditor(
            text = stateVm.text,
            onTextChanged = { stateVm.text = it }
        )
        val tab = Tab(stateVm.name, editor).apply {
            textProperty().bind(stateVm.nameProperty)
            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
                openEditorTabs.remove(this@apply)
            }
        }
        openEditorTabs[tab] = editor
        tabs.tabs.add(tab)
    }

    private fun openLoopEditorTab(loopTxVm: LoopTransactionViewModel, stateVm: StateViewModel) {
        val editor = editorFactory.createTextEditor(
            text = loopTxVm.text,
            onTextChanged = { loopTxVm.text = it }
        )
        val tab = Tab("${stateVm.name} (loop)", editor).apply {
            textProperty().bind(stateVm.nameProperty.concat(" (loop)"))
            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
                openEditorTabs.remove(this@apply)
            }
        }
        openEditorTabs[tab] = editor
        tabs.tabs.add(tab)
    }

    private fun buildToolbar(): ToolBar {
        val newStateButton = Button("New state").apply {
            onAction = EventHandler {
                viewModel.addState()
            }
        }

        val newTransitionButton = Button().apply {
            onAction = EventHandler {
                viewModel.toggleAddTransition()
            }
            textProperty().bind(viewModel.addTransitionButtonText)
        }

        val separator = Separator()

        val removeStateButton = Button().apply {
            onAction = EventHandler {
                viewModel.toggleRemoveState()
            }
            textProperty().bind(viewModel.removeStateButtonText)
        }

        val removeTransitionButton = Button().apply {
            onAction = EventHandler {
                viewModel.toggleRemoveTransition()
            }
            textProperty().bind(viewModel.removeTransitionButtonText)
        }

        return ToolBar(newStateButton, newTransitionButton, separator, removeStateButton, removeTransitionButton).apply {
            val visible = tabs.selectionModel.selectedItemProperty().isEqualTo(diagramTab)
            visibleProperty().bind(visible)
            managedProperty().bind(visible)
        }
    }

    fun getBlueprintModel() = viewModel.toBlueprintModel()

    fun setBlueprintModel(model: ru.isma.next.editor.blueprint.models.BlueprintModel) {
        viewModel.fromBlueprintModel(model)
    }

    fun dispose() {
        openEditorTabs.values.forEach { editorFactory.disposeInstance(it) }
        openEditorTabs.clear()
    }
}
