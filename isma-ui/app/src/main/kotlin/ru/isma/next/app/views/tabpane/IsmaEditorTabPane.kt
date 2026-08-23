package ru.isma.next.app.views.tabpane

import javafx.collections.ListChangeListener
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import ru.isma.next.app.viewmodels.EditorTabPaneViewModel
import ru.isma.next.app.viewmodels.ProjectItemViewModel

class IsmaEditorTabPane(
    private val viewModel: EditorTabPaneViewModel,
) : TabPane() {
    private val tabItems = mutableMapOf<Tab, ProjectItemViewModel>()

    init {
        viewModel.projects.addListener(ListChangeListener { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    change.addedSubList.forEach { addTabAndSelect(it) }
                }
                if (change.wasRemoved()) {
                    change.removed.forEach { removeTab(it) }
                }
            }
        })

        viewModel.projects.forEach { addTabAndSelect(it) }
    }

    private fun addTabAndSelect(item: ProjectItemViewModel) {
        val tab = Tab().apply {
            textProperty().bind(item.nameProperty)
            content = item.editorNode
            setOnCloseRequest {
                it.consume()
                viewModel.closeProject(item)
            }
            setOnSelectionChanged {
                if (isSelected) {
                    viewModel.selectProject(item)
                }
            }
        }

        tabItems[tab] = item
        tabs.add(tab)
        selectionModel.select(tab)
    }

    private fun removeTab(item: ProjectItemViewModel) {
        val tab = tabItems.entries.firstOrNull { it.value == item }?.key ?: return
        tabItems.remove(tab)
        tabs.remove(tab)
    }
}
