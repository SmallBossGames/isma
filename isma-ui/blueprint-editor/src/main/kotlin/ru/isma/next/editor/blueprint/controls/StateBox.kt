package ru.isma.next.editor.blueprint.controls

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.controls.CoroutineScopeProvider
import ru.isma.next.editor.blueprint.utilities.ClickDisambiguator
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue

class StateBox(
    onPress: (StateBox, MouseEvent) -> Unit = { _,_ -> },
    onRelease: (StateBox, MouseEvent) -> Unit = { _,_ -> },
    onClick: (StateBox, MouseEvent) -> Unit = { _,_ -> },
    onDoubleClick: (StateBox, MouseEvent) -> Unit = { _,_ -> },
) : Group() {
    private val registeredHandlers = mutableListOf<Pair<javafx.event.EventType<MouseEvent>, EventHandler<MouseEvent>>>()

    val isEditModeEnabledProperty = SimpleBooleanProperty(false)
    val isEditableProperty = SimpleBooleanProperty(true)
    val nameProperty = SimpleStringProperty("")
    private val isEditButtonVisibleProperty = SimpleBooleanProperty(true)
    private val textProperty = SimpleStringProperty("")
    private val squareWidthProperty = SimpleDoubleProperty(DEFAULT_STATE_WIDTH)
    private val squareHeightProperty = SimpleDoubleProperty(DEFAULT_STATE_HEIGHT)
    private val colorProperty = SimpleObjectProperty<Paint>(Color.WHITE)

    private var isEditModeEnabled by isEditModeEnabledProperty

    var isEditable by isEditableProperty
    var isEditButtonVisible by isEditButtonVisibleProperty
    var name: String by nameProperty
    var text: String by textProperty
    var squareWidth by squareWidthProperty
    var squareHeight by squareHeightProperty
    var color: Paint by colorProperty

    fun centerXProperty(): DoubleBinding = layoutXProperty().add(squareWidth / 2)
    fun centerYProperty(): DoubleBinding = layoutYProperty().add(squareHeight / 2)

    init {
        children.add(Rectangle().apply {
            heightProperty().bind(squareHeightProperty)
            widthProperty().bind(squareWidthProperty)
            fillProperty().bind(colorProperty)
            viewOrder = 3.0
            arcWidth = CORNER_RADIUS
            arcHeight = CORNER_RADIUS
        })

        val nameTextArea = TextArea().apply {
            visibleProperty().bind(isEditModeEnabledProperty)
            managedProperty().bind(isEditModeEnabledProperty)
            focusedProperty().addListener { _, _, value ->
                if (value) {
                    text = name
                } else {
                    name = text
                    isEditModeEnabled = false
                }
            }
        }

        val boxLabel = Label().apply {
            font = Font("Arial", STATE_NAME_FONT_SIZE)
            textProperty().bind(nameProperty)
            visibleProperty().bind(!isEditModeEnabledProperty)
            managedProperty().bind(!isEditModeEnabledProperty)
        }

        children.add(HBox().apply {
            prefHeightProperty().bind(squareHeightProperty.subtract(20.0))
            prefWidthProperty().bind(squareWidthProperty.subtract(20.0))
            translateX += STATE_INSET
            translateY += STATE_INSET
            alignment = Pos.CENTER
            children.add(boxLabel)
            children.add(nameTextArea)
        })

        val clickDisambiguator = ClickDisambiguator(
            coroutineScope = CoroutineScopeProvider.scope,
            singleClick = {
                if (isEditable) {
                    isEditModeEnabled = true
                    nameTextArea.requestFocus()
                }
                onClick(this@StateBox, it)
            },
            doubleClick = { onDoubleClick(this@StateBox, it) }
        )

        val pressedHandler = EventHandler<MouseEvent> {
            clickDisambiguator.onKeyPress()
            onPress(this@StateBox, it)
        }
        val releasedHandler = EventHandler<MouseEvent> {
            onRelease(this@StateBox, it)
        }
        val draggedHandler = EventHandler<MouseEvent> {
            clickDisambiguator.onDragged()
        }
        val clickedHandler = EventHandler<MouseEvent> {
            clickDisambiguator.onClick(it)
        }

        addEventHandler(MouseEvent.MOUSE_PRESSED, pressedHandler)
        addEventHandler(MouseEvent.MOUSE_RELEASED, releasedHandler)
        addEventHandler(MouseEvent.MOUSE_DRAGGED, draggedHandler)
        addEventHandler(MouseEvent.MOUSE_CLICKED, clickedHandler)

        registeredHandlers.addAll(listOf(
            MouseEvent.MOUSE_PRESSED to pressedHandler,
            MouseEvent.MOUSE_RELEASED to releasedHandler,
            MouseEvent.MOUSE_DRAGGED to draggedHandler,
            MouseEvent.MOUSE_CLICKED to clickedHandler
        ))

        parentProperty().addListener { _, _, newParent ->
            if (newParent == null) {
                cleanup()
            }
        }
    }

    private fun cleanup() {
        for ((eventType, handler) in registeredHandlers) {
            removeEventHandler(eventType, handler)
        }
        registeredHandlers.clear()
    }
}