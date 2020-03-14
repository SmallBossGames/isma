package ru.nstu.grin.common.controller

import ru.nstu.grin.common.converters.dto.DescriptionDTOConverter
import ru.nstu.grin.common.model.view.DescriptionViewModel
import tornadofx.Controller

class DescriptionModalController : Controller() {

    private val model: DescriptionViewModel by inject()

    fun addDescription() {
        val descriptionDTO = DescriptionDTOConverter.convert(model)
//        fire(DescriptionEvent(descriptionDTO))
    }
}