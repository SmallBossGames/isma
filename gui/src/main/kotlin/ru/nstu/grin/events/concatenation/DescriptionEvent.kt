package ru.nstu.grin.events.concatenation

import ru.nstu.grin.dto.common.DescriptionDTO
import tornadofx.FXEvent

class DescriptionEvent(val descriptionDTO: DescriptionDTO) : FXEvent()