package views

import controllers.ActiveProjectController
import controllers.LismaPdeController
import controllers.ProjectController
import controllers.SyntaxHighlightingController
import events.*
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.*

import javafx.scene.input.MouseEvent
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment
import models.IsmaProjectModel
import views.editors.blueprint.IsmaBlueprintEditor
import views.editors.text.IsmaTextEditor


class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()
    private val ismaCodeArea: IsmaTextEditor by inject()
    private val ismaBluprintEditor: IsmaBlueprintEditor by inject()



    override val root = tabpane {
        subscribe<NewBlueprintProjectEvent> {
            tab("The canvas project") {
                add(ismaBluprintEditor)
            }
        }
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.ismaProject

            tab(thisTabProject.name) {
                add(ismaCodeArea.apply {
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