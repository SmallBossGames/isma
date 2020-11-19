package views

import controllers.ActiveProjectController
import controllers.ProjectController
import events.NewProjectEvent
import tornadofx.*

class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()

    override val root = tabpane {
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.ismaProject
            tab(thisTabProject.name) {
                textarea(thisTabProject.projectTextProperty)

                selectionModel.select(this)
                activeProjectController.activeProject = thisTabProject

                textProperty().bindBidirectional(thisTabProject.nameProperty)


                setOnCloseRequest {
                    projectController.close(event.ismaProject)
                }

                setOnSelectionChanged {
                    if(this.isSelected){
                        activeProjectController.activeProject = event.ismaProject
                    }
                }
            }
        }
    }

    fun addIsmaTab() {
        val tab = IsmaProjectTab()
        tab.title = "fiewwfe"
        root.add(tab)
    }
}