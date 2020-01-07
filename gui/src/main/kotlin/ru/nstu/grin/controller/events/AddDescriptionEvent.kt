package ru.nstu.grin.controller.events

import ru.nstu.grin.dto.DescriptionDTO
import tornadofx.FXEvent

class AddDescriptionEvent(val descriptionDTO: DescriptionDTO) : FXEvent()