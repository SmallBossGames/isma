package ru.nstu.grin.model

import javafx.scene.canvas.GraphicsContext

interface Drawable : Scalable, Locationable {
    fun draw(context: GraphicsContext)
}