package views.editors.tabpane

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
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
        subscribe<NewBlueprintProjectEvent> {
            tab("The canvas project") {
                add(find<IsmaBlueprintEditor>())
            }
        }
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.ismaProject

            tab(thisTabProject.name) {
                add<IsmaTextEditor> {
                    replaceText(thisTabProject.projectText)
                    thisTabProject.projectTextProperty().bind(textProperty())
                }

                selectionModel.select(this)
                projectController.activeProject = thisTabProject

                textProperty().bind(thisTabProject.nameProperty())

                setOnCloseRequest {
                    projectController.close(event.ismaProject)
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