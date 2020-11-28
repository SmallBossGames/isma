package views

import controllers.ActiveProjectController
import controllers.LismaPdeController
import controllers.ProjectController
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.NewProjectEvent
import events.PasteTextInCurrentEditorEvent
import javafx.scene.text.FontWeight
import org.antlr.v4.runtime.Lexer
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import tornadofx.*
import java.util.*


class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()
    private val lismaPdeController: LismaPdeController by inject()

    override val root = tabpane {
        subscribe<NewProjectEvent> { event->
            val thisTabProject = event.ismaProject
            tab(thisTabProject.name) {
                val codeArea = CodeArea()

                codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
                codeArea.replaceText(thisTabProject.projectText)

                subscribe<CutTextInCurrentEditorEvent> { if (isSelected) codeArea.cut() }
                subscribe<CopyTextInCurrentEditorEvent> { if (isSelected) codeArea.copy() }
                subscribe<PasteTextInCurrentEditorEvent> { if (isSelected) codeArea.paste() }

                thisTabProject.projectTextProperty.bind(codeArea.textProperty())

                codeArea.textProperty().onChange {
                    highlightText(codeArea)
                }

                codeArea.stylesheet {
                    this.addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("test")))){
                        fontWeight = FontWeight.BOLD
                    })
                }

                selectionModel.select(this)
                activeProjectController.activeProject = thisTabProject

                textProperty().bind(thisTabProject.nameProperty)

                setOnCloseRequest {
                    projectController.close(event.ismaProject)
                }

                setOnSelectionChanged {
                    if(this.isSelected){
                        activeProjectController.activeProject = thisTabProject
                    }
                }

                add(codeArea)
            }
        }
    }

    private fun highlightText(codeArea: CodeArea){
        lismaPdeController.getLismaTokens().forEach {
            if(it.text == "for"){
                codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("test"))
            }
        }
    }
}