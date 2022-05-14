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
import ru.isma.next.editor.blueprint.controls.*
import ru.isma.next.editor.blueprint.models.*
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
    private val loopTransactions = ArrayList<BlueprintEditorLoopTransactionModel>()
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
        val blueprintLoopTransactions = loopTransactions.map { it.toBlueprintLoopTransaction() }.toTypedArray()

        return BlueprintModel(main, init, states, blueprintTransactions, blueprintLoopTransactions)
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

        model.loopTransactions.forEach { loopTransaction ->
            addLoopTransactionArrow(
                stateBoxes[loopTransaction.stateName]!!,
                loopTransaction.text,
                loopTransaction.predicate,
                loopTransaction.alias
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

    private fun BlueprintEditorLoopTransactionModel.toBlueprintLoopTransaction() : BlueprintLoopTransactionModel {
        return BlueprintLoopTransactionModel(
            stateName = this.stateBox.name,
            predicate = this.loopTransactionArrow.predicate,
            alias = this.loopTransactionArrow.alias,
            text = this.loopTransactionArrow.text,
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

    private fun addLoopTransactionArrow(
        stateBox: StateBox,
        text: String = "",
        predicate: String = "",
        alias: String = "",
    ){
        if(loopTransactions.any { it.stateBox == stateBox }) {
            return
        }

        val loopTransactionArrow = LoopTransactionArrow(
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
            onArrowDoubleClick = { source, _ ->
                openStateTextEditorTab(source, stateBox)
            },
            text = text,
            alias = alias,
            predicate = predicate
        ).apply {
            layoutXProperty().bind(stateBox.centerXProperty())
            layoutYProperty().bind(stateBox.centerYProperty())
        }

        canvas.children.add(loopTransactionArrow)

        loopTransactions.add(BlueprintEditorLoopTransactionModel(stateBox, loopTransactionArrow))
    }

    private fun addTransactionArrow(
        startStateBox: StateBox,
        endStateBox: StateBox,
        predicate: String = "",
        alias: String = "",
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

    private fun LoopTransactionArrow.removeFromEditor() {
        loopTransactions.toList()
            .filter { it.loopTransactionArrow == this }
            .forEach { removeTransaction(it) }
    }

    private fun removeTransaction(transaction: BlueprintEditorTransactionModel) {
        transactions.remove(transaction)

        canvas.children.remove(transaction.transactionArrow)
    }

    private fun removeTransaction(transaction: BlueprintEditorLoopTransactionModel) {
        loopTransactions.remove(transaction)

        canvas.children.remove(transaction.loopTransactionArrow)
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

            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
            }
        })
    }

    private fun openStateTextEditorTab(arrow: LoopTransactionArrow, stateBox: StateBox) {
        val editor = editorFactory.createTextEditor(
            text = arrow.text,
            onTextChanged = { arrow.text = it }
        )

        tabs.tabs.add(Tab("${stateBox.name} (loop)", editor).apply {
            textProperty().bind(stateBox.nameProperty.concat(" (loop)"))

            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
            }
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

            if (addTransactionStateCounter < 2) {
                return
            }

            val state1 = statesToLink[0]!!
            val state2 = statesToLink[1]!!

            if(state1 === state2){
                addLoopTransactionArrow(state1)
            } else {
                addTransactionArrow(state1, state2)
            }

            isAddTransactionMode = false
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

    private fun createEditPopOver(arrow: ITransactionArrowData, x: Double, y: Double) =
        EditArrowPopOver(arrow, x, y).apply {
            setOnMouseExited {
                canvas.children.remove(this)
            }
        }
}

