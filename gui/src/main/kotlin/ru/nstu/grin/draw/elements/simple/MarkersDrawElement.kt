package ru.nstu.grin.draw.elements.simple

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.view.ChainDrawElement
import ru.nstu.grin.view.simple.SimplePlotSettings

class MarkersDrawElement(
    private val settings: SimplePlotSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        val middleWidth = context.canvas.width / 2
        val relativeX = middleWidth - MARKER_MARGIN
        val middleHeight = context.canvas.height / 2
        val relativeY = middleHeight + MARKER_MARGIN

        context.stroke = Color.valueOf("5F5F5F")

        context.strokeText("0", relativeX, relativeY)

        var currentX = middleWidth + settings.pixelCost
        var currentCount = settings.step
        while (currentX < context.canvas.width) {
            context.strokeText(currentCount.toString(), currentX, relativeY)
            currentCount += settings.step
            currentX += settings.pixelCost
        }

        currentX = middleWidth - settings.pixelCost
        currentCount = -settings.step
        while (currentX > 0) {
            context.strokeText(currentCount.toString(), currentX, relativeY)
            currentCount -= settings.step
            currentX -= settings.pixelCost
        }

        var currentY = middleHeight - settings.pixelCost
        currentCount = settings.step
        while (currentY > 0) {
            context.strokeText(currentCount.toString(), relativeX - 5, currentY)
            currentY -= settings.pixelCost
            currentCount += settings.step
        }

        currentY = middleHeight + settings.pixelCost
        currentCount = -settings.step
        while (currentY < context.canvas.height) {
            context.strokeText(currentCount.toString(), relativeX - 10, currentY)
            currentY += settings.pixelCost
            currentCount -= settings.step
        }
    }

    private companion object {
        const val MARKER_MARGIN = 15
    }
}