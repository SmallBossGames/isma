package ru.isma.next.editor.blueprint.controls

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.controls.CoroutineScopeProvider
import ru.isma.next.editor.blueprint.utilities.ClickDisambiguator
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue


class LoopTransactionArrow(
    val onClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onArrowClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onArrowDoubleClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    var text: String = "",
    alias: String = "",
    predicate: String = "",
): Group(), ITransactionArrowData {
    override val aliasProperty = SimpleStringProperty(alias)
    override val predicateProperty = SimpleStringProperty(predicate)

    var alias: String by aliasProperty
    var predicate: String by predicateProperty

    init {
        viewOrder = 4.0

        val clickDisambiguator = ClickDisambiguator(
            coroutineScope = CoroutineScopeProvider.scope,
            singleClick = { onArrowClick(this@LoopTransactionArrow, it) },
            doubleClick = { onArrowDoubleClick(this@LoopTransactionArrow, it) }
        )

        children.addAll(
            Circle(LOOP_CIRCLE_RADIUS, Color.TRANSPARENT).apply {
                fill = Color.TRANSPARENT
                stroke = Color.BLACK
                strokeWidth = ARROW_LINE_STROKE

                centerX = LOOP_CIRCLE_CENTER_X
            },
            Group(
                Polygon(0.0, -ARROWHEAD_WIDTH, ARROWHEAD_WIDTH, 0.0, -ARROWHEAD_WIDTH, 0.0).apply {
                    strokeWidth = ARROWHEAD_STROKE
                    viewOrder = 6.0
                }
            ).apply {
                layoutX = LOOP_ARROWHEAD_X

                addEventHandler(MouseEvent.MOUSE_CLICKED){ clickDisambiguator.onClick(it) }
            },
            Label().apply {
                font = Font("Arial", ARROW_LABEL_FONT_SIZE)
                translateY = LOOP_LABEL_Y_OFFSET
                translateX = LOOP_LABEL_X
                alignment = Pos.CENTER

                fun updatePredicateText(){
                    val localAlias = aliasProperty.value
                    val localPredicate = predicateProperty.value

                    text = if (localAlias != "") localAlias else localPredicate
                }

                aliasProperty.addListener { _,_,_ -> updatePredicateText() }
                predicateProperty.addListener { _,_,_ -> updatePredicateText() }

                updatePredicateText()
            }
        )

        setOnMouseClicked {
            onClick(this@LoopTransactionArrow, it)
        }
    }
}