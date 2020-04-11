package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.view.SimplePlotSettings

class GridDrawElement(
    private val size: Double,
    private val color: Color,
    private val settings: SimplePlotSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.stroke = color

        val middleWidth = context.canvas.width / 2 + settings.xCorrelation
        val middleHeight = context.canvas.height / 2 + settings.yCorrelation

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