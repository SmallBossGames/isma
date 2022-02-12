package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement

class GridDrawElement(
    private val size: Double,
    private val color: Color,
    private val xCorrelation: Double,
    private val yCorrelation: Double,
    private val isFull: Boolean = false
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        context.stroke = color

        val middleWidth = context.canvas.width / 2 + xCorrelation
        val middleHeight = context.canvas.height / 2 + yCorrelation

        // Second quadrant
        var currentX = middleWidth
        while (currentX > 0) {
            var currentY = middleHeight
            while (currentY > 0) {
                context.strokeLine(
                    currentX,
                    currentY - size,
                    currentX - size,
                    currentY - size
                )
                context.strokeLine(
                    currentX - size,
                    currentY - size,
                    currentX - size,
                    currentY
                )
                currentY -= size
            }
            currentX -= size
        }

        //first quadrant
        currentX = middleWidth
        while (currentX < context.canvas.width) {
            var currentY = middleHeight
            while (currentY > 0) {
                context.strokeLine(
                    currentX,
                    currentY - size,
                    currentX + size,
                    currentY - size
                )
                context.strokeLine(
                    currentX + size,
                    currentY - size,
                    currentX + size,
                    currentY
                )
                currentY -= size
            }
            currentX += size
        }

        //third quadrant
        currentX = middleWidth
        while (currentX > 0) {
            var currentY = middleHeight
            while (currentY < context.canvas.height) {
                context.strokeLine(
                    currentX,
                    currentY + size,
                    currentX - size,
                    currentY + size
                )
                context.strokeLine(
                    currentX - size,
                    currentY + size,
                    currentX - size,
                    currentY
                )
                currentY += size
            }
            currentX -= size
        }

        // fourth
        currentX = middleWidth
        while (currentX < context.canvas.width) {
            var currentY = middleHeight
            while (currentY < context.canvas.height) {
                context.strokeLine(
                    currentX,
                    currentY + size,
                    currentX + size,
                    currentY + size
                )
                context.strokeLine(
                    currentX + size,
                    currentY + size,
                    currentX + size,
                    currentY
                )
                currentY += size
            }
            currentX += size
        }

        if (isFull) {
            context.strokeLine(0.0, middleHeight, context.canvas.width, middleHeight)
            context.strokeLine(middleWidth, 0.0, middleWidth, context.canvas.height)
        }
    }
}