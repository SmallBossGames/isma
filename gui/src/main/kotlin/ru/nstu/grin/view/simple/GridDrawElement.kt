package ru.nstu.grin.view.simple

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.view.ChainDrawElement

class GridDrawElement : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.stroke = Color.valueOf("BBBBBB")

        val middleWidth = context.canvas.width / 2
        val middleHeight = context.canvas.height / 2

        // Second quadrant
        var currentX = middleWidth
        while (currentX > 0) {
            var currentY = middleHeight
            while (currentY > 0) {
                context.strokeLine(
                    currentX,
                    currentY - DEFAULT_KUBE_SIZE,
                    currentX - DEFAULT_KUBE_SIZE,
                    currentY - DEFAULT_KUBE_SIZE
                )
                context.strokeLine(
                    currentX - DEFAULT_KUBE_SIZE,
                    currentY - DEFAULT_KUBE_SIZE,
                    currentX - DEFAULT_KUBE_SIZE,
                    currentY
                )
                currentY -= DEFAULT_KUBE_SIZE
            }
            currentX -= DEFAULT_KUBE_SIZE
        }

        //first quadrant
        currentX = middleWidth
        while (currentX < context.canvas.width) {
            var currentY = middleHeight
            while (currentY > 0) {
                context.strokeLine(
                    currentX,
                    currentY - DEFAULT_KUBE_SIZE,
                    currentX + DEFAULT_KUBE_SIZE,
                    currentY - DEFAULT_KUBE_SIZE
                )
                context.strokeLine(
                    currentX + DEFAULT_KUBE_SIZE,
                    currentY - DEFAULT_KUBE_SIZE,
                    currentX + DEFAULT_KUBE_SIZE,
                    currentY
                )
                currentY -= DEFAULT_KUBE_SIZE
            }
            currentX += DEFAULT_KUBE_SIZE
        }

        //third quadrant
        currentX = middleWidth
        while (currentX > 0) {
            var currentY = middleHeight
            while (currentY < context.canvas.height) {
                context.strokeLine(
                    currentX,
                    currentY + DEFAULT_KUBE_SIZE,
                    currentX - DEFAULT_KUBE_SIZE,
                    currentY + DEFAULT_KUBE_SIZE
                )
                context.strokeLine(
                    currentX - DEFAULT_KUBE_SIZE,
                    currentY + DEFAULT_KUBE_SIZE,
                    currentX - DEFAULT_KUBE_SIZE,
                    currentY
                )
                currentY += DEFAULT_KUBE_SIZE
            }
            currentX -= DEFAULT_KUBE_SIZE
        }

        // fourth
        currentX = middleWidth
        while (currentX < context.canvas.width) {
            var currentY = middleHeight
            while (currentY < context.canvas.height) {
                context.strokeLine(
                    currentX,
                    currentY + DEFAULT_KUBE_SIZE,
                    currentX + DEFAULT_KUBE_SIZE,
                    currentY + DEFAULT_KUBE_SIZE
                )
                context.strokeLine(
                    currentX + DEFAULT_KUBE_SIZE,
                    currentY + DEFAULT_KUBE_SIZE,
                    currentX + DEFAULT_KUBE_SIZE,
                    currentY
                )
                currentY += DEFAULT_KUBE_SIZE
            }
            currentX += DEFAULT_KUBE_SIZE
        }
    }

    private companion object {
        const val DEFAULT_KUBE_SIZE = 120
    }
}