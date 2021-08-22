package ru.isma.next.app.views.tabpane

import javafx.collections.SetChangeListener
import javafx.scene.control.Tab
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.text.IsmaTextEditor
import tornadofx.View
import tornadofx.stylesheet
import tornadofx.tab
import tornadofx.tabpane


class IsmaEditorTabPane: View(), KoinComponent {
    private val projectController: ProjectService by di()

    private fun getTextEditor() : IsmaTextEditor = get()

    override val root = tabpane {
        stylesheet {  }

        projectController.projects.addListener { it: SetChangeListener.Change<out IProjectModel?> ->
            when (val addedElement = it.elementAdded) {
                is BlueprintProjectModel -> {
                    tab(addedElement.name) {
                        add<IsmaBlueprintEditor> {
                            val provider = BlueprintProjectDataProvider(this@add)
                            addedElement.apply {
                                dataProvider = provider
                                pushBlueprint()
                            }
                        }
                        initProjectTab(addedElement)
                    }
                }
                is LismaProjectModel -> {
                    tab(addedElement.name) {
                        val editor = getTextEditor().apply {
                            replaceText(addedElement.lismaText)
                            addedElement.lismaTextProperty().bind(textProperty())
                        }

                        add(editor)

                        initProjectTab(addedElement)
                    }
                }
                null -> {
                }
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