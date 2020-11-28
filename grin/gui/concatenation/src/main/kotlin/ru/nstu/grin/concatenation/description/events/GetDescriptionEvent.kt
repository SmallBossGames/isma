package ru.nstu.grin.concatenation.description.events

import ru.nstu.grin.common.model.Description
import tornadofx.FXEvent

data class GetDescriptionEvent(
    val description: Description
) : FXEvent()