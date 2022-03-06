package ru.isma.next.editor.blueprint

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.effect.DropShadow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import ru.isma.next.editor.blueprint.constants.INIT_STATE
import ru.isma.next.editor.blueprint.constants.MAIN_STATE
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.StateTransactionArrow
import ru.isma.next.editor.blueprint.models.BlueprintEditorTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue
import kotlin.math.max

class IsmaBlueprintEditor(private val editorFactory: ITextEditorFactory): BorderPane() {
    private val nameChangingMonitor = NameChangingMonitor("New state")

    private val isRemoveStateModeProperty = SimpleBooleanProperty(false)
    private val isRemoveTransactionModeProperty = SimpleBooleanProperty(false)
    private val isAddTransactionModeProperty = SimpleBooleanProperty(false)
    private val addTransactionStateCounterProperty = SimpleIntegerProperty(0)

    private var activeStateBox: StateBox? = null
    private var statesToLink = arrayOf<StateBox?>(null, null)

    private var xOffset = 0.0
    private var yOffset = 0.0

    private fun isRemoveStateModeProperty() = isRemoveStateModeProperty
    private fun isRemoveTransactionModeProperty() = isRemoveTransactionModeProperty
    private fun isAddTransactionModeProperty() = isAddTransactionModeProperty
    private fun addTransactionStateCounterProperty() = addTransactionStateCounterProperty

    private var isRemoveStateMode by isRemoveStateModeProperty
    private var isRemoveTransactionMode by isRemoveTransactionModeProperty
    private var isAddTransactionMode by isAddTransactionModeProperty
    private var addTransactionStateCounter by addTransactionStateCounterProperty

    private val transactions = ArrayList<BlueprintEditorTransactionModel>()
    private val stateBoxes = ArrayList<StateBox>()
    private val mainStateBox: StateBox = createMainStateBox()
    private val initStateBox: StateBox = createInitStateBox()

