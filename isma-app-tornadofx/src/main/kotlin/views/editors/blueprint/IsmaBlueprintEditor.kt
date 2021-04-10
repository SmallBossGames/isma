package views.editors.blueprint

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import tornadofx.*
import views.editors.blueprint.controls.StateBox
import views.editors.blueprint.controls.StateTransactionArrow
import views.editors.blueprint.models.LismaStateModel
import views.editors.text.IsmaTextEditor

class IsmaBlueprintEditor: Fragment() {
    private val isRemoveStateModeProperty = SimpleBooleanProperty(false)
    private val isRemoveTransactionModeProperty = SimpleBooleanProperty(false)
    private val isAddTransactionModeProperty = SimpleBooleanProperty(false)
    private val addTransactionStateCounterProperty = SimpleIntegerProperty(0)
    private val mainProjectTextProperty = SimpleStringProperty("")

    private var activeStateBox: StateBox? = null
    private var statesToLink = arrayOf<StateBox?>(null, null)

    private var xOffset = 0.0;
    private var yOffset = 0.0;

    private fun isRemoveStateModeProperty() = isRemoveStateModeProperty
    private fun isRemoveTransactionModeProperty() = isRemoveTransactionModeProperty
    private fun isAddTransactionModeProperty() = isAddTransactionModeProperty
    private fun addTransactionStateCounterProperty() = addTransactionStateCounterProperty
    private fun mainProjectTextProperty() = mainProjectTextProperty

    private var isRemoveStateMode by isRemoveStateModeProperty
    private var isRemoveTransactionMode by isRemoveTransactionModeProperty
    private var isAddTransactionMode by isAddTransactionModeProperty
    private var addTransactionStateCounter by addTransactionStateCounterProperty
    private val mainProjectText by mainProjectTextProperty

    private val canvas = pane {
        addMainAndInitBoxes()

        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            if(it.isPrimaryButtonDown && activeStateBox != null && !isRemoveStateMode) {
                moveStateBox(activeStateBox!!, it.x + xOffset, it.y + yOffset)
            }
        }
    }

    private val tabs = tabpane {
        tab ("Blueprint") {
            add(canvas)
        }
    }

    override val root = borderpane {
        center = tabs
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
        canvas.add<StateBox> {
            color = Color.CORAL

            val stateModel = LismaStateModel()
            stateModel.titleProperty().bind(boxNameProperty())
            addEditActionListener {
                openStateTextEditorTab(stateModel)
            }
            initMouseMovingEvents()
            initMouseRemoveStateEvents()
            initMouseLinkTransactionEvents()
            isEditableProperty().bind((isRemoveStateModeProperty).or(isAddTransactionModeProperty).not())
        }
    }

    private fun resetEditorMode() {
        isRemoveStateMode = false
        isRemoveTransactionMode = false
        isAddTransactionMode = false
    }

    private fun openStateTextEditorTab(state: LismaStateModel) {
        tabs.tab(state.title) {
            add<IsmaTextEditor> {
                replaceText(state.text)
                state.textProperty().bind(textProperty())
            }
            textProperty().bind(state.titleProperty())
        }
    }

    private fun openMainTextEditorTab() {
        tabs.tab("Main") {
            add<IsmaTextEditor> {
                replaceText(mainProjectText)
                mainProjectTextProperty().bind(textProperty())
            }
        }
    }

    private fun Pane.addMainAndInitBoxes() {
        add<StateBox> {
            color = Color.LIGHTGREEN
            isEditable = false
            squareHeight = 60.0
            boxName = "Main"
            translateXProperty() += 10
            translateYProperty() += 10

            addEditActionListener { openMainTextEditorTab() }

            initMouseMovingEvents()
        }

        add<StateBox> {
            color = Color.LIGHTBLUE
            isEditButtonVisible = false
            isEditable = false
            squareHeight = 60.0
            boxName = "Init"
            translateXProperty() += 10
            translateYProperty() += 100

            initMouseMovingEvents()
            initMouseLinkTransactionEvents()
        }
    }

    private fun StateBox.initMouseRemoveStateEvents() {
        addMousePressedListener { it, event ->
            if(!isRemoveStateMode){
                return@addMousePressedListener
            }
            it.removeFromParent()
        }
    }

    private fun StateTransactionArrow.initMouseRemoveTransactionEvents() {
        addMousePressedListener { it, event ->
            if(!isRemoveTransactionMode){
                return@addMousePressedListener
            }
            it.removeFromParent()
        }
    }

    private fun StateBox.initMouseMovingEvents() {
        addMousePressedListener { it, event ->
            if(isRemoveStateMode || isAddTransactionMode){
                return@addMousePressedListener
            }

            xOffset = -event.x
            yOffset = -event.y
            activeStateBox = it
        }
        addMouseReleasedListener { _, _ ->
            activeStateBox = null
        }
    }

    private fun StateBox.initMouseLinkTransactionEvents() {
        addMouseClickedListeners { it, _ ->
            if (!isAddTransactionMode || isRemoveStateMode) {
                return@addMouseClickedListeners
            }

            statesToLink[addTransactionStateCounter++] = it

            if (addTransactionStateCounter > 1) {
                canvas.add<StateTransactionArrow> {
                    startXProperty().bind(statesToLink[0]!!.centerXProperty())
                    startYProperty().bind(statesToLink[0]!!.centerYProperty())
                    endXProperty().bind(statesToLink[1]!!.centerXProperty())
                    endYProperty().bind(statesToLink[1]!!.centerYProperty())

                    initMouseRemoveTransactionEvents()
                }
                isAddTransactionMode = false
            }

            return@addMouseClickedListeners
        }
    }
}