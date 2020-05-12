package ru.nstu.grin.concatenation.description.service

import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.description.events.AddDescriptionEvent
import tornadofx.Controller

class DescriptionService : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()

    fun addDescription(event: AddDescriptionEvent) {
        model.descriptions.add(event.description)
    }
}