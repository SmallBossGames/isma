package ru.isma.next.editor.blueprint.controls


import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
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

class StateTransactionArrow(
    onClick: (source: StateTransactionArrow, event: MouseEvent) -> Unit,
    onArrowClick: (source: StateTransactionArrow, event: MouseEvent) -> Unit,
) : Group() {
    val aliasProperty = SimpleStringProperty("")
    val textProperty = SimpleStringProperty("")

    val startXProperty = SimpleDoubleProperty(0.0)
    val startYProperty = SimpleDoubleProperty(0.0)
    val endXProperty = SimpleDoubleProperty(0.0)
    val endYProperty = SimpleDoubleProperty(0.0)

    val startX by startXProperty
    val startY by startYProperty
    val endX by endXProperty
    val endY by endYProperty

    var alias: String by aliasProperty
    var text: String by textProperty

    init {
        viewOrder = 4.0
        layoutXProperty().bind((endXProperty.subtract(startXProperty)).divide( 2).add(startXProperty))
        layoutYProperty().bind((endYProperty.subtract(startYProperty)).divide(2).add(startYProperty))

        //val popover = createEditPopOver()

        val predicateText = Label().apply {
            font = Font("Arial", 16.0)
            prefWidth = TextFieldLength
            translateY = -10.0
            translateX = -TextFieldLength / 2.0
            alignment = Pos.CENTER
        }

        val predicateTextWrapped = Group(predicateText)

        val arrowhead = Group(
            Polygon(7.0, -7.0, -7.0, 0.0, 7.0, 7.0).apply {
                strokeWidth = 3.0
                viewOrder = 6.0
            }
        ).apply {
            setOnMouseClicked { onArrowClick(this@StateTransactionArrow, it) }
        }

        children.addAll(arrowhead, predicateTextWrapped)

        line {
            strokeWidth = 3.0
            viewOrder = 6.0

            fun updateGeometry() {
                val x = this@StateTransactionArrow.endX - this@StateTransactionArrow.startX
                val y = this@StateTransactionArrow.endY - this@StateTransactionArrow.startY
                val angle = atan2(x, y) + PI / 2
                val offsetX = 10.0 * sin(angle)
                val offsetY = 10.0 * cos(angle)
                val textOffsetX = 75.0 * sin(angle)
                val textOffsetY = 50.0 * cos(angle)

                startX = this@StateTransactionArrow.startX - this@StateTransactionArrow.layoutX + offsetX
                startY = this@StateTransactionArrow.startY - this@StateTransactionArrow.layoutY + offsetY
                endX = this@StateTransactionArrow.endX - this@StateTransactionArrow.layoutX + offsetX
                endY = this@StateTransactionArrow.endY - this@StateTransactionArrow.layoutY + offsetY

                arrowhead.translateX = offsetX
                arrowhead.translateY = offsetY
                arrowhead.rotate = -angle / PI * 180.0

                predicateTextWrapped.translateX = textOffsetX
                predicateTextWrapped.translateY = textOffsetY
            }

            this@StateTransactionArrow.startXProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.startYProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.endXProperty.onChange { updateGeometry() }
            this@StateTransactionArrow.endYProperty.onChange { updateGeometry() }
        }

        fun updatePredicateText(){
            val alias = aliasProperty.value
            val predicate = textProperty.value

            predicateText.text = if (alias != "") alias else predicate
        }

        aliasProperty.onChange {
            updatePredicateText()
        }

        textProperty.onChange {
            updatePredicateText()
        }

        setOnMouseClicked { onClick(this@StateTransactionArrow, it) }
    }

    fun <T> ObservableValue<T>.onChange(op: () -> Unit){
        this.addListener{ _, _, value ->
            op()
        }
    }

    companion object {
        private const val TextFieldLength = 120.0

        inline fun Group.line(crossinline op: Line.() -> Unit){
            this.children.add(
                Line().apply(op)
            )
        }
    }
}