package views.editors.blueprint.controls

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import tornadofx.*

class StateBox : Fragment() {
    private val squareWidth = 150.0
    private val squareHeight = 80.0

    private val mousePressedListeners = arrayListOf<(StateBox, MouseEvent) -> Unit>()
    private val mouseReleasedListeners = arrayListOf<(StateBox, MouseEvent) -> Unit>()
    private val mouseClickedListeners = arrayListOf<(StateBox, MouseEvent) -> Unit>()
    private val editActionListeners = arrayListOf<() -> Unit>()

    private val isEditModeEnabledProperty = SimpleBooleanProperty(false)
    private fun isEditModeEnabledProperty() = isEditModeEnabledProperty
    private var isEditModeEnabled by isEditModeEnabledProperty

    private val isEditableProperty = SimpleBooleanProperty(true)
    public fun isEditableProperty() = isEditableProperty
    public var isEditable by isEditableProperty

    private val boxNameProperty = SimpleStringProperty("")
    public fun boxNameProperty() = boxNameProperty
    public var boxName by boxNameProperty

    private var isDragged = false

    fun translateXProperty(): DoubleProperty = root.layoutXProperty()
    fun translateYProperty(): DoubleProperty = root.layoutYProperty()

    fun centerXProperty(): DoubleBinding = root.layoutXProperty() + squareWidth / 2
    fun centerYProperty(): DoubleBinding = root.layoutYProperty() + squareHeight / 2

    override val root = group {
        prefWidth(squareWidth)
        prefHeight(squareHeight)
        rectangle {
            viewOrder = 3.0
            fill = Color.CORAL
            width = squareWidth
            height = squareHeight
            arcWidth = 20.0
            arcHeight = 20.0
        }

        val nameTextArea = TextArea().apply {
            textProperty().bindBidirectional(boxNameProperty())
            visibleWhen(isEditModeEnabledProperty())
            managedWhen(isEditModeEnabledProperty())
            focusedProperty().onChange {
                if(!it){
                    isEditModeEnabled = false
                }
            }
        }

        val boxLabel = Label().label {
            textProperty().bind(boxNameProperty())
            visibleWhen(!isEditModeEnabledProperty())
            managedWhen(!isEditModeEnabledProperty())
        }

        group {
            hbox {
                setPrefSize(squareWidth - 20, squareHeight - 20)
                translateX += 10
                translateY += 10
                alignment = Pos.CENTER
                add(boxLabel)
                add(nameTextArea)
            }
            hbox {
                button("Edit") {
                    action { executeEditActionListeners() }
                }
                visibleWhen(!isEditModeEnabledProperty())
                managedWhen(!isEditModeEnabledProperty())
            }
        }

        addEventHandler(MouseEvent.MOUSE_CLICKED) {
            if (!isDragged && isEditable) {
                isEditModeEnabled = true
                nameTextArea.requestFocus()
            }
            executeMouseClickedListeners(it)
        }

        addEventHandler(MouseEvent.MOUSE_PRESSED) {
            isDragged = false
            executeMousePressedListener(it)
        }
        addEventHandler(MouseEvent.MOUSE_RELEASED) {
            executeMouseReleasedListeners(it)
        }
        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            isDragged = true
        }
    }

    fun addMousePressedListener(handler: (StateBox, MouseEvent) -> Unit){
        mousePressedListeners.add(handler);
    }

    fun removeMousePressedListener(handler: (StateBox, MouseEvent) -> Unit){
        mousePressedListeners.remove(handler);
    }

    private fun executeMousePressedListener(event: MouseEvent){
        if(event.isPrimaryButtonDown){
            for(i in mousePressedListeners.indices){
                mousePressedListeners[i](this, event)
            }
        }
    }

    fun addMouseReleasedListener(handler: (StateBox, MouseEvent) -> Unit){
        mouseReleasedListeners.add(handler);
    }

    fun removeMouseReleasedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseReleasedListeners.remove(handler);
    }

    private fun executeMouseReleasedListeners(event: MouseEvent) {
        for (i in mouseReleasedListeners.indices) {
            mouseReleasedListeners[i](this, event)
        }
    }

    fun addMouseClickedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseClickedListeners.add(handler);
    }

    fun removeMouseClickedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseClickedListeners.remove(handler);
    }

    private fun executeMouseClickedListeners(event: MouseEvent){
        for(i in mouseClickedListeners.indices){
            mouseClickedListeners[i](this, event)
        }
    }

    fun addEditActionListener(op: () -> Unit){
        editActionListeners.add(op);
    }

    fun removeEditActionListener(op: () -> Unit){
        editActionListeners.remove(op);
    }

    private fun executeEditActionListeners(){
        for(i in mouseClickedListeners.indices){
            editActionListeners[i]()
        }
    }
}