package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.extensions.findLocatedAxisOrNull
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer

class ScalableScrollHandler(
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: CanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val matrixTransformer: MatrixTransformer,
) : EventHandler<ScrollEvent> {
    override fun handle(event: ScrollEvent) {
        val axis = model.cartesianSpaces.findLocatedAxisOrNull(event.x, event.y, canvasViewModel)

        if (axis != null) {
            handleScaleByAxis(event, axis)
        } else {
            for (cartesianSpace in model.cartesianSpaces) {
                handleScaleByAxis(event, cartesianSpace.xAxis)
                handleScaleByAxis(event, cartesianSpace.yAxis)
            }
        }
        chainDrawer.draw()
    }

    private fun handleScaleByAxis(event: ScrollEvent, axis: ConcatenationAxis) {
        val delta = event.deltaY
        val scaleProperties = axis.scaleProperties

        val units = when(axis.direction){
            Direction.LEFT, Direction.RIGHT ->
                matrixTransformer.transformPixelToUnits(event.y, scaleProperties, axis.direction)
            Direction.TOP, Direction.BOTTOM ->
                matrixTransformer.transformPixelToUnits(event.x, scaleProperties, axis.direction)
        }

        if(delta > 0) {
            axis.scaleProperties = scaleProperties.copy(
                minValue = scaleProperties.minValue * (1.0 - delta * AXIS_SCALING_COEFFICIENT) + units * delta * AXIS_SCALING_COEFFICIENT,
                maxValue = scaleProperties.maxValue * (1.0 - delta * AXIS_SCALING_COEFFICIENT) + units * delta * AXIS_SCALING_COEFFICIENT
            )
        } else {
            axis.scaleProperties = scaleProperties.copy(
                minValue = (scaleProperties.minValue + units * delta * AXIS_SCALING_COEFFICIENT) / (1.0 + delta * AXIS_SCALING_COEFFICIENT),
                maxValue = (scaleProperties.maxValue + units * delta * AXIS_SCALING_COEFFICIENT) / (1.0 + delta * AXIS_SCALING_COEFFICIENT)
            )
        }
    }

    private companion object {
        const val AXIS_SCALING_COEFFICIENT = 0.003
    }
}