package ru.nstu.grin.concatenation.description.events

import tornadofx.FXEvent
import java.util.*

data class DeleteDescriptionQuery(
    val id: UUID
) : FXEvent()