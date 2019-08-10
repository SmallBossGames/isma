package org.nstu.grin

import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import org.nstu.grin.axis.Axis
import tornadofx.*


class GrinCanvas : View() {

    val canvas: Canvas = Canvas()

    companion object {
        private const val WIDTH = 300.0
        private const val HEIGHT = 300.0
    }

    override val root: Parent = stackpane {
        group {
            canvas(WIDTH, HEIGHT) {
                Axis(this.graphicsContext2D, 0.0, 0.0, MappingPosition.LEFT)
                Axis(graphicsContext2D, 0.0, 0.0, MappingPosition.BOTTOM)
            }
        }
    }
}