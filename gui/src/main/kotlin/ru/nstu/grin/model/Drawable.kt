package ru.nstu.grin.model

import javafx.scene.canvas.GraphicsContext

interface Drawable {
    fun draw(context: GraphicsContext)
}