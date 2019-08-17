package ru.nstu.grin

import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import tornadofx.*


class GrinCanvas : View() {

    val canvas: Canvas = Canvas()

    override val root: Parent = stackpane {
        group {
            polyline(0.0, 0.0, 80.0, 40.0, 40.0, 80.0)
            canvas {

            }
        }
    }
}