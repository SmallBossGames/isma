package ru.nstu.grin.concatenation.extensions

import javafx.beans.property.SimpleListProperty
import javafx.collections.ListChangeListener
import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.model.Drawable

fun <T : Drawable> drawListener(listProperty: SimpleListProperty<T>, graphicsContext2D: GraphicsContext) {
    listProperty.addListener { listener: ListChangeListener.Change<out T> ->
        graphicsContext2D.clearRect(0.0, 0.0, SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight())
        listener.list.forEach {
            it.draw(graphicsContext2D)
        }
    }
}