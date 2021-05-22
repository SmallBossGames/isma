package views.editors.tabpane

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import javafx.scene.control.Tab
import models.projects.BlueprintProjectDataProvider
import models.projects.IProjectModel
import services.project.ProjectService
import tornadofx.View
import tornadofx.tab
import tornadofx.*
import views.editors.blueprint.IsmaBlueprintEditor
import views.editors.text.IsmaTextEditor


class IsmaEditorTabPane(
    private val projectController: ProjectService
): View() {
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