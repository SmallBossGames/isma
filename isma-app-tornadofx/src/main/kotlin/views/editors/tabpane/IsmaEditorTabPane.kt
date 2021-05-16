package views.editors.tabpane

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import models.projects.BlueprintProjectDataProvider
import org.koin.core.component.KoinComponent
import services.ProjectService
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane
import views.editors.blueprint.IsmaBlueprintEditor
import views.editors.text.IsmaTextEditor
import org.koin.core.component.inject as koinInject


class IsmaEditorTabPane: View(), KoinComponent {
    private val projectController: ProjectService by koinInject()

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
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.lismaProject

            tab(thisTabProject.name) {
                add<IsmaTextEditor> {
                    replaceText(thisTabProject.lismaText)
                    thisTabProject.lismaTextProperty().bind(textProperty())
                }

                selectionModel.select(this)
                projectController.activeProject = thisTabProject

                textProperty().bind(thisTabProject.nameProperty())

                setOnCloseRequest {
                    projectController.close(event.lismaProject)
                }

                setOnSelectionChanged {
                    if(this.isSelected){
                        projectController.activeProject = thisTabProject
                    }
                }
            }
        }
    }
}