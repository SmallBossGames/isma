package ru.nstu.grin.concatenation.function.events

import tornadofx.FXEvent
import java.util.*

data class ShowIntersectionsEvent(
    val id: UUID,
    val secondId: UUID
) : FXEvent()