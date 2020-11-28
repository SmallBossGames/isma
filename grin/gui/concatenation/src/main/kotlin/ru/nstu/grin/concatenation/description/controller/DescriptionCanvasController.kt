package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.events.*
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import tornadofx.Controller

class DescriptionCanvasController : Controller() {
    private val service: DescriptionCanvasService by inject()

    init {
        subscribe<AddDescriptionEvent> { event ->
            service.add(event)
        }
        subscribe<UpdateDescriptionEvent> {
            service.update(it)
        }
        subscribe<GetDescriptionQuery> {
            service.get(it)
        }
        subscribe<GetAllDescriptionQuery> {
            service.getAll()
        }
        subscribe<DeleteDescriptionQuery> {
            service.delete(it)
        }
    }
}