package views

import controllers.ActiveProjectController
import controllers.LismaPdeController
import controllers.ProjectController
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.NewProjectEvent
import events.PasteTextInCurrentEditorEvent
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
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
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("keyword")))){
                        fill = Color.ORANGE
                        fontWeight = FontWeight.BOLD
                    })
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("default")))){
                        fill = Color.BLACK
                        fontWeight = FontWeight.NORMAL
                    })
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("decimal")))){
                        fill = Color.BLUE
                        fontWeight = FontWeight.NORMAL
                    })
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("comment")))){
                        fill = Color.GRAY
                        fontWeight = FontWeight.NORMAL
                        fontStyle = FontPosture.ITALIC
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
            when {
                it.text == "for" -> {
                    codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("keyword"))
                }
                it.text == "state" -> {
                    codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("keyword"))
                }
                it.text == "const" -> {
                    codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("keyword"))
                }
                it.type == LismaLexer.COMMENT -> {
                    codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("comment"))
                }
                it.type == LismaLexer.FloatingPointLiteral || it.type == LismaLexer.DecimalLiteral -> {
                    codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("decimal"))
                }
                else -> {
                    codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("default"))
                }
            }
        }
    }
}