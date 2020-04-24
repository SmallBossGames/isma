package ru.nstu.grin.concatenation.view.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.model.ConcatenationAxis
import ru.nstu.grin.concatenation.model.DraggedSettings
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class DraggedHandler(
    val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<MouseEvent> {
    private val currentCanvasSettings: MutableMap<ConcatenationAxis, DraggedSettings> = mutableMapOf()

    override fun handle(event: MouseEvent) {
        if (model.pointToolTipSettings.isShow) return
        if (!event.isPrimaryButtonDown) return

        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val axis = axises.firstOrNull {
            it.isLocated(event.x, event.y)
        } ?: return

        val draggedSettings = getDraggedSettings(axis)

        if (draggedSettings.lastX == -1.0) draggedSettings.lastX = event.x
        if (draggedSettings.lastY == -1.0) draggedSettings.lastY = event.y


        if (axis.isXAxis()) {
            when {
                event.x < draggedSettings.lastX -> {
                    axis.settings.correlation -= DELTA
                }
                event.x > draggedSettings.lastX -> {
                    axis.settings.correlation += DELTA
                }
                else -> {
                }
            }
        } else {
            when {
                event.y < draggedSettings.lastY -> {
                    axis.settings.correlation -= DELTA
                }
                event.y > draggedSettings.lastY -> {
                    axis.settings.correlation += DELTA
                }
                else -> {
                }
            }
        }
        draggedSettings.lastX = event.x
        draggedSettings.lastY = event.y
        currentCanvasSettings[axis] = draggedSettings
        chainDrawer.draw()
    }

    private fun getDraggedSettings(axis: ConcatenationAxis): DraggedSettings {
        return currentCanvasSettings[axis] ?: DraggedSettings()
    }

    private companion object {
        const val DELTA = 2.5
    }
}