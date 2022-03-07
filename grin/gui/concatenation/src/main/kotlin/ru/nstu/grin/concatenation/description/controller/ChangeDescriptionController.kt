package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.common.model.DescriptionDto
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionViewModel
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService

class ChangeDescriptionController(
    private val descriptionCanvasService: DescriptionCanvasService
) {
    fun updateOrCreateDescription(model: ChangeDescriptionViewModel) {
        val descriptionModel = DescriptionDto(
            x = model.xPosition,
            y = model.yPosition,
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