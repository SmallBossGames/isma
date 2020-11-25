package views

import controllers.ActiveProjectController
import controllers.ProjectController
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.NewProjectEvent
import events.PasteTextInCurrentEditorEvent
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.*
import java.text.MessageFormat




class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()

    override val root = tabpane {
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.ismaProject
            tab(thisTabProject.name) {
                val codeArea = CodeArea()

                codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea);
                codeArea.replaceText(thisTabProject.projectText)

                subscribe<CutTextInCurrentEditorEvent> { if (isSelected) codeArea.cut() }
                subscribe<CopyTextInCurrentEditorEvent> { if (isSelected) codeArea.copy() }
                subscribe<PasteTextInCurrentEditorEvent> { if (isSelected) codeArea.paste() }

                thisTabProject.projectTextProperty.bind(codeArea.textProperty())

                selectionModel.select(this)
                activeProjectController.activeProject = thisTabProject

                textProperty().bind(thisTabProject.nameProperty)

                setOnCloseRequest {
                    projectController.close(event.ismaProject)
                }

                setOnSelectionChanged {
                    if(this.isSelected){
                        activeProjectController.activeProject = event.ismaProject
                    }
                }

                add(codeArea)
            }
        }
    }

    fun addIsmaTab() {
        val tab = IsmaProjectTab()
        tab.title = "fiewwfe"
        root.add(tab)
    }
}