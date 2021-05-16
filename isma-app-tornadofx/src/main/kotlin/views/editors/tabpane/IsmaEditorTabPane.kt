package views.editors.tabpane

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import javafx.scene.control.Tab
import models.projects.BlueprintProjectDataProvider
import models.projects.IProjectModel
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