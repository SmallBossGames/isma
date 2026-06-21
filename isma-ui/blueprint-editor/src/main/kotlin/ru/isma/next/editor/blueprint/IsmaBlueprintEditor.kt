package ru.isma.next.editor.blueprint

import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.views.JavaFxBlueprintViewAdapter

class IsmaBlueprintEditor(
    editorFactory: ITextEditorFactory
) : BorderPane() {

    private val canvas = Pane()
    private val diagramTab = Tab("Diagram", javafx.scene.control.ScrollPane(canvas)).apply {
        isClosable = false
    }
    private val tabs = TabPane(diagramTab)
    private val viewModel = IsmaBlueprintViewModel(
        editorFactory,
        canvas,
        JavaFxBlueprintViewAdapter()
    )

    init {
        viewModel.onStateDoubleClick = { state: StateBox ->
            val tab = viewModel.openStateTextEditor(state)
            tabs.tabs.add(tab)
        }

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event: MouseEvent ->
            viewModel.onCanvasDrag(event)
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

        val newTransitionButton = Button("New transition").apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.AddTransition) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleAddTransition()
                }
            }
            textProperty().bind(Bindings.createStringBinding({
                when (viewModel.editorMode) {
                    is EditorMode.AddTransition -> "Stop adding transaction"
                    else -> "New transition"
                }
            }, viewModel.editorModeProperty))
        }

        val separator = Separator()

        val removeStateButton = Button("Remove state").apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.RemoveState) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleRemoveState()
                }
            }
            textProperty().bind(Bindings.createStringBinding({
                when (viewModel.editorMode) {
                    is EditorMode.RemoveState -> "Stop remove state"
                    else -> "Remove state"
                }
            }, viewModel.editorModeProperty))
        }

        val removeTransitionButton = Button("Remove transition").apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.RemoveTransition) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleRemoveTransition()
                }
            }
            textProperty().bind(Bindings.createStringBinding({
                when (viewModel.editorMode) {
                    is EditorMode.RemoveTransition -> "Stop remove transition"
                    else -> "Remove transition"
                }
            }, viewModel.editorModeProperty))
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
