package ru.isma.next.app.views.tabpane

import javafx.collections.SetChangeListener
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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

    init {
        projectController.projects.addListener { it: SetChangeListener.Change<out IProjectModel?> ->
            when (val addedElement = it.elementAdded) {
                is BlueprintProjectModel -> {
                    tabs.add(
                        Tab(addedElement.name, blueprintEditor.apply {
                            val provider = BlueprintProjectDataProvider(this)
                            addedElement.apply {
                                dataProvider = provider
                                pushBlueprint()
                            }
                        }).apply {
                            initProjectTab(addedElement)
                        }
                    )
                }
                is LismaProjectModel -> {
                    tabs.add(
                        Tab(addedElement.name, textEditor.apply {
                            replaceText(addedElement.lismaText)
                            addedElement.lismaTextProperty().bind(textProperty())
                        }).apply {
                            initProjectTab(addedElement)
                        }
                    )
                }
                null -> {
                }
            }
        }
    }

    private fun Tab.initProjectTab(project: IProjectModel) {
        selectionModel.select(this)
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
}