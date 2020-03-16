package ru.nstu.grin.concatenation.model

import javafx.scene.canvas.GraphicsContext

interface Drawable : Locationable {
    fun draw(context: GraphicsContext)
}