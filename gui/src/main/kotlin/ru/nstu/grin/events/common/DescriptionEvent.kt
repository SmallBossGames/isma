package ru.nstu.grin.events.common

import ru.nstu.grin.dto.common.DescriptionDTO
import tornadofx.FXEvent

sealed class DescriptionEvent(
    val description: DescriptionDTO
) : FXEvent()

class ConcatenationDescriptionEvent(
    description: DescriptionDTO
) : DescriptionEvent(description)

class SimpleDescriptionEvent(
    description: DescriptionDTO
) : DescriptionEvent(description)