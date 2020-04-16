package ru.nstu.grin.simple.view

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel

class ReleaseMouseHandler(
    private val model: SimpleCanvasViewModel,
    private val chainDrawer: SimpleChainDrawer
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY) model.pointToolTipSettings.isShow = false
        chainDrawer.draw()
    }
}