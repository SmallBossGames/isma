package ru.nstu.grin.common.events

import ru.nstu.grin.common.dto.DescriptionDTO
import tornadofx.FXEvent

sealed class DescriptionEvent(
    val description: DescriptionDTO
) : FXEvent()

class SimpleDescriptionEvent(
    description: DescriptionDTO
) : DescriptionEvent(description)