package ru.nstu.grin.concatenation.description.events

import ru.nstu.grin.common.model.Description
import tornadofx.FXEvent

data class AddDescriptionEvent(
    val description: Description
) : FXEvent()