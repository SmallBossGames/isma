package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.model.Offsets
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel

class AxisDrawElement(
    private val canvasModel: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val verticalAxisDraw: VerticalAxisDrawStrategy,
    private val horizontalAxisDraw: HorizontalAxisDrawStrategy,
) : ChainDrawElement {

    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        val offsets = Offsets()

        for (cartesianSpace in canvasModel.cartesianSpaces) {
            for (axis in cartesianSpace.axes){
                if (!axis.styleProperties.isVisible) {
                    context.drawAxisBackground(
                        offsets,
                        axis.styleProperties.borderHeight,
                        axis.direction,
                        axis.styleProperties.backgroundColor,
                        axis.styleProperties.marksColor,
                    )
                    drawAxisMarks(
                        context,
                        offsets,
                        axis
                    )
                    offsets.increase(
                        axis.styleProperties.borderHeight,
                        axis.direction
                    )
                }
            }
        }
    }

    private fun drawAxisMarks(
        context: GraphicsContext,
        offsets: Offsets,
        axis: ConcatenationAxis,
    ) {
        val direction = axis.direction
        val axisHeight = axis.styleProperties.borderHeight
        val canvasWidth = canvasViewModel.canvasWidth
        val canvasHeight = canvasViewModel.canvasHeight

        val marksCoordinate = offsets.getByDirection(direction) + axisHeight / 2

        when (direction) {
            Direction.LEFT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    axis,
                    marksCoordinate,
                )
            }
            Direction.RIGHT -> {
                verticalAxisDraw.drawMarks(
                    context,
                    axis,
                    canvasWidth - marksCoordinate,
                )
            }
            Direction.TOP -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    axis,
                    marksCoordinate,
                )
            }
            Direction.BOTTOM -> {
                horizontalAxisDraw.drawMarks(
                    context,
                    axis,
                    canvasHeight - marksCoordinate,
                )
            }
        }
    }

    private fun GraphicsContext.drawAxisBackground(
        offsets: Offsets,
        axisHeight: Double,
        direction: Direction,
        backgroundColor: Color,
        lineColor: Color,
    ) {
        save()

        val canvasWidth = canvasViewModel.canvasWidth
        val canvasHeight = canvasViewModel.canvasHeight
        val functionsArea = canvasViewModel.functionsArea

        fill = backgroundColor
        stroke = lineColor

        when (direction) {
            Direction.LEFT -> {
                val x = offsets.left
                val y = functionsArea.top
                val height = canvasHeight - functionsArea.top - functionsArea.bottom

                strokeLine(x + axisHeight, y, x + axisHeight, y + height)
                fillRect(x, y, axisHeight, height)
            }
            Direction.RIGHT -> {
                val x = canvasWidth - offsets.right - axisHeight
                val y = functionsArea.top
                val height = canvasHeight - functionsArea.top - functionsArea.bottom

                strokeLine(x, y, x, y + height)
                fillRect(x, y, axisHeight, height)
            }
            Direction.TOP -> {
                val x = functionsArea.left
                val y = offsets.top
                val width = canvasWidth - functionsArea.left - functionsArea.right

                strokeLine(x, y + axisHeight, x + width, y + axisHeight)
                fillRect(x, y, width, axisHeight)
            }
            Direction.BOTTOM -> {
                val x = functionsArea.left
                val y = canvasHeight - offsets.bottom - axisHeight
                val width = canvasWidth - functionsArea.left - functionsArea.right

                strokeLine(x, y , x + width, y)
                fillRect(x, y, width, axisHeight)
            }
        }

        restore()
    }
}