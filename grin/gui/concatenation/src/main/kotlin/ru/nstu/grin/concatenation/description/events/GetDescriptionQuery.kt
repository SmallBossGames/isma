package ru.nstu.grin.concatenation.description.events

import tornadofx.FXEvent
import java.util.*

data class GetDescriptionQuery(
    val id: UUID
) : FXEvent()