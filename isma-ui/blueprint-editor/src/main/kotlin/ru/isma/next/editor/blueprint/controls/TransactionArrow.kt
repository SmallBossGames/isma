package ru.isma.next.editor.blueprint.controls


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
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel
import ru.isma.next.editor.blueprint.viewmodels.TransactionViewModel

class TransactionArrow(
    val viewModel: TransactionViewModel,
    val startViewModel: StateViewModel,
    val endViewModel: StateViewModel,
    val onArrowClick: (TransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onClick: (TransactionArrow, MouseEvent) -> Unit = { _, _ -> }
) : Group() {

    init {
        viewOrder = 4.0
        layoutXProperty().bind(startViewModel.centerX().add(endViewModel.centerX()).divide(2))
        layoutYProperty().bind(startViewModel.centerY().add(endViewModel.centerY()).divide(2))

        val predicateText = Label().apply {
            font = Font("Arial", ARROW_LABEL_FONT_SIZE)
            prefWidth = ARROW_LABEL_FIELD_WIDTH
            translateY = LOOP_LABEL_Y_OFFSET
            translateX = -ARROW_LABEL_FIELD_WIDTH / 2.0
            alignment = Pos.CENTER
        }
        predicateText.textProperty().bind(viewModel.displayText)

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
                val geoStartX = startViewModel.centerX().value
                val geoStartY = startViewModel.centerY().value
                val geoEndX = endViewModel.centerX().value
                val geoEndY = endViewModel.centerY().value
                val layoutX = this@TransactionArrow.layoutXProperty().value
                val layoutY = this@TransactionArrow.layoutYProperty().value

                val geometry = calculateArrowGeometry(
                    startX = geoStartX,
                    startY = geoStartY,
                    endX = geoEndX,
                    endY = geoEndY,
                    layoutX = layoutX,
                    layoutY = layoutY,
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

            updateGeometry()

            startViewModel.xProperty.onChange { updateGeometry() }
            startViewModel.yProperty.onChange { updateGeometry() }
            startViewModel.squareWidthProperty.onChange { updateGeometry() }
            startViewModel.squareHeightProperty.onChange { updateGeometry() }
            endViewModel.xProperty.onChange { updateGeometry() }
            endViewModel.yProperty.onChange { updateGeometry() }
            endViewModel.squareWidthProperty.onChange { updateGeometry() }
            endViewModel.squareHeightProperty.onChange { updateGeometry() }
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
