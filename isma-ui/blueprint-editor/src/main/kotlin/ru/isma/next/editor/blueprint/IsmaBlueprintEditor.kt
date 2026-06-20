package ru.isma.next.editor.blueprint

import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.controls.*
import ru.isma.next.editor.blueprint.models.*
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue
import kotlin.math.max

class IsmaBlueprintEditor(private val editorFactory: ITextEditorFactory): BorderPane() {
    private val nameChangingMonitor = NameChangingMonitor("New state")

    private val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
    private var editorMode by editorModeProperty

    private var activeStateBox: StateBox? = null

    private var xOffset = 0.0
    private var yOffset = 0.0

    private val canvasViewModel = CanvasViewModel()
    private val mainStateBox: StateBox = createMainStateBox()
    private val initStateBox: StateBox = createInitStateBox()

    private val canvas = Pane().apply {
        addMainStateBox()
        addInitStateBox()

        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            if(it.isPrimaryButtonDown && activeStateBox != null && editorMode !is EditorMode.RemoveState) {
                moveStateBox(activeStateBox!!, it.x + xOffset, it.y + yOffset)
            }
        }
    }

    private val diagramTab = Tab("Diagram", ScrollPane(canvas)).apply {
        isClosable = false
    }

    private val tabs = TabPane(
        diagramTab
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
                    if(editorMode is EditorMode.AddTransition) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        editorMode = EditorMode.AddTransition(mutableListOf())
                    }
                }

                val updateText = {
                    text = when(editorMode) {
                        is EditorMode.AddTransition -> "Stop adding transaction"
                        else -> "New transition"
                    }
                }
                updateText()
                editorModeProperty.addListener { _, _, _ -> updateText() }
            },
            Separator(),
            Button("Remove state").apply {
                onAction = EventHandler {
                    if (editorMode is EditorMode.RemoveState) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        editorMode = EditorMode.RemoveState
                    }
                }

                val updateText = {
                    text = when(editorMode) {
                        is EditorMode.RemoveState -> "Stop remove state"
                        else -> "Remove state"
                    }
                }
                updateText()
                editorModeProperty.addListener { _, _, _ -> updateText() }
            },

            Button("Remove transition").apply {
                onAction = EventHandler {
                    if (editorMode is EditorMode.RemoveTransition) {
                        resetEditorMode()
                    } else {
                        resetEditorMode()
                        editorMode = EditorMode.RemoveTransition
                    }
                }

                val updateText = {
                    text = when(editorMode) {
                        is EditorMode.RemoveTransition -> "Stop remove transition"
                        else -> "Remove transition"
                    }
                }
                updateText()
                editorModeProperty.addListener { _, _, _ -> updateText() }
            }
        ).apply {
            val visible = tabs.selectionModel.selectedItemProperty().isEqualTo(diagramTab)
            visibleProperty().bind(visible)
            managedProperty().bind(visible)
        }
    }

    fun getBlueprintModel() : BlueprintModel {
        val main = mainStateBox.toBlueprintState()
        val init = initStateBox.toBlueprintState()
        val states = canvasViewModel.states.map { it.toBlueprintState() }.toTypedArray()
        val blueprintTransactions = canvasViewModel.transactions.map { it.toBlueprintTransaction() }.toTypedArray()
        val blueprintLoopTransactions = canvasViewModel.loopTransactions.map { it.toBlueprintLoopTransaction() }.toTypedArray()

        return BlueprintModel(main, init, states, blueprintTransactions, blueprintLoopTransactions)
    }

    fun setBlueprintModel(model: BlueprintModel) {
        canvasViewModel.states.toList().forEach { it.removeFromEditor() }

        mainStateBox.applyBlueprintState(model.main)
        initStateBox.applyBlueprintState(model.init)

        val stateMap = model.states.associateByTo(
            mutableMapOf(
                initStateBox.name to initStateBox,
                mainStateBox.name to mainStateBox
            ),
            { it.name },
            {instantiateStateBoxFromBlueprintState(it)}
        )

        stateMap.values.forEach { canvasViewModel.addState(it) }

        model.transactions.forEach {
            addTransactionArrow(
                stateMap[it.startStateName]!!,
                stateMap[it.endStateName]!!,
                it.predicate,
                it.alias,
            )
        }

        model.loopTransactions.forEach { loopTransaction ->
            addLoopTransactionArrow(
                stateMap[loopTransaction.stateName]!!,
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

    private fun CanvasViewModel.EditorTransaction.toBlueprintTransaction() : BlueprintTransactionModel {
        return  BlueprintTransactionModel(
            startStateName = this.startBox.name,
            endStateName = this.endBox.name,
            predicate = this.arrow.text,
            alias = this.arrow.alias
        )
    }

    private fun CanvasViewModel.EditorLoopTransaction.toBlueprintLoopTransaction() : BlueprintLoopTransactionModel {
        return BlueprintLoopTransactionModel(
            stateName = this.stateBox.name,
            predicate = this.arrow.predicate,
            alias = this.arrow.alias,
            text = this.arrow.text,
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

            isEditableProperty.bind(editorModeProperty.map { it.isNotEditingMode() })
        }

        canvasViewModel.addState(stateBox)
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
            squareHeight = FIXED_STATE_HEIGHT
            name = MAIN_STATE
            layoutXProperty().value += STATE_INSET
            layoutXProperty().value += STATE_INSET

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
            squareHeight = FIXED_STATE_HEIGHT
            name = INIT_STATE
            layoutXProperty().value += STATE_INSET
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
        if(canvasViewModel.loopTransactions.any { it.stateBox == stateBox }) {
            return
        }

        val loopTransactionArrow = LoopTransactionArrow(
            onClick = { source, _ ->
                if(editorMode is EditorMode.RemoveTransition){
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

        canvasViewModel.addLoopTransaction(CanvasViewModel.EditorLoopTransaction(stateBox, loopTransactionArrow))
    }

    private fun addTransactionArrow(
        startStateBox: StateBox,
        endStateBox: StateBox,
        predicate: String = "",
        alias: String = "",
    ) {
        if(canvasViewModel.transactions.any { it.startBox == startStateBox && it.endBox == endStateBox }) {
            return
        }

        val transactionArrow = TransactionArrow(
            onClick = { source, _ ->
                if(editorMode is EditorMode.RemoveTransition){
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

        canvasViewModel.addTransaction(CanvasViewModel.EditorTransaction(startStateBox, endStateBox, transactionArrow))
    }

    private fun StateBox.removeFromEditor() {
        canvasViewModel.removeState(this)
        canvas.children.remove(this)
    }

    private fun TransactionArrow.removeFromEditor() {
        canvasViewModel.removeTransaction(this)
        canvas.children.remove(this)
    }

    private fun LoopTransactionArrow.removeFromEditor() {
        canvasViewModel.removeLoopTransaction(this)
        canvas.children.remove(this)
    }

    private fun moveStateBox(stateBox: StateBox, positionX: Double, positionY: Double) {
        stateBox.layoutXProperty().value = max(positionX, 0.0)
        stateBox.layoutYProperty().value = max(positionY, 0.0)
    }

    private fun resetEditorMode() {
        editorMode = EditorMode.Idle
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
        if(editorMode is EditorMode.RemoveState){
            source.removeFromEditor()
        }
    }

    private fun mouseMovingEventPressHandler(source: StateBox, event: MouseEvent) {
        if(editorMode is EditorMode.Idle){
            xOffset = -event.x
            yOffset = -event.y
            activeStateBox = source
        }
    }

    private fun mouseMovingEventReleaseHandler() {
        activeStateBox = null
    }

    private fun mouseLinkTransactionEventHandler(source: StateBox) {
        if (editorMode is EditorMode.AddTransition) {
            (editorMode as EditorMode.AddTransition).selectedStates.add(source)

            if ((editorMode as EditorMode.AddTransition).selectedStates.size < 2) {
                return
            }

            val states = (editorMode as EditorMode.AddTransition).selectedStates
            val state1 = states[0]!!
            val state2 = states[1]!!

            if(state1 === state2){
                addLoopTransactionArrow(state1)
            } else {
                addTransactionArrow(state1, state2)
            }

            editorMode = EditorMode.Idle
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
