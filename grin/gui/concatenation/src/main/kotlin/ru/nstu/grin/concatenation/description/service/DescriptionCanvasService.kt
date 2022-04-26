package ru.nstu.grin.concatenation.description.service

import javafx.scene.text.Font
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.description.model.Description
import ru.nstu.grin.concatenation.description.model.DescriptionDto
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class DescriptionCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun add(descriptionModel: DescriptionDto) {
        val description = Description(
            text = descriptionModel.text,
            x = descriptionModel.x,
            y = descriptionModel.y,
            color = descriptionModel.color,
            font = Font(descriptionModel.font, descriptionModel.textSize)
        )

        descriptionModel.space.descriptions.add(description)

        reportUpdate()
    }

    fun update(description: Description, descriptionModel: DescriptionDto) {
        description.apply {
            x = descriptionModel.x
            y = descriptionModel.y
            text = descriptionModel.text
            color = descriptionModel.color
            font = Font(descriptionModel.font, descriptionModel.textSize)
        }

        val currentSpace = model.cartesianSpaces.first { it.descriptions.contains(description) }
        val newSpace = descriptionModel.space

        if(currentSpace !== newSpace){
            currentSpace.descriptions.remove(description)
            newSpace.descriptions.add(description)
        }

        reportUpdate()
    }

    fun delete(description: Description) {
        val space = model.cartesianSpaces.first{ it.descriptions.contains(description) }

        space.descriptions.remove(description)

        reportUpdate()
    }

    private fun reportUpdate() = coroutineScope.launch {
        model.reportDescriptionsListUpdate()
    }
}