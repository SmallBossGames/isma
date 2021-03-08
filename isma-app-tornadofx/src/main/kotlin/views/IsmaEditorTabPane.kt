package views

import controllers.ActiveProjectController
import controllers.LismaPdeController
import controllers.ProjectController
import controllers.SyntaxHighlightingController
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.NewBlueprintProjectEvent
import events.PasteTextInCurrentEditorEvent
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


class IsmaEditorTabPane: View() {
    private val projectController: ProjectController by inject()
    private val activeProjectController: ActiveProjectController by inject()
    private val lismaPdeController: LismaPdeController by inject()
    private val highlightingController: SyntaxHighlightingController by inject()

    private var activeSquare: Rectangle? = null
    private var xOffset = 0.0;
    private var yOffset = 0.0;

    private val textOffsetYProperty = SimpleDoubleProperty(75.0)


    override val root = tabpane {
        tab("The canvas project") {
            borderpane {
                center = tabpane {
                    tab ("Blueprint") {
                        pane {
                            val r1 = rectangle {
                                viewOrder = 3.0
                                fill = Color.CORAL
                                width = 200.0
                                height = 100.0
                                arcWidth = 20.0
                                arcHeight = 20.0

                                addEventHandler(MouseEvent.MOUSE_PRESSED) {
                                    xOffset = this.x - it.x
                                    yOffset = this.y - it.y
                                    activeSquare = this
                                    print("I'm here")
                                }
                                addEventHandler(MouseEvent.MOUSE_RELEASED) {
                                    activeSquare = null
                                    print("I'm here too")
                                }
                            }
                            val r2 = rectangle {
                                viewOrder = 3.0
                                fill = Color.CORAL
                                width = 200.0
                                height = 100.0
                                arcWidth = 20.0
                                arcHeight = 20.0

                                addEventHandler(MouseEvent.MOUSE_PRESSED) {
                                    xOffset = this.x - it.x
                                    yOffset = this.y - it.y
                                    activeSquare = this
                                    print("I'm here")
                                }
                                addEventHandler(MouseEvent.MOUSE_RELEASED) {
                                    activeSquare = null
                                    print("I'm here too")
                                }
                            }

                            val r3 = rectangle {
                                viewOrder = 3.0
                                fill = Color.LIGHTGREEN
                                width = 150.0
                                height = 50.0
                                arcWidth = 20.0
                                arcHeight = 20.0

                                addEventHandler(MouseEvent.MOUSE_PRESSED) {
                                    xOffset = this.x - it.x
                                    yOffset = this.y - it.y
                                    activeSquare = this
                                    print("I'm here")
                                }
                                addEventHandler(MouseEvent.MOUSE_RELEASED) {
                                    activeSquare = null
                                    print("I'm here too")
                                }
                            }

                            val inintR = rectangle {
                                viewOrder = 3.0
                                fill = Color.LIGHTBLUE
                                width = 150.0
                                height = 50.0
                                arcWidth = 20.0
                                arcHeight = 20.0

                                addEventHandler(MouseEvent.MOUSE_PRESSED) {
                                    xOffset = this.x - it.x
                                    yOffset = this.y - it.y
                                    activeSquare = this
                                    print("I'm here")
                                }
                                addEventHandler(MouseEvent.MOUSE_RELEASED) {
                                    activeSquare = null
                                    print("I'm here too")
                                }
                            }

                            val lineBetweenStates = line {
                                viewOrder = 4.0
                                startXProperty().bind(r1.xProperty() + r1.widthProperty() / 2)
                                startYProperty().bind(r1.yProperty() + r1.heightProperty() / 2)
                                endXProperty().bind(r2.xProperty() + r2.widthProperty() / 2)
                                endYProperty().bind(r2.yProperty() + r2.heightProperty() / 2)
                            }

                            val lineBetweenInitAndState = line {
                                viewOrder = 4.0
                                startXProperty().bind(inintR.xProperty() + inintR.widthProperty() / 2)
                                startYProperty().bind(inintR.yProperty() + inintR.heightProperty() / 2)
                                endXProperty().bind(r1.xProperty() + r1.widthProperty() / 2)
                                endYProperty().bind(r1.yProperty() + r1.heightProperty() / 2)
                            }

                            val t1 = text {
                                textAlignment = TextAlignment.CENTER
                                xProperty().bind(r1.xProperty() + 60.0)
                                yProperty().bind(r1.yProperty() + 50.0)
                                text = "SomeStateName"
                            }

                            val t2 = text {
                                textAlignment = TextAlignment.CENTER
                                xProperty().bind(r2.xProperty() + 50.0)
                                yProperty().bind(r2.yProperty() + 50.0)
                                text = "SomeStateNameToo"
                            }

                            val r3Text = text {
                                textAlignment = TextAlignment.CENTER
                                xProperty().bind(r3.xProperty() + 20.0)
                                yProperty().bind(r3.yProperty() + 30.0)
                                text = "Main"
                            }

                            val inintRText = text {
                                textAlignment = TextAlignment.CENTER
                                xProperty().bind(inintR.xProperty() + 20.0)
                                yProperty().bind(inintR.yProperty() + 30.0)
                                text = "Init"
                            }

                            val transaction1 = text {
                                textAlignment = TextAlignment.JUSTIFY
                                xProperty().bind((lineBetweenStates.startXProperty() - lineBetweenStates.endXProperty()) / 2.0 + lineBetweenStates.endXProperty())
                                yProperty().bind((lineBetweenStates.startYProperty() - lineBetweenStates.endYProperty()) / 2.0 + lineBetweenStates.endYProperty() - 20.0)
                                text = "TIME > TIME1"
                            }

                            val transaction2 = text {
                                textAlignment = TextAlignment.JUSTIFY
                                xProperty().bind((lineBetweenStates.startXProperty() - lineBetweenStates.endXProperty()) / 2.0 + lineBetweenStates.endXProperty())
                                yProperty().bind((lineBetweenStates.startYProperty() - lineBetweenStates.endYProperty()) / 2.0 + lineBetweenStates.endYProperty() + 20.0)
                                text = "TIME < TIME3"
                            }

                            addEventHandler(MouseEvent.MOUSE_DRAGGED) {
                                if(activeSquare != null) {
                                    activeSquare!!.x = it.x + xOffset
                                    activeSquare!!.y = it.y + yOffset
                                }

                                //r2.x = it.x - r2.width / 2
                                //r2.y = it.y - r2.height / 2
                            }
                        }
                    }
                    tab ("Main") {

                    }
                    tab ("State 1") {

                    }
                    tab ("State 2") {

                    }
                }
                bottom = toolbar {
                    button {
                        text = "New state"
                    }
                    button {
                        text = "New transition"
                    }
                    separator()
                    button {
                        text = "Remove state"
                    }
                    button {
                        text = "Remove transition"
                    }
                }
            }

        }
        subscribe<NewBlueprintProjectEvent> { event->
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