package ru.nstu.grin.concatenation.description.events

import ru.nstu.grin.common.model.Description
import tornadofx.FXEvent

data class GetAllDescriptionsEvent(
    val descriptions: List<Description>
) : FXEvent()