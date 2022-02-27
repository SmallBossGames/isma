package ru.nstu.grin.concatenation.description.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.description.events.*
import ru.nstu.grin.concatenation.description.model.UpdateDescriptionModel
import tornadofx.Controller

class DescriptionCanvasService : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun add(description: Description) {
        model.descriptions.add(description)
        reportUpdate()
        view.redraw()
    }

    fun update(descriptionModel: UpdateDescriptionModel) {
        val description = descriptionModel.description
        description.text = descriptionModel.text
        description.textSize = descriptionModel.textSize
        description.color = descriptionModel.color
        description.font = descriptionModel.font
        reportUpdate()
        view.redraw()
    }

    fun get(event: GetDescriptionQuery) {
        val description = model.descriptions.first { it.id == event.id }
        fire(GetDescriptionEvent(description = description))
    }

    fun reportUpdate() = coroutineScope.launch {
        model.reportDescriptionsListUpdate()
    }

    fun delete(description: Description) {
        model.descriptions.remove(description)
        reportUpdate()
        view.redraw()
    }
}