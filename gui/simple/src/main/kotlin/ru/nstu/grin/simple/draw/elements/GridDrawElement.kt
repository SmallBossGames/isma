package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement

class GridDrawElement(
    private val size: Double,
    private val color: Color
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.stroke = color

        val middleWidth = context.canvas.width / 2
        val middleHeight = context.canvas.height / 2

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
    }
}