    private val canvas = Pane().apply {
        addMainStateBox()
        addInitStateBox()

        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            if(it.isPrimaryButtonDown && activeStateBox != null && !isRemoveStateMode) {
                moveStateBox(activeStateBox!!, it.x + xOffset, it.y + yOffset)
            }
        }
    }

    private val tabs = TabPane(
        Tab("Blueprint", ScrollPane(canvas)).apply {
            isClosable = false
        }
    )

    init {
        center = tabs
        bottom = ToolBar(
            Button("New state").apply {
                onAction = EventHandler {
                    resetEditorMode()
                    addStateBox()
                }
            },
            Button("New transition").apply {
                onAction = EventHandler {
                    if(isAddTransactionMode) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        isAddTransactionMode = true
                        addTransactionStateCounter = 0
                    }
                }

                isAddTransactionModeProperty().addListener { _, _, value ->
                    text = if(value) {
                        "Stop adding transaction"
                    } else {
                        "New transition"
                    }
                }
            },
            Separator(),
            Button("Remove state").apply {
                onAction = EventHandler {
                    if (isRemoveStateMode) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        isRemoveStateMode = true
                    }
                }

                text = "Remove state"

                isRemoveStateModeProperty().addListener { _, _, value ->
                    text = if(value){
                        "Stop remove state"
                    } else {
                        "Remove state"
                    }
                }
            },

            Button("Remove transition").apply {
                onAction = EventHandler {
                    if (isRemoveTransactionMode) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        isRemoveTransactionMode = true
                    }
                }

                isRemoveTransactionModeProperty().addListener { _, _, value ->
                    text = if(value){
                        "Stop remove transition"
                    } else {
                        "Remove transition"
                    }
                }
            }
        )
    }

    fun getBlueprintModel() : BlueprintModel {
        val main = mainStateBox.toBlueprintState()
        val init = initStateBox.toBlueprintState()
        val states = stateBoxes.map { it.toBlueprintState() }.toTypedArray()

        val blueprintTransactions = transactions
            .map {
                BlueprintTransactionModel(
                    startStateName = it.startStateBox.name,
                    endStateName = it.endStateBox.name,
                    predicate = it.transactionArrow.text
                )
            }
            .toTypedArray()

        return BlueprintModel(main, init, states, blueprintTransactions)
    }

    fun setBlueprintModel(model: BlueprintModel) {
        stateBoxes.toTypedArray().forEach { it.removeFromEditor() }

        mainStateBox.applyBlueprintState(model.main)
        initStateBox.applyBlueprintState(model.init)

        val stateBoxes = model.states.associateByTo(
            mutableMapOf(
                initStateBox.name to initStateBox,
                mainStateBox.name to mainStateBox
            ),
            { it.name },
            {instantiateStateBoxFromBlueprintState(it)}
        )

        model.transactions.forEach {
            addTransactionArrow(stateBoxes[it.startStateName]!!, stateBoxes[it.endStateName]!!, it.predicate)
        }
    }

    private fun StateBox.toBlueprintState() : BlueprintStateModel {
        return BlueprintStateModel(
            this.layoutXProperty().value,
            this.layoutYProperty().value,
            this.name,
            this.text
        )
    }

    private fun StateBox.applyBlueprintState(blueprintState: BlueprintStateModel){
        this.apply {
            layoutXProperty().value = blueprintState.canvasPositionX
            layoutYProperty().value = blueprintState.canvasPositionY
            name = blueprintState.name
            text = blueprintState.text
        }
    }

    private fun instantiateStateBoxFromBlueprintState(blueprintState: BlueprintStateModel) : StateBox {
        return instantiateStateBox(
            positionX = blueprintState.canvasPositionX,
            positionY = blueprintState.canvasPositionY,
            stateName = blueprintState.name,
            stateText = blueprintState.text,
        )
    }

    private fun instantiateStateBox(
        positionX: Double = 0.0,
        positionY: Double = 0.0,
        stateName: String = "",
        stateText: String = "",
    ) : StateBox {
        val stateBox = StateBox().apply {
            color = Color.CORAL

            addEditActionListener {
                openStateTextEditorTab(this)
            }

            initMouseMovingEvents()
            initMouseRemoveStateEvents()
            initMouseLinkTransactionEvents()
            initNameChangingEvent()

            layoutXProperty().value = positionX
            layoutYProperty().value = positionY
            name = stateName.ifEmpty { nameChangingMonitor.createNextDefaultName() }
            text = stateText

            nameChangingMonitor.tryRegister(name)

            isEditableProperty().bind((isRemoveStateModeProperty).or(isAddTransactionModeProperty).not())
        }

        stateBoxes.add(stateBox)
        canvas.children.add(stateBox)

        return stateBox
    }

    private fun addStateBox() {
        instantiateStateBox(10.0, 200.0)
    }

    private fun addTransactionArrow(startStateBox: StateBox, endStateBox: StateBox, predicate: String = "") {
        if(transactions.any { it.startStateBox == startStateBox && it.endStateBox == endStateBox }) {
            return
        }

        val transactionArrow = StateTransactionArrow().apply {
            startXProperty().bind(startStateBox.centerXProperty())
            startYProperty().bind(startStateBox.centerYProperty())
            endXProperty().bind(endStateBox.centerXProperty())
            endYProperty().bind(endStateBox.centerYProperty())

            text = predicate

            initMouseRemoveTransactionEvents()

            setShowPopup { node, x, y ->
                val converted = canvas.sceneToLocal(x, y)

                val popover = createEditPopOver(node, converted.x, converted.y)

                canvas.children.add(popover)
            }
        }

        canvas.children.add(transactionArrow)

        transactions.add(BlueprintEditorTransactionModel(startStateBox, endStateBox, transactionArrow))


    }

    private fun StateBox.removeFromEditor() {
        transactions
            .toTypedArray()
            .filter { it.startStateBox == this || it.endStateBox == this }
            .forEach { removeTransaction(it) }

        stateBoxes.remove(this)
        canvas.children.remove(this)
    }

    private fun StateTransactionArrow.removeFromEditor() {
        transactions
            .toTypedArray()
            .filter { it.transactionArrow == this }
            .forEach { removeTransaction(it) }
    }

    private fun removeTransaction(transaction: BlueprintEditorTransactionModel) {
        transactions.remove(transaction)

        canvas.children.remove(transaction.transactionArrow)
    }

    private fun createMainStateBox() : StateBox {
        return StateBox().apply {
            color = Color.LIGHTGREEN
            isEditable = false
            squareHeight = 60.0
            name = MAIN_STATE
            layoutXProperty().value += 10
            layoutXProperty().value += 10

            nameChangingMonitor.tryRegister(name)

            addEditActionListener { openMainTextEditorTab() }

            initMouseMovingEvents()
        }
    }

    private fun createInitStateBox() : StateBox {
        return StateBox().apply {
            color = Color.LIGHTBLUE
            isEditButtonVisible = false
            isEditable = false
            squareHeight = 60.0
            name = INIT_STATE
            layoutXProperty().value += 10
            layoutYProperty().value += 100

            nameChangingMonitor.tryRegister(name)

            initMouseMovingEvents()
            initMouseLinkTransactionEvents()
        }
    }

    private fun Pane.addMainStateBox() {
        children.add(mainStateBox)
    }

    private fun Pane.addInitStateBox() {
        children.add(initStateBox)
    }

    private fun moveStateBox(stateBox: StateBox, positionX: Double, positionY: Double) {
        stateBox.layoutXProperty().value = max(positionX, 0.0)
        stateBox.layoutYProperty().value = max(positionY, 0.0)
    }

    private fun resetEditorMode() {
        isRemoveStateMode = false
        isRemoveTransactionMode = false
        isAddTransactionMode = false
    }

    private fun openStateTextEditorTab(state: StateBox) {
        val editor = editorFactory.createTextEditor().apply {
            replaceText(state.text)
            state.textProperty().bind(textProperty())
        }

        tabs.tabs.add(Tab(state.name, editor).apply {
            textProperty().bind(state.nameProperty())
        })
    }

    private fun openMainTextEditorTab() {
        val editor = editorFactory.createTextEditor().apply {
            replaceText(mainStateBox.text)
            mainStateBox.textProperty().bind(textProperty())
        }
        tabs.tabs.add(Tab("Main", editor))
    }

    private fun StateBox.initMouseRemoveStateEvents() {
        addMousePressedListener { it, _ ->
            if(!isRemoveStateMode){
                return@addMousePressedListener
            }
            it.removeFromEditor()
        }
    }

    private fun StateTransactionArrow.initMouseRemoveTransactionEvents() {
        addMousePressedListener { it, _ ->
            if(!isRemoveTransactionMode){
                return@addMousePressedListener
            }
            it.removeFromEditor()
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
                addTransactionArrow(statesToLink[0]!!, statesToLink[1]!!)
                isAddTransactionMode = false
            }

            return@addMouseClickedListeners
        }
    }

    private fun StateBox.initNameChangingEvent() {
        var previousName = ""
        isEditModeEnabledProperty().addListener { _, _, value ->
            if(value) {
                previousName = name
            } else {
                if(nameChangingMonitor.tryRegister(name)){
                    nameChangingMonitor.tryUnregister(previousName)
                } else {
                    name = previousName
                }
            }
        }
    }

    fun createEditPopOver(arrow: StateTransactionArrow, x: Double, y: Double): VBox {
        return VBox(
            Label("Alias (optional)"),
            TextField().apply {
                textProperty().bindBidirectional(arrow.aliasProperty)
            },
            Label("Predicate"),
            TextField().apply {
                textProperty().bindBidirectional(arrow.textProperty)
            },
        ).apply {
            translateXProperty().bind(widthProperty().divide(-2).add(x))
            translateY = y - 2.0

            padding = Insets(5.0)

            background = Background(
                BackgroundFill(
                    Color.WHITE,
                    CornerRadii(5.0),
                    Insets(0.0)
                )
            )

            effect = DropShadow(20.0, Color.LIGHTGRAY)

            setOnMouseExited {
                canvas.children.remove(this)
            }
        }
    }
}

