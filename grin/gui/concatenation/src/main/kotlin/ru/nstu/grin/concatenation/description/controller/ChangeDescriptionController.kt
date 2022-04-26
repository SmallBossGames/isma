package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.model.DescriptionDto
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionViewModel
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService

class ChangeDescriptionController(
    private val descriptionCanvasService: DescriptionCanvasService,
    private val matrixTransformer: MatrixTransformer,
) {
    fun updateOrCreateDescription(model: ChangeDescriptionViewModel) {
        val space = model.cartesianSpace!!
        val xAxis = space.xAxis
        val yAxis = space.yAxis

        val x = matrixTransformer.transformPixelToUnits(
            model.xPosition,
            xAxis.scaleProperties,
            xAxis.direction
        )

        val y = matrixTransformer.transformPixelToUnits(
            model.yPosition,
            yAxis.scaleProperties,
            yAxis.direction
        )

        val descriptionModel = DescriptionDto(
            space = model.cartesianSpace,
            x = x,
            y = y,
            text = model.text,
            textSize = model.textSize,
            color = model.color,
            font = model.font
        )

        val description = model.description

        if(description != null){
            descriptionCanvasService.update(
                model.description,
                descriptionModel
            )
        } else {
            descriptionCanvasService.add(descriptionModel)
        }
    }
}