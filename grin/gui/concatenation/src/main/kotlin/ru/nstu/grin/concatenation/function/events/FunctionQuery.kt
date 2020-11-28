package ru.nstu.grin.concatenation.function.events

import tornadofx.FXEvent
import java.util.*

data class FunctionQuery(
    val id: UUID
) : FXEvent()