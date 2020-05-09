package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import tornadofx.Controller

class ReleaseMouseHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()

    override fun handle(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY) {
            println("Release primary button")
            model.selectionSettings.dropToDefault()
        }
//        if (event.button == MouseButton.PRIMARY) model.pointToolTipSettings.isShow = false
        chainDrawer.draw()
    }
}