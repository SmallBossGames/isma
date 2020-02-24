package ru.nstu.grin.controller

import ru.nstu.grin.converters.dto.DescriptionDTOConverter
import ru.nstu.grin.model.view.DescriptionViewModel
import tornadofx.Controller

class DescriptionModalController : Controller() {

    private val model: DescriptionViewModel by inject()

    fun addDescription() {
        val descriptionDTO = DescriptionDTOConverter.convert(model)
//        fire(DescriptionEvent(descriptionDTO))
    }
}