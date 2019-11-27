package ru.nstu.grin.extensions

import javafx.beans.property.SimpleListProperty
import javafx.collections.ListChangeListener
import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.Drawable

fun <T : Drawable> drawListener(listProperty: SimpleListProperty<T>, graphicsContext2D: GraphicsContext) {
    listProperty.addListener { listener: ListChangeListener.Change<out T> ->
        listener.list.forEach {
            it.draw(graphicsContext2D)
        }
    }
}