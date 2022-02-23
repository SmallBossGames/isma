package ru.nstu.grin.concatenation.description.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.description.events.*
import tornadofx.Controller

class DescriptionCanvasService : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun add(event: AddDescriptionEvent) {
        model.descriptions.add(event.description)
        view.redraw()
    }

    fun update(event: UpdateDescriptionEvent) {
        val description = model.descriptions.first { it.id == event.id }
        description.text = event.text
        description.textSize = event.textSize
        description.color = event.color
        description.font = event.font
        getAll()
        view.redraw()
    }

    fun get(event: GetDescriptionQuery) {
        val description = model.descriptions.first { it.id == event.id }
        fire(GetDescriptionEvent(description = description))
    }

    fun getAll() {
        coroutineScope.launch {
            model.reportDescriptionsListUpdate()
        }
    }

    fun delete(event: DeleteDescriptionQuery) {
        model.descriptions.removeIf { it.id == event.id }
        getAll()
        view.redraw()
    }
}