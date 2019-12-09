package ru.nstu.grin.model

import javafx.scene.canvas.GraphicsContext

interface Drawable : Scalable {
    fun draw(context: GraphicsContext)
}