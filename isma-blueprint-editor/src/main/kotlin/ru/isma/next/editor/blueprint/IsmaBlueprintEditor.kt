package ru.isma.next.editor.blueprint

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import ru.isma.next.editor.blueprint.constants.INIT_STATE
import ru.isma.next.editor.blueprint.constants.MAIN_STATE
import ru.isma.next.editor.blueprint.controls.EditArrowPopOver
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow
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
        Tab("Diagram", ScrollPane(canvas)).apply {
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
        val blueprintTransactions = transactions.map { it.toBlueprintTransaction() }.toTypedArray()

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
            addTransactionArrow(
                stateBoxes[it.startStateName]!!,
                stateBoxes[it.endStateName]!!,
                it.predicate,
                it.alias,
            )
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

    private fun BlueprintEditorTransactionModel.toBlueprintTransaction() : BlueprintTransactionModel {
        return  BlueprintTransactionModel(
            startStateName = this.startStateBox.name,
            endStateName = this.endStateBox.name,
            predicate = this.transactionArrow.text,
            alias = this.transactionArrow.alias
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
        val stateBox = StateBox(

            onPress = { source, event ->
                mouseMovingEventPressHandler(source, event)
            },
            onRelease = { _, _ ->
                mouseMovingEventReleaseHandler()
            },
            onClick = { source, _ ->
                mouseLinkTransactionEventHandler(source)
                mouseRemoveStateEventHandler(source)
            },
            onDoubleClick = { source, _ ->
                openStateTextEditorTab(source)
            }
        ).apply {
            color = Color.CORAL

            initNameChangingEvent()

            layoutXProperty().value = positionX
            layoutYProperty().value = positionY
            name = stateName.ifEmpty { nameChangingMonitor.createNextDefaultName() }
            text = stateText

            nameChangingMonitor.tryRegister(name)

            isEditableProperty.bind((isRemoveStateModeProperty).or(isAddTransactionModeProperty).not())
        }

        stateBoxes.add(stateBox)
        canvas.children.add(stateBox)

        return stateBox
    }

    private fun createMainStateBox() : StateBox {
        return StateBox(
            onDoubleClick = { source, _ ->
                openStateTextEditorTab(source)
            },
            onPress = { source, event ->
                mouseMovingEventPressHandler(source, event)
            },
            onRelease = { _, _ ->
                mouseMovingEventReleaseHandler()
            }
        ).apply {
            color = Color.LIGHTGREEN
            isEditable = false
            squareHeight = 60.0
            name = MAIN_STATE
            layoutXProperty().value += 10
            layoutXProperty().value += 10

            nameChangingMonitor.tryRegister(name)
        }
    }

    private fun createInitStateBox() : StateBox {
        return StateBox(
            onClick = { source, _ ->
                mouseLinkTransactionEventHandler(source)
            },
            onPress = { source, event ->
                mouseMovingEventPressHandler(source, event)
            },
            onRelease = { _, _ ->
                mouseMovingEventReleaseHandler()
            }
        ).apply {
            color = Color.LIGHTBLUE
            isEditButtonVisible = false
            isEditable = false
            squareHeight = 60.0
            name = INIT_STATE
            layoutXProperty().value += 10
            layoutYProperty().value += 100

            nameChangingMonitor.tryRegister(name)
        }
    }

    private fun Pane.addMainStateBox() {
        children.add(mainStateBox)
    }

    private fun Pane.addInitStateBox() {
        children.add(initStateBox)
    }

    private fun addStateBox() {
        instantiateStateBox(10.0, 200.0)
    }

    private fun addTransactionArrow(
        startStateBox: StateBox,
        endStateBox: StateBox,
        predicate: String = "",
        alias: String = ""
    ) {
        if(transactions.any { it.startStateBox == startStateBox && it.endStateBox == endStateBox }) {
            return
        }

        val transactionArrow = TransactionArrow(
            onClick = { source, _ ->
                if(isRemoveTransactionMode){
                    source.removeFromEditor()
                }
            },
            onArrowClick = { source, event ->
                val converted = canvas.sceneToLocal(event.sceneX, event.sceneY)

                val popover = createEditPopOver(source, converted.x, converted.y)

                canvas.children.add(popover)
            },
        ).apply {
            startXProperty.bind(startStateBox.centerXProperty())
            startYProperty.bind(startStateBox.centerYProperty())
            endXProperty.bind(endStateBox.centerXProperty())
            endYProperty.bind(endStateBox.centerYProperty())

            this.text = predicate
            this.alias = alias
        }

        canvas.children.add(transactionArrow)

        transactions.add(BlueprintEditorTransactionModel(startStateBox, endStateBox, transactionArrow))
    }

    private fun StateBox.removeFromEditor() {
        transactions.toList()
            .filter { it.startStateBox == this || it.endStateBox == this }
            .forEach { removeTransaction(it) }

        stateBoxes.remove(this)
        canvas.children.remove(this)
    }

    private fun TransactionArrow.removeFromEditor() {
        transactions.toList()
            .filter { it.transactionArrow == this }
            .forEach { removeTransaction(it) }
    }

    private fun removeTransaction(transaction: BlueprintEditorTransactionModel) {
        transactions.remove(transaction)

        canvas.children.remove(transaction.transactionArrow)
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
        val editor = editorFactory.createTextEditor(
            text = state.text,
            onTextChanged = { state.text = it }
        )

        tabs.tabs.add(Tab(state.name, editor).apply {
            textProperty().bind(state.nameProperty)
        })
    }

    private fun mouseRemoveStateEventHandler(source: StateBox) {
        if(isRemoveStateMode){
            source.removeFromEditor()
        }
    }

    private fun mouseMovingEventPressHandler(source: StateBox, event: MouseEvent) {
        if(!isRemoveStateMode && !isAddTransactionMode){
            xOffset = -event.x
            yOffset = -event.y
            activeStateBox = source
        }
    }

    private fun mouseMovingEventReleaseHandler() {
        activeStateBox = null
    }

    private fun mouseLinkTransactionEventHandler(source: StateBox) {
        if (isAddTransactionMode && !isRemoveStateMode) {
            statesToLink[addTransactionStateCounter++] = source

            if (addTransactionStateCounter > 1) {
                addTransactionArrow(statesToLink[0]!!, statesToLink[1]!!)
                isAddTransactionMode = false
            }
        }
    }

    private fun StateBox.initNameChangingEvent() {
        var previousName = ""
        isEditModeEnabledProperty.addListener { _, _, value ->
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

    private fun createEditPopOver(arrow: TransactionArrow, x: Double, y: Double) =
        EditArrowPopOver(arrow, x, y).apply {
            setOnMouseExited {
                canvas.children.remove(this)
            }
        }
}

