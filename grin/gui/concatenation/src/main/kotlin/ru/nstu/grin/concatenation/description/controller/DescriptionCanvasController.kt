package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.events.GetAllDescriptionQuery
import ru.nstu.grin.concatenation.description.events.GetDescriptionQuery
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import tornadofx.Controller

class DescriptionCanvasController : Controller() {
    private val service: DescriptionCanvasService by inject()

    init {
        subscribe<GetDescriptionQuery> {
            service.get(it)
        }
        subscribe<GetAllDescriptionQuery> {
            service.reportUpdate()
        }
    }
}