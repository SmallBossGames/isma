package ru.nstu.isma.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import ru.nstu.isma.controller.StateChartChainDrawer
import ru.nstu.isma.handlers.DraggedMouseHandler
import ru.nstu.isma.handlers.PressedMouseHandler
import ru.nstu.isma.handlers.ReleaseMouseHandler
import ru.nstu.isma.model.SettingsModel
import ru.nstu.isma.model.StateChartCanvasModel
import ru.nstu.isma.model.entity.StateElement
import tornadofx.*

class StateChartCanvas : View() {
    private val settings: SettingsModel by inject()
    private val model: StateChartCanvasModel by inject()

    private val draggedMouseHandler: DraggedMouseHandler by inject()
    private val pressedMouseHandle: PressedMouseHandler by inject()
    private val releaseMouseHandler: ReleaseMouseHandler by inject()
    private val chainDrawer: StateChartChainDrawer by inject()

    override val root: Parent = vbox {
        canvas(settings.width, settings.height) {
            model.canvas = this

            model.stateElementsProperty.addListener { _: ListChangeListener.Change<out StateElement> ->
                chainDrawer.draw()
            }

            onMouseDragged = draggedMouseHandler
            onMousePressed = pressedMouseHandle
            onMouseReleased = releaseMouseHandler
        }
    }
}