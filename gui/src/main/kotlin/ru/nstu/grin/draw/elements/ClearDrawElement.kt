package ru.nstu.grin.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.settings.SettingsProvider
import ru.nstu.grin.view.ChainDrawElement

class ClearDrawElement : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.clearRect(0.0, 0.0, SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight())
    }
}