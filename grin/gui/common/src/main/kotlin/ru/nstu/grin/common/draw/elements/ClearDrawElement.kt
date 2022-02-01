package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.view.ChainDrawElement

object ClearDrawElement : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.clearRect(0.0, 0.0, SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight())
    }
}