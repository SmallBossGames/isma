package ru.nstu.grin.concatenation.description.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.DescriptionDto
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import java.util.*

class DescriptionCanvasService(
    private val model: ConcatenationCanvasModel,
    private val view: ConcatenationCanvas,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun add(descriptionModel: DescriptionDto) {
        val description = Description(
            id = UUID.randomUUID(),
            text = descriptionModel.text,
            textSize = descriptionModel.textSize,
            x = descriptionModel.x,
            y = descriptionModel.y,
            color = descriptionModel.color,
            font = descriptionModel.font
        )

        model.descriptions.add(description)
        reportUpdate()
        view.redraw()
    }

    fun update(description: Description, descriptionModel: DescriptionDto) {
        description.x = descriptionModel.x
        description.y = descriptionModel.y
        description.text = descriptionModel.text
        description.textSize = descriptionModel.textSize
        description.color = descriptionModel.color
        description.font = descriptionModel.font

        reportUpdate()

        view.redraw()
    }

    private fun reportUpdate() = coroutineScope.launch {
        model.reportDescriptionsListUpdate()
    }

    fun delete(description: Description) {
        model.descriptions.remove(description)
        reportUpdate()
        view.redraw()
    }
}