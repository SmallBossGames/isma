package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.model.ChangeDescriptionModel
import ru.nstu.grin.concatenation.description.model.UpdateDescriptionModel
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import tornadofx.Controller

class ChangeDescriptionController : Controller() {
    private val descriptionCanvasService: DescriptionCanvasService by inject()

    fun updateDescription(model: ChangeDescriptionModel) {
        val updateDescriptionModel = UpdateDescriptionModel(
            description = model.description,
            text = model.text,
            textSize = model.textSize.toDouble(),
            color = model.color,
            font = model.font
        )

        descriptionCanvasService.update(updateDescriptionModel)
    }
}