package views.editors.blueprint

import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment
import tornadofx.*

class IsmaBlueprintEditor: View() {
    private var activeSquare: Rectangle? = null
    private var xOffset = 0.0;
    private var yOffset = 0.0;

    override val root: Parent = borderpane {
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