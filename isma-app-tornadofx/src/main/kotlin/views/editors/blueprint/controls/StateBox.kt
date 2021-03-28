package views.editors.blueprint.controls

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.DoubleProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import tornadofx.*

class StateBox : Fragment() {
    private val squareWidth = 150.0
    private val squareHeight = 80.0

    private val mousePressedListeners = arrayListOf<(StateBox, MouseEvent) -> Unit>()
    private val mouseReleasedListeners = arrayListOf<(StateBox, MouseEvent) -> Unit>()
    private val mouseClickedListeners = arrayListOf<(StateBox, MouseEvent) -> Unit>()

    fun translateXProperty(): DoubleProperty = root.layoutXProperty()
    fun translateYProperty(): DoubleProperty = root.layoutYProperty()

    fun centerXProperty(): DoubleBinding = root.layoutXProperty() + squareWidth / 2
    fun centerYProperty(): DoubleBinding = root.layoutYProperty() + squareHeight / 2

    override val root = group {
        rectangle {
            viewOrder = 3.0
            fill = Color.CORAL
            width = squareWidth
            height = squareHeight
            arcWidth = 20.0
            arcHeight = 20.0
        }
        label("Some label")

        addEventHandler(MouseEvent.MOUSE_PRESSED) {
            executeMousePressedListener(it)
        }
        addEventHandler(MouseEvent.MOUSE_RELEASED) {
            executeMouseReleasedListeners(it)
        }
    }

    fun addMousePressedListener(handler: (StateBox, MouseEvent) -> Unit){
        mousePressedListeners.add(handler);
    }

    fun removeMousePressedListener(handler: (StateBox, MouseEvent) -> Unit){
        mousePressedListeners.remove(handler);
    }

    private fun executeMousePressedListener(event: MouseEvent){
        mousePressedListeners.forEach { it(this, event) }
    }

    fun addMouseReleasedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseReleasedListeners.add(handler);
    }

    fun removeMouseReleasedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseReleasedListeners.remove(handler);
    }

    private fun executeMouseReleasedListeners(event: MouseEvent){
        mouseReleasedListeners.forEach { it(this, event) }
    }

    fun addMouseClickedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseClickedListeners.add(handler);
    }

    fun removeMouseClickedListeners(handler: (StateBox, MouseEvent) -> Unit){
        mouseClickedListeners.remove(handler);
    }

    private fun executeMouseClickedListeners(event: MouseEvent){
        mouseClickedListeners.forEach { it(this, event) }
    }
}