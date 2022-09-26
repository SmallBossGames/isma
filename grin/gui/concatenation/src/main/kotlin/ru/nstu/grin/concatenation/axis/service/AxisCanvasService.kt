package ru.nstu.grin.concatenation.axis.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.UpdateAxisChangeSet
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class AxisCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun updateAxis(
        axis: ConcatenationAxis,
        changeSet: UpdateAxisChangeSet,
    ) {
        axis.apply {
            name = changeSet.name
            direction = changeSet.direction
            styleProperties = changeSet.styleProperties
            scaleProperties = changeSet.scaleProperties
        }

        coroutineScope.launch {
            model.reportAxesListUpdate()
        }
    }
}