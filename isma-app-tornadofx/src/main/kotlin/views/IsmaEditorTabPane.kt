package views

import controllers.ActiveProjectController
import controllers.LismaPdeController
import controllers.ProjectController
import controllers.SyntaxHighlightingController
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.NewProjectEvent
import events.PasteTextInCurrentEditorEvent
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpansBuilder
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import tornadofx.*
import java.util.*


class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()
    private val lismaPdeController: LismaPdeController by inject()
    private val highlightingController: SyntaxHighlightingController by inject()

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
                    val highlighting = highlightingController.createHighlightingStyleSpans(it ?: "")
                    codeArea.setStyleSpans(0, highlighting)
                }

                codeArea.stylesheet {
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("keyword")))) {
                        fill = Color.ORANGE
                        fontWeight = FontWeight.BOLD
                    })
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("default")))) {
                        fill = Color.BLACK
                        fontWeight = FontWeight.NORMAL
                    })
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("decimal")))) {
                        fill = Color.BLUE
                        fontWeight = FontWeight.NORMAL
                    })
                    addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("comment")))) {
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
}