package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.events.AddDescriptionEvent
import ru.nstu.grin.concatenation.description.service.DescriptionService
import tornadofx.Controller

class DescriptionCanvasController : Controller() {
    private val service: DescriptionService by inject()

    init {
        subscribe<AddDescriptionEvent> { event ->
            service.addDescription(event)
        }
    }
}