package ru.isma.next.editor.blueprint.controls

import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.utilities.ClickDisambiguator
import ru.isma.next.editor.blueprint.viewmodels.LoopTransactionViewModel


class LoopTransactionArrow(
    val viewModel: LoopTransactionViewModel,
    val stateViewModel: ru.isma.next.editor.blueprint.viewmodels.StateViewModel,
    val onClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onArrowClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onArrowDoubleClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
): Group() {
    init {
        layoutXProperty().bind(stateViewModel.centerX())
        layoutYProperty().bind(stateViewModel.centerY())
        viewOrder = 4.0

        val clickDisambiguator = ClickDisambiguator(
            singleClick = { onArrowClick(this@LoopTransactionArrow, it) },
            doubleClick = { onArrowDoubleClick(this@LoopTransactionArrow, it) },
            clickDelay = 200L
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

                textProperty().bind(viewModel.displayText)
            }
        )

        setOnMouseClicked {
            onClick(this@LoopTransactionArrow, it)
        }
    }
}
