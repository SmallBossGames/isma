package views

import controllers.ActiveProjectController
import controllers.ProjectController
import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane
import views.editors.blueprint.IsmaBlueprintEditor
import views.editors.text.IsmaTextEditor


class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()

    override val root = tabpane {
        subscribe<NewBlueprintProjectEvent> {
            tab("The canvas project") {
                add(find<IsmaBlueprintEditor>())
            }
        }
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.ismaProject

            tab(thisTabProject.name) {
                add(find<IsmaTextEditor> {
                    isSelectedProperty().bind(this@tab.selectedProperty())
                    replaceText(thisTabProject.projectText)
                    thisTabProject.projectTextProperty().bind(textProperty())}
                )

                selectionModel.select(this)
                activeProjectController.activeProject = thisTabProject

                textProperty().bind(thisTabProject.nameProperty())

                setOnCloseRequest {
                    projectController.close(event.ismaProject)
                }

                setOnSelectionChanged {
                    if(this.isSelected){
                        activeProjectController.activeProject = thisTabProject
                    }
                }
            }
        }
    }
}