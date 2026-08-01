package ru.isma.next.app.views.tabpane

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.isma.javafx.extensions.coroutines.flow.addedAsFlow
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.services.project.IProjectService


class IsmaEditorTabPane(
    private val projectController: IProjectService,
): TabPane(), KoinComponent, AutoCloseable {
    private val _coroutinesScope = CoroutineScope(Dispatchers.JavaFx)
    private var _collectJob: Job? = null

    init {
        _collectJob = _coroutinesScope.launch {
            // Initial emission of all existing projects, then stream of new additions
            merge(
                projectController.projects.asIterable().asFlow(),
                projectController.projects.addedAsFlow()
            ).cancellable().collect {
                addTabAndSelect(
                    Tab(it.name, it.editor).apply {
                        initProjectTab(it)
                    }
                )
            }
        }
    }

    override fun close() {
        _collectJob?.cancel()
        _coroutinesScope.coroutineContext.cancel()
    }

    private fun Tab.initProjectTab(project: IProjectModel) {
        projectController.activeProject = project

        textProperty().bind(project.nameProperty())

        setOnCloseRequest {
            projectController.close(project)
        }

        setOnSelectionChanged {
            if(this.isSelected){
                projectController.activeProject = project
            }
        }
    }

    private fun TabPane.addTabAndSelect(tab: Tab) {
        tabs.add(tab)
        selectionModel.select(tab)
    }
}