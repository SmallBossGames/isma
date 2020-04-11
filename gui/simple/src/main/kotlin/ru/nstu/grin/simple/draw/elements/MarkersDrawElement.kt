package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.view.SimplePlotSettings

class MarkersDrawElement(
    private val settings: SimplePlotSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        val zeroX = context.canvas.width / 2 + settings.xCorrelation
        val relativeX = zeroX - MARKER_MARGIN
        val zeroY = context.canvas.height / 2 + settings.yCorrelation
        val relativeY = zeroY + MARKER_MARGIN

        context.stroke = Color.valueOf("5F5F5F")

        context.strokeText("0", relativeX, relativeY)

        var currentX = zeroX + settings.pixelCost
        var currentCount = settings.step
        while (currentX < context.canvas.width) {
            context.strokeText(currentCount.toString(), currentX, relativeY)
            currentCount += settings.step
            currentX += settings.pixelCost
        }

        currentX = zeroX - settings.pixelCost
        currentCount = -settings.step
        while (currentX > 0) {
            context.strokeText(currentCount.toString(), currentX, relativeY)
            currentCount -= settings.step
            currentX -= settings.pixelCost
        }

        var currentY = zeroY - settings.pixelCost
        currentCount = settings.step
        while (currentY > 0) {
            context.strokeText(currentCount.toString(), relativeX - 5, currentY)
            currentY -= settings.pixelCost
            currentCount += settings.step
        }

        currentY = zeroY + settings.pixelCost
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