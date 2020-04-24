package ru.nstu.grin.concatenation.view.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class ReleaseMouseHandler(
    private val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY) model.pointToolTipSettings.isShow = false
        chainDrawer.draw()
    }
}