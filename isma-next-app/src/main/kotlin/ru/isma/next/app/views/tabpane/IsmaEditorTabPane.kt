package ru.isma.next.app.views.tabpane

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.isma.javafx.extensions.coroutines.flow.addedAsFlow
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.text.IsmaTextEditor


class IsmaEditorTabPane(
    private val projectController: ProjectService,
): TabPane(), KoinComponent {
    private val textEditor : IsmaTextEditor get() = get()

    private val blueprintEditor : IsmaBlueprintEditor get() = get()

    private val coroutinesScope = CoroutineScope(Dispatchers.JavaFx)

    init {
        coroutinesScope.launch {
            merge(projectController.projects.asFlow(), projectController.projects.addedAsFlow())
                .cancellable()
                .collect {
                    val tab = when (it) {
                        is BlueprintProjectModel -> {
                            Tab(it.name, blueprintEditor.apply {
                                val provider = BlueprintProjectDataProvider(this)
                                it.apply {
                                    dataProvider = provider
                                    pushBlueprint()
                                }
                            }).apply {
                                initProjectTab(it)
                            }
                        }
                        is LismaProjectModel -> {
                            Tab(it.name, textEditor.apply {
                                replaceText(it.lismaText)
                                it.lismaTextProperty().bind(textProperty())
                            }).apply {
                                initProjectTab(it)
                            }
                        }
                        else -> {
                            throw IllegalArgumentException()
                        }
                    }

                    addTabAndSelect(tab)
                }
        }
    }

    fun dispose() {
        coroutinesScope.cancel()
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