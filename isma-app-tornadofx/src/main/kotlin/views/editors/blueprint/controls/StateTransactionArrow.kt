package views.editors.blueprint.controls

import javafx.scene.control.TextField
import javafx.scene.control.Label
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.input.MouseEvent

import tornadofx.*
import kotlin.math.*

class StateTransactionArrow : Fragment() {

    private val startXProperty = SimpleDoubleProperty(0.0)
    private val startYProperty = SimpleDoubleProperty(0.0)
    private val endXProperty = SimpleDoubleProperty(0.0)
    private val endYProperty = SimpleDoubleProperty(0.0)

    private val textProperty = SimpleStringProperty("")
    private val isTextEditModeProperty = SimpleBooleanProperty(false)

    fun startXProperty() = startXProperty
    fun startYProperty() = startYProperty
    fun endXProperty() = endXProperty
    fun endYProperty() = endYProperty

    fun textProperty() = textProperty

    val startX by startXProperty
    val startY by startYProperty
    val endX by endXProperty
    val endY by endYProperty

    var isTextEditMode by isTextEditModeProperty
    var text by textProperty

    override val root: Parent = group {
        viewOrder = 4.0
        layoutXProperty().bind((endXProperty - startXProperty) / 2 + startXProperty)
        layoutYProperty().bind((endYProperty - startYProperty) / 2 + startYProperty)

        val arrowTextEditor = TextField().apply {
            prefWidth = TextFieldLength
            translateY -= 11.0
            translateX = -TextFieldLength / 2.0
            textProperty().bindBidirectional(this@StateTransactionArrow.textProperty)
            visibleWhen(isTextEditModeProperty)
            managedWhen(isTextEditModeProperty)

            focusedProperty().onChange {
                if(!it) {
                    isTextEditMode = false
                }
            }
        }
        val arrowText = Label().apply {
            prefWidth = TextFieldLength
            translateY -= 11.0
            translateX = -TextFieldLength / 2.0
            alignment = Pos.CENTER
            textProperty().bind(this@StateTransactionArrow.textProperty)
            visibleWhen(!isTextEditModeProperty)
            managedWhen(!isTextEditModeProperty)
        }
        val predicateText = group {
            viewOrder = 7.0
            add(arrowTextEditor)
            add(arrowText)
        }

        val arrowhead = polyline(5.0, -5.0, 0.0, 0.0, 5.0, 5.0) {
            viewOrder = 6.0
        }

        line {
            viewOrder = 6.0
            fun updateGeometry() {
                val x = this.endX - this.startX
                val y = this.endY - this.startY
                val angle = atan2(x, y) + PI/2
                val offsetX = 10.0 * sin(angle)
                val offsetY = 10.0 * cos(angle)
                val textOffsetX = 30.0 * sin(angle)
                val textOffsetY = 30.0 * cos(angle)

                startX = this@StateTransactionArrow.startX - this@group.layoutX + offsetX
                startY = this@StateTransactionArrow.startY - this@group.layoutY + offsetY
                endX = this@StateTransactionArrow.endX - this@group.layoutX + offsetX
                endY = this@StateTransactionArrow.endY - this@group.layoutY + offsetY

                arrowhead.translateX = offsetX
                arrowhead.translateY = offsetY
                arrowhead.rotate = - angle / PI * 180.0

                predicateText.translateX = textOffsetX
                predicateText.translateY = textOffsetY
            }

            this@StateTransactionArrow.startXProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.startYProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.endXProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.endYProperty.onChange { updateGeometry() }
        }

        addEventHandler(MouseEvent.MOUSE_CLICKED) {
            isTextEditMode = true
            arrowTextEditor.requestFocus()
        }
    }

    companion object {
        private const val TextFieldLength = 200.0
    }
}