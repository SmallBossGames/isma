package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.view.DescriptionViewModel
import ru.nstu.grin.concatenation.description.events.AddDescriptionEvent
import tornadofx.Controller
import java.util.*

class DescriptionModalController : Controller() {

    private val model: DescriptionViewModel by inject()

    fun addDescription() {
        val description = Description(
            id = UUID.randomUUID(),
            text = model.text,
            size = model.size,
            x = model.x,
            y = model.y,
            color = model.textColor,
            font = model.font
        )

        fire(AddDescriptionEvent(description = description))
    }
}