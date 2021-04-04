package views.editors.blueprint

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment
import tornadofx.*
import views.editors.blueprint.controls.StateBox
import views.editors.blueprint.controls.StateTransactionArrow

class IsmaBlueprintEditor: Fragment() {
    private val isRemoveStateModeProperty = SimpleBooleanProperty(false)
    private val isRemoveTransactionModeProperty = SimpleBooleanProperty(false)
    private val isAddTransactionModeProperty = SimpleBooleanProperty(false)
    private val addTransactionStateCounterProperty = SimpleIntegerProperty(0)

    private var activeSquare: Rectangle? = null
    private var activeStateBox: StateBox? = null
    private var statesToLink = arrayOf<StateBox?>(null, null)

    private var xOffset = 0.0;
    private var yOffset = 0.0;

    private fun isRemoveStateModeProperty() = isRemoveStateModeProperty
    private fun isRemoveTransactionModeProperty() = isRemoveTransactionModeProperty
    private fun isAddTransactionModeProperty() = isAddTransactionModeProperty
    private fun addTransactionStateCounterProperty() = addTransactionStateCounterProperty

    private var isRemoveStateMode by isRemoveStateModeProperty
    private var isRemoveTransactionMode by isRemoveTransactionModeProperty
    private var isAddTransactionMode by isAddTransactionModeProperty
    private var addTransactionStateCounter by addTransactionStateCounterProperty

    private val canvas = pane {

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

        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            if(activeSquare != null) {
                activeSquare!!.x = it.x + xOffset
                activeSquare!!.y = it.y + yOffset
            }

            if(it.isPrimaryButtonDown && activeStateBox != null && !isRemoveStateMode) {
                moveStateBox(activeStateBox!!, it.x + xOffset, it.y + yOffset)
            }
        }
    }

    override val root: Parent = borderpane {
        center = tabpane {
            tab ("Blueprint") {
                add(canvas)
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
                action {
                    resetEditorMode()
                    createNewState()
                }

                text = "New state"

                disableClose()
            }
            button {
                action {
                    if(isAddTransactionMode) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        isAddTransactionMode = true
                        addTransactionStateCounter = 0
                    }
                }

                text = "New transition"

                isAddTransactionModeProperty().onChange {
                    text = if(it) {
                        "Stop adding transaction"
                    } else {
                        "New transition"
                    }
                }
            }
            separator()
            button {
                action {
                    if (isRemoveStateMode) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        isRemoveStateMode = true
                    }
                }

                text = "Remove state"

                isRemoveStateModeProperty().onChange {
                    text = if(it){
                        "Stop remove state"
                    } else {
                        "Remove state"
                    }
                }
            }
            button {
                action {
                    if (isRemoveTransactionMode) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        isRemoveTransactionMode = true
                    }
                }
                text = "Remove transition"

                isRemoveTransactionModeProperty().onChange {
                    text = if(it){
                        "Stop remove transition"
                    } else {
                        "Remove transition"
                    }
                }
            }
        }
    }

    private fun moveStateBox(stateBox: StateBox, positionX: Double, positionY: Double) {
        stateBox.translateXProperty().value = positionX
        stateBox.translateYProperty().value = positionY
    }

    private fun createNewState(){
        val stateBox = find<StateBox> {
            addMousePressedListener { it, event ->
                if(isRemoveStateMode){
                    it.removeFromParent()
                    return@addMousePressedListener;
                }

                xOffset = -event.x
                yOffset = -event.y
                activeStateBox = it
            }
            addMouseReleasedListener { _, _ ->
                activeStateBox = null
            }
            addMouseClickedListeners { it, _ ->
                if(isAddTransactionMode) {
                    statesToLink[addTransactionStateCounter++] = it

                    if (addTransactionStateCounter > 1) {
                        val arrow = find<StateTransactionArrow> {
                            startXProperty().bind(statesToLink[0]!!.centerXProperty())
                            startYProperty().bind(statesToLink[0]!!.centerYProperty())
                            endXProperty().bind(statesToLink[1]!!.centerXProperty())
                            endYProperty().bind(statesToLink[1]!!.centerYProperty())
                            addMousePressedListener { it, _ ->
                                if(isRemoveTransactionMode) {
                                    it.removeFromParent()
                                }
                            }
                        }
                        isAddTransactionMode = false
                        canvas.add(arrow)
                    }

                    return@addMouseClickedListeners;
                }
            }
            isEditableProperty().bind((isRemoveStateModeProperty).or(isAddTransactionModeProperty).not())
        }
        canvas.add(stateBox)
    }

    private fun resetEditorMode() {
        isRemoveStateMode = false
        isRemoveTransactionMode = false
        isAddTransactionMode = false
    }
}