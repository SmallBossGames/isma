package ru.isma.next.editor.blueprint.controls

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.utilities.ClickDisambiguator
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel

class StateBox(
    val viewModel: StateViewModel,
    onClick: (StateViewModel) -> Unit = {},
    onDoubleClick: (StateViewModel) -> Unit = {},
) : Group() {
    private val registeredHandlers = mutableListOf<Pair<javafx.event.EventType<MouseEvent>, EventHandler<MouseEvent>>>()

    init {
        layoutXProperty().bind(viewModel.xProperty)
        layoutYProperty().bind(viewModel.yProperty)

        children.add(Rectangle().apply {
            heightProperty().bind(viewModel.squareHeightProperty)
            widthProperty().bind(viewModel.squareWidthProperty)
            fillProperty().bind(viewModel.colorProperty)
            viewOrder = 3.0
            arcWidth = CORNER_RADIUS
            arcHeight = CORNER_RADIUS
        })

        val nameTextArea = TextArea().apply {
            visibleProperty().bind(viewModel.editModeProperty)
            managedProperty().bind(viewModel.editModeProperty)
            focusedProperty().addListener { _, _, focused ->
                if (focused) {
                    text = viewModel.name
                } else {
                    viewModel.name = text
                    viewModel.commitEdit()
                }
            }
        }

        val nameLabel = Label().apply {
            font = Font("Arial", STATE_NAME_FONT_SIZE)
            textProperty().bind(viewModel.nameProperty)
            visibleProperty().bind(viewModel.editModeProperty.not())
            managedProperty().bind(viewModel.editModeProperty.not())
        }

        children.add(HBox().apply {
            prefHeightProperty().bind(viewModel.squareHeightProperty.subtract(20.0))
            prefWidthProperty().bind(viewModel.squareWidthProperty.subtract(20.0))
            translateX += STATE_INSET
            translateY += STATE_INSET
            alignment = Pos.CENTER
            children.add(nameLabel)
            children.add(nameTextArea)
        })

        val clickDisambiguator = ClickDisambiguator(
            singleClick = {
                if (viewModel.editable) {
                    viewModel.startEdit()
                    nameTextArea.requestFocus()
                }
                onClick(viewModel)
            },
            doubleClick = { onDoubleClick(viewModel) },
            clickDelay = 200L
        )

        val pressedHandler = EventHandler<MouseEvent> {
            clickDisambiguator.onKeyPress()
        }
        val draggedHandler = EventHandler<MouseEvent> {
            clickDisambiguator.onDragged()
        }
        val clickedHandler = EventHandler<MouseEvent> {
            clickDisambiguator.onClick(it)
        }

        addEventHandler(MouseEvent.MOUSE_PRESSED, pressedHandler)
        addEventHandler(MouseEvent.MOUSE_DRAGGED, draggedHandler)
        addEventHandler(MouseEvent.MOUSE_CLICKED, clickedHandler)

        registeredHandlers.addAll(listOf(
            MouseEvent.MOUSE_PRESSED to pressedHandler,
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
