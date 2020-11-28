package ru.nstu.grin.concatenation.function.events

import tornadofx.FXEvent
import java.util.*

data class FunctionCopyQuery(
    val id: UUID,
    val name: String
) : FXEvent()