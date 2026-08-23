package ru.isma.next.editor.blueprint.views

import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import ru.isma.next.editor.blueprint.services.ITextEditor
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
    private val openStateEditorTabs = mutableMapOf<StateViewModel, OpenEditorTab>()
    private val openLoopEditorTabs = mutableMapOf<LoopTransactionViewModel, OpenEditorTab>()

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

    private class OpenEditorTab(
        val tab: Tab,
        val editor: ITextEditor,
        val boundText: StringProperty,
    )

    private fun openStateEditorTab(stateVm: StateViewModel) {
        openEditorTab(openStateEditorTabs, stateVm, stateVm.nameProperty, stateVm.textProperty)
    }

    private fun openLoopEditorTab(loopTxVm: LoopTransactionViewModel, stateVm: StateViewModel) {
        openEditorTab(openLoopEditorTabs, loopTxVm, stateVm.nameProperty.concat(" (loop)"), loopTxVm.textProperty)
    }

    private fun <K : Any> openEditorTab(
        openTabs: MutableMap<K, OpenEditorTab>,
        key: K,
        title: ObservableValue<String>,
        boundText: StringProperty,
    ) {
        openTabs[key]?.let { openTab ->
            tabs.selectionModel.select(openTab.tab)
            return
        }

        val editor = editorFactory.createEditor()
        editor.text.bindBidirectional(boundText)

        val tab = Tab(title.value, editor.node).apply {
            textProperty().bind(title)
            setOnCloseRequest {
                editor.text.unbind()
                boundText.unbind()
                editor.dispose()
                openTabs.remove(key)
            }
        }

        openTabs[key] = OpenEditorTab(tab, editor, boundText)
        tabs.tabs.add(tab)
        tabs.selectionModel.select(tab)
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
        (openStateEditorTabs.values + openLoopEditorTabs.values).forEach { openTab ->
            openTab.editor.text.unbind()
            openTab.boundText.unbind()
            tabs.tabs.remove(openTab.tab)
            openTab.editor.dispose()
        }
        openStateEditorTabs.clear()
        openLoopEditorTabs.clear()
    }
}
