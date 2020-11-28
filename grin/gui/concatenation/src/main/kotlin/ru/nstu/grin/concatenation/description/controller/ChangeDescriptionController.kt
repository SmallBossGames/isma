package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.events.GetDescriptionEvent
import ru.nstu.grin.concatenation.description.events.UpdateDescriptionEvent
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionModel
import tornadofx.Controller
import java.util.*

class ChangeDescriptionController : Controller() {
    val descriptionId: UUID by param()
    private val model: ChangeDescriptionModel by inject()

    init {
        subscribe<GetDescriptionEvent> {
            if (descriptionId == it.description.id) {
                model.text = it.description.text
                model.textSize = it.description.textSize.toString()
                model.color = it.description.color
                model.font = it.description.font
            }
        }
    }

    fun updateDescription() {
        val event = UpdateDescriptionEvent(
            id = descriptionId,
            text = model.text,
            textSize = model.textSize.toDouble(),
            color = model.color,
            font = model.font
        )
        fire(event)
    }
}