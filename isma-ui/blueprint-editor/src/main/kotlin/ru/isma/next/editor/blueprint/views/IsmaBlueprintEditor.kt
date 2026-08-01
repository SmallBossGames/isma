package ru.isma.next.editor.blueprint.views

import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.viewmodels.EditorMode
import ru.isma.next.editor.blueprint.viewmodels.IsmaBlueprintViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel

class IsmaBlueprintEditor(
    editorFactory: ITextEditorFactory
) : BorderPane() {

    private val canvas = javafx.scene.layout.Pane()
    private val diagramTab = Tab("Diagram", javafx.scene.control.ScrollPane(canvas)).apply {
        isClosable = false
    }
    private val tabs = TabPane(diagramTab)
    private val viewModel = IsmaBlueprintViewModel(editorFactory)
    private val canvasView = CanvasView(canvas, viewModel.canvasViewModel) { stateVm ->
        val tab = viewModel.openStateTextEditor(stateVm)
        tabs.tabs.add(tab)
    }

    init {
        viewModel.onStateDoubleClick = { stateViewModel: StateViewModel ->
            val tab = viewModel.openStateTextEditor(stateViewModel)
            tabs.tabs.add(tab)
        }

        center = tabs
        bottom = buildToolbar()
    }

    private fun buildToolbar(): ToolBar {
        val newStateButton = Button("New state").apply {
            onAction = EventHandler {
                viewModel.resetMode()
                viewModel.addState()
            }
        }

        val newTransitionButton = Button().apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.AddTransition) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleAddTransition()
                }
            }
            textProperty().bind(viewModel.addTransitionButtonText)
        }

        val separator = Separator()

        val removeStateButton = Button().apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.RemoveState) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleRemoveState()
                }
            }
            textProperty().bind(viewModel.removeStateButtonText)
        }

        val removeTransitionButton = Button().apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.RemoveTransition) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleRemoveTransition()
                }
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
}
