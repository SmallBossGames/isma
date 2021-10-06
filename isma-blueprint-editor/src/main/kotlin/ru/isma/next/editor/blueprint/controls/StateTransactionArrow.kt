package ru.isma.next.editor.blueprint.controls


import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Line
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class StateTransactionArrow : Group() {
    private val startXProperty = SimpleDoubleProperty(0.0)
    private val startYProperty = SimpleDoubleProperty(0.0)
    private val endXProperty = SimpleDoubleProperty(0.0)
    private val endYProperty = SimpleDoubleProperty(0.0)

    private val textProperty = SimpleStringProperty("")
    private val isTextEditModeProperty = SimpleBooleanProperty(false)

    private val mousePressedListeners = arrayListOf<(StateTransactionArrow, MouseEvent) -> Unit>()

    fun startXProperty() = startXProperty
    fun startYProperty() = startYProperty
    fun endXProperty() = endXProperty
    fun endYProperty() = endYProperty
    fun isTextEditModeProperty() = isTextEditModeProperty

    fun textProperty() = textProperty

    val startX by startXProperty
    val startY by startYProperty
    val endX by endXProperty
    val endY by endYProperty

    var isTextEditMode by isTextEditModeProperty
    var text: String by textProperty

    init {
        viewOrder = 4.0
        layoutXProperty().bind((endXProperty.subtract(startXProperty)).divide( 2).add(startXProperty))
        layoutYProperty().bind((endYProperty.subtract(startYProperty)).divide(2).add(startYProperty))

        val predicateText = Group().apply {
            viewOrder = 5.0
            children.add(TextField().apply {
                prefWidth = TextFieldLength
                translateY = -10.0
                translateX = -TextFieldLength / 2.0
                textProperty().bindBidirectional(this@StateTransactionArrow.textProperty)

                visibleProperty().bind(isTextEditModeProperty)
                managedProperty().bind(isTextEditModeProperty)

                isTextEditModeProperty().addListener { _, _, value ->
                    if (value) {
                        requestFocus()
                    }
                }

                focusedProperty().addListener { _, _, value ->
                    if (!value) {
                        isTextEditMode = false
                    }
                }
            })
            children.add(
                Label().apply {
                    font = Font("Arial", 16.0)
                    prefWidth = TextFieldLength
                    translateY = -10.0
                    translateX = -TextFieldLength / 2.0
                    alignment = Pos.CENTER
                    textProperty().bind(this@StateTransactionArrow.textProperty)
                    visibleProperty().bind(!isTextEditModeProperty)
                    managedProperty().bind(!isTextEditModeProperty)
                }
            )
        }

        val arrowhead = Polygon(7.0, -7.0, -7.0, 0.0, 7.0, 7.0).apply {
            strokeWidth = 3.0
            viewOrder = 6.0
        }

        children.addAll(arrowhead, predicateText)

        line {
            strokeWidth = 3.0
            viewOrder = 6.0

            fun updateGeometry() {
                val x = this@StateTransactionArrow.endX - this@StateTransactionArrow.startX
                val y = this@StateTransactionArrow.endY - this@StateTransactionArrow.startY
                val angle = atan2(x, y) + PI / 2
                val offsetX = 10.0 * sin(angle)
                val offsetY = 10.0 * cos(angle)
                val textOffsetX = 70.0 * sin(angle)
                val textOffsetY = 40.0 * cos(angle)

                startX = this@StateTransactionArrow.startX - this@StateTransactionArrow.layoutX + offsetX
                startY = this@StateTransactionArrow.startY - this@StateTransactionArrow.layoutY + offsetY
                endX = this@StateTransactionArrow.endX - this@StateTransactionArrow.layoutX + offsetX
                endY = this@StateTransactionArrow.endY - this@StateTransactionArrow.layoutY + offsetY

                arrowhead.translateX = offsetX
                arrowhead.translateY = offsetY
                arrowhead.rotate = -angle / PI * 180.0

                predicateText.translateX = textOffsetX
                predicateText.translateY = textOffsetY
            }

            this@StateTransactionArrow.startXProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.startYProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.endXProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.endYProperty.onChange { updateGeometry() }
        }


        addEventHandler(MouseEvent.MOUSE_PRESSED) {
            executeMousePressedListener(it)
        }

        addEventHandler(MouseEvent.MOUSE_CLICKED) {
            isTextEditMode = true
        }
    }

    fun addMousePressedListener(handler: (StateTransactionArrow, MouseEvent) -> Unit){
        mousePressedListeners.add(handler)
    }

    fun removeMousePressedListener(handler: (StateTransactionArrow, MouseEvent) -> Unit){
        mousePressedListeners.remove(handler)
    }

    private fun executeMousePressedListener(event: MouseEvent){
        if(event.isPrimaryButtonDown){
            for(i in mousePressedListeners.indices){
                mousePressedListeners[i](this, event)
            }
        }
    }

    fun <T> ObservableValue<T>.onChange(op: () -> Unit){
        this.addListener{ _, _, value ->
            op()
        }
    }

    companion object {
        private const val TextFieldLength = 100.0

        inline fun Group.line(op: Line.() -> Unit){
            this.children.add(
                Line().apply(op)
            )
        }
    }
}