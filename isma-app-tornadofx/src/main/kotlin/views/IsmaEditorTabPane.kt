package views

import controllers.ActiveProjectController
import controllers.ProjectController
import env.project.IsmaProject
import events.NewProjectEvent
import models.IsmaProjectModel
import tornadofx.*

class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()

    override val root = tabpane {
        subscribe<NewProjectEvent> { event->
            tab(event.ismaProject.name){
                textarea(event.ismaProject.projectText)
                selectionModel.select(this)
                activeProjectController.activeProject = event.ismaProject

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