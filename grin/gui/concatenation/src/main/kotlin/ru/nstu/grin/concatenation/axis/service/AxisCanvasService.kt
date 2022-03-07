package ru.nstu.grin.concatenation.axis.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.UpdateAxisChangeSet
import ru.nstu.grin.concatenation.axis.model.UpdateLogarithmicTypeChangeSet
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas

class AxisCanvasService(
    private val model: ConcatenationCanvasModel,
    private val view: ConcatenationCanvas,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun getAllAxises() {
        coroutineScope.launch {
            model.reportAxesListUpdate()
        }
    }

    fun updateAxis(
        axis: ConcatenationAxis,
        axisChangeSet: UpdateAxisChangeSet,
        logarithmicChangeSet: UpdateLogarithmicTypeChangeSet
    ) {
        axis.settings.isLogarithmic = axisChangeSet.axisMarkType == AxisMarkType.LOGARITHMIC

        if (axisChangeSet.axisMarkType == AxisMarkType.LINEAR) {
            axis.settings.isLogarithmic = false
            axis.settings.isOnlyIntegerPow = false
        }

        axis.axisMarkType = axisChangeSet.axisMarkType
        axis.distanceBetweenMarks = axisChangeSet.distance
        axis.textSize = axisChangeSet.textSize
        axis.font = axisChangeSet.font
        axis.fontColor = axisChangeSet.fontColor
        axis.backGroundColor = axisChangeSet.axisColor
        axis.settings.logarithmBase = logarithmicChangeSet.logarithmBase
        axis.settings.isOnlyIntegerPow = logarithmicChangeSet.isOnlyIntegerPow
        axis.settings.integerStep = logarithmicChangeSet.integerStep
        axis.settings.min = axisChangeSet.min
        axis.settings.max = axisChangeSet.max
        axis.isHide = axisChangeSet.isHide
        view.redraw()
        getAllAxises()
    }
}