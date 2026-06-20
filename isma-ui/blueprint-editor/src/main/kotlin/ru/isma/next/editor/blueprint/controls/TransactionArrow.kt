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
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.utilities.calculateArrowGeometry
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue

interface ITransactionArrowData {
    val aliasProperty: SimpleStringProperty
    val predicateProperty: SimpleStringProperty
}

class TransactionArrow(
    onClick: (source: TransactionArrow, event: MouseEvent) -> Unit,
    onArrowClick: (source: TransactionArrow, event: MouseEvent) -> Unit,
) : Group(), ITransactionArrowData {
    override val aliasProperty = SimpleStringProperty("")
    override val predicateProperty = SimpleStringProperty("")

    val startXProperty = SimpleDoubleProperty(0.0)
    val startYProperty = SimpleDoubleProperty(0.0)
    val endXProperty = SimpleDoubleProperty(0.0)
    val endYProperty = SimpleDoubleProperty(0.0)

    val startX by startXProperty
    val startY by startYProperty
    val endX by endXProperty
    val endY by endYProperty

    var alias: String by aliasProperty
    var text: String by predicateProperty

    init {
        viewOrder = 4.0
        layoutXProperty().bind((endXProperty.subtract(startXProperty)).divide( 2).add(startXProperty))
        layoutYProperty().bind((endYProperty.subtract(startYProperty)).divide(2).add(startYProperty))

        val predicateText = Label().apply {
            font = Font("Arial", ARROW_LABEL_FONT_SIZE)
            prefWidth = ARROW_LABEL_FIELD_WIDTH
            translateY = LOOP_LABEL_Y_OFFSET
            translateX = -ARROW_LABEL_FIELD_WIDTH / 2.0
            alignment = Pos.CENTER
        }

        val predicateTextWrapped = Group(predicateText)

        val arrowhead = Group(
            Polygon(ARROWHEAD_WIDTH, -ARROWHEAD_WIDTH, -ARROWHEAD_WIDTH, 0.0, ARROWHEAD_WIDTH, ARROWHEAD_WIDTH).apply {
                strokeWidth = ARROWHEAD_STROKE
                viewOrder = 6.0
            }
        ).apply {
            setOnMouseClicked { onArrowClick(this@TransactionArrow, it) }
        }

        children.addAll(arrowhead, predicateTextWrapped)

        line {
            strokeWidth = ARROW_LINE_STROKE
            viewOrder = 6.0

            fun updateGeometry() {
                val geometry = calculateArrowGeometry(
                    startX = this@TransactionArrow.startX,
                    startY = this@TransactionArrow.startY,
                    endX = this@TransactionArrow.endX,
                    endY = this@TransactionArrow.endY,
                    layoutX = this@TransactionArrow.layoutX,
                    layoutY = this@TransactionArrow.layoutY,
                    lineOffset = ARROW_LINE_OFFSET,
                    textXOffset = ARROW_TEXT_X_OFFSET,
                    textYOffset = ARROW_TEXT_Y_OFFSET
                )

                startX = geometry.lineStartX
                startY = geometry.lineStartY
                endX = geometry.lineEndX
                endY = geometry.lineEndY

                arrowhead.translateX = geometry.arrowheadTranslateX
                arrowhead.translateY = geometry.arrowheadTranslateY
                arrowhead.rotate = geometry.arrowheadRotation

                predicateTextWrapped.translateX = geometry.labelTextTranslateX
                predicateTextWrapped.translateY = geometry.labelTextTranslateY
            }

            this@TransactionArrow.startXProperty.onChange { updateGeometry() }
            this@TransactionArrow.startYProperty.onChange { updateGeometry() }
            this@TransactionArrow.endXProperty.onChange { updateGeometry() }
            this@TransactionArrow.endYProperty.onChange { updateGeometry() }
        }

        fun updatePredicateText(){
            val alias = aliasProperty.value
            val predicate = predicateProperty.value

            predicateText.text = if (alias != "") alias else predicate
        }

        aliasProperty.onChange {
            updatePredicateText()
        }

        predicateProperty.onChange {
            updatePredicateText()
        }

        setOnMouseClicked { onClick(this@TransactionArrow, it) }
    }

    fun <T> ObservableValue<T>.onChange(op: () -> Unit){
        this.addListener{ _, _, _ ->
            op()
        }
    }

    companion object {
        inline fun Group.line(crossinline op: Line.() -> Unit){
            this.children.add(
                Line().apply(op)
            )
        }
    }
}