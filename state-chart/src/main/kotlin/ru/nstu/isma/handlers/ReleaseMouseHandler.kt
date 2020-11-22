package ru.nstu.isma.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.isma.model.ElementsViewModel
import ru.nstu.isma.model.StateChartCanvasModel
import ru.nstu.isma.model.entity.ElementType
import ru.nstu.isma.model.entity.Point
import ru.nstu.isma.model.entity.StateElement
import tornadofx.Controller

class ReleaseMouseHandler : EventHandler<MouseEvent>, Controller() {
    private val model: StateChartCanvasModel by inject()
    private val elementsModel: ElementsViewModel by inject()

    override fun handle(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY) {
            println("Add selected")
            addSelectedElement(event.x, event.y)
        }
    }

    private fun addSelectedElement(x: Double, y: Double) {
        val center = Point(x, y)
        val selectedElement = elementsModel.selectedElement
        when (selectedElement) {
            ElementType.STATE -> {
                model.stateElements.add(
                    StateElement(
                        center, 10.0
                    )
                )
            }
        }
    }
}