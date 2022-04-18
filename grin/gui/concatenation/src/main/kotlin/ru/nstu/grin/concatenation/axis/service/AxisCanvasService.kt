package ru.nstu.grin.concatenation.axis.service

import javafx.scene.text.Font
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.UpdateAxisChangeSet
import ru.nstu.grin.concatenation.axis.model.UpdateLogarithmicTypeChangeSet
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class AxisCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun updateAxis(
        axis: ConcatenationAxis,
        axisChangeSet: UpdateAxisChangeSet,
        logarithmicChangeSet: UpdateLogarithmicTypeChangeSet
    ) {
        axis.scaleProperties = axis.scaleProperties.copy(
            scalingType = axisChangeSet.axisScalingType,
            scalingLogBase = logarithmicChangeSet.logarithmBase,
            minValue = axisChangeSet.min,
            maxValue = axisChangeSet.max,
        )

        axis.styleProperties = axis.styleProperties.copy(
            backgroundColor = axisChangeSet.axisColor,
            borderHeight = axisChangeSet.borderHeight,
            marksDistanceType = axisChangeSet.marksDistanceType,
            marksDistance = axisChangeSet.marksDistance,
            marksColor = axisChangeSet.fontColor,
            marksFont = Font.font(axisChangeSet.font, axisChangeSet.textSize),
            isVisible = axisChangeSet.isHide
        )

        coroutineScope.launch {
            model.reportAxesListUpdate()
        }
    }
}