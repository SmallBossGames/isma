package ru.nstu.grin.extensions

import javafx.beans.property.SimpleListProperty
import javafx.collections.ListChangeListener
import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.settings.SettingsProvider

fun <T : Drawable> drawListener(listProperty: SimpleListProperty<T>, graphicsContext2D: GraphicsContext) {
    listProperty.addListener { listener: ListChangeListener.Change<out T> ->
        graphicsContext2D.clearRect(0.0, 0.0, SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight())
        listener.list.forEach {
            it.draw(graphicsContext2D)
        }
    }
}