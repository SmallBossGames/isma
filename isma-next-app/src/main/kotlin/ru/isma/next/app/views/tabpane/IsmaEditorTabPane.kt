package ru.isma.next.app.views.tabpane

import ru.isma.next.app.events.project.NewBlueprintProjectEvent
import ru.isma.next.app.events.project.NewProjectEvent
import javafx.scene.control.Tab
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.app.services.project.ProjectService
import tornadofx.View
import tornadofx.tab
import tornadofx.*


class IsmaEditorTabPane: View() {
    private val projectController: ProjectService by di()

    private val ismaTextEditor: IsmaTextEditor by di()

    override val root = tabpane {
        subscribe<NewBlueprintProjectEvent> { event ->
            val project = event.blueprintProject
            tab(project.name) {
                add<IsmaBlueprintEditor> {
                    val provider = BlueprintProjectDataProvider(this@add)
                    project.apply {
                        dataProvider = provider
                        pushBlueprint()
                    }
                }

                initProjectTab(project)
            }
        }
        subscribe<NewProjectEvent> { event->
            val project = event.lismaProject

            tab(project.name) {
                add<IsmaTextEditor> {
                    replaceText(project.lismaText)
                    project.lismaTextProperty().bind(textProperty())
                }

                initProjectTab(project)
            }
        }
    }

    private fun Tab.initProjectTab(project: IProjectModel) {
        tabPane.selectionModel.select(this)
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