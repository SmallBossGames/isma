package ru.nstu.grin.concatenation.view

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.DraggedSettings
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class DraggedHandler(
    val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<MouseEvent> {
    private val currentCanvasSettings: MutableMap<CartesianSpace, DraggedSettings> = mutableMapOf()

    override fun handle(event: MouseEvent) {
        if (!event.isPrimaryButtonDown) return

        val axises = model.cartesianSpaces.map {
            listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
        }.flatten()
        val cartesianSpace = axises.firstOrNull {
            it.second.isLocated(event.x, event.y)
        }?.first ?: return

        val draggedSettings = getDraggedSettings(cartesianSpace)

        if (draggedSettings.lastX == -1.0) draggedSettings.lastX = event.x
        if (draggedSettings.lastY == -1.0) draggedSettings.lastY = event.y


        when {
            event.x < draggedSettings.lastX -> {
                cartesianSpace.settings.xCorrelation -= DELTA
            }
            event.x > draggedSettings.lastX -> {
                cartesianSpace.settings.xCorrelation += DELTA
            }
            else -> {
            }
        }

        when {
            event.y < draggedSettings.lastY -> {
                cartesianSpace.settings.yCorrelation -= DELTA
            }
            event.y > draggedSettings.lastY -> {
                cartesianSpace.settings.yCorrelation += DELTA
            }
            else -> {
            }
        }
        draggedSettings.lastX = event.x
        draggedSettings.lastY = event.y
        currentCanvasSettings[cartesianSpace] = draggedSettings
        chainDrawer.draw()
    }

    private fun getDraggedSettings(cartesianSpace: CartesianSpace): DraggedSettings {
        return currentCanvasSettings[cartesianSpace] ?: DraggedSettings()
    }

    private companion object {
        const val DELTA = 2.5
    }
}