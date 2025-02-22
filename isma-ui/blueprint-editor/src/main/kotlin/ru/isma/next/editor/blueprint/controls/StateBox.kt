package ru.isma.next.editor.blueprint.controls

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
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
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue

class StateBox(
    onPress: (StateBox, MouseEvent) -> Unit = { _,_ -> },
    onRelease: (StateBox, MouseEvent) -> Unit = { _,_ -> },
    onClick: (StateBox, MouseEvent) -> Unit = { _,_ -> },
    onDoubleClick: (StateBox, MouseEvent) -> Unit = { _,_ -> },
) : Group() {
    val isEditModeEnabledProperty = SimpleBooleanProperty(false)
    val isEditableProperty = SimpleBooleanProperty(true)
    val nameProperty = SimpleStringProperty("")
    private val isEditButtonVisibleProperty = SimpleBooleanProperty(true)
    private val textProperty = SimpleStringProperty("")
    private val squareWidthProperty = SimpleDoubleProperty(110.0)
    private val squareHeightProperty = SimpleDoubleProperty(65.0)
    private val colorProperty = SimpleObjectProperty<Paint>(Color.WHITE)

    private var isEditModeEnabled by isEditModeEnabledProperty
    private var isDragged = false

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
            arcWidth = 20.0
            arcHeight = 20.0
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
            font = Font("Arial", 16.0)
            textProperty().bind(nameProperty)
            visibleProperty().bind(!isEditModeEnabledProperty)
            managedProperty().bind(!isEditModeEnabledProperty)
        }

        children.add(HBox().apply {
            prefHeightProperty().bind(squareHeightProperty.subtract(20.0))
            prefWidthProperty().bind(squareWidthProperty.subtract(20.0))
            translateX += 10.0
            translateY += 10.0
            alignment = Pos.CENTER
            children.add(boxLabel)
            children.add(nameTextArea)
        })

        var singleClickAction: Job? = null

        addEventHandler(MouseEvent.MOUSE_CLICKED) {
            when(it.clickCount){
                1 -> {
                    if(singleClickAction == null) {
                        singleClickAction = coroutineScope.launch {
                            delay(200)

                            singleClickAction = null

                            if (!isDragged && isEditable) {
                                isEditModeEnabled = true
                                nameTextArea.requestFocus()
                            }

                            onClick(this@StateBox, it)
                        }
                    }

                }
                2 -> {
                    if(singleClickAction != null){
                        singleClickAction?.cancel()
                        singleClickAction = null

                        onDoubleClick(this@StateBox, it)
                    }
                }
            }
        }
        addEventHandler(MouseEvent.MOUSE_PRESSED) {
            isDragged = false
            onPress(this@StateBox, it)
        }
        addEventHandler(MouseEvent.MOUSE_RELEASED) {
            onRelease(this@StateBox, it)
        }
        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            isDragged = true
        }
    }

    companion object {
        private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    }
